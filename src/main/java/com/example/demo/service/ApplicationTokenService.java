package com.example.demo.service;

import com.example.demo.dtos.helper.TokenDTO;
import com.example.demo.dtos.output.LoginResponseDTO;
import com.example.demo.dtos.output.ResponseModelDTO;
import com.example.demo.entities.User;
import com.example.demo.repositories.RefreshTokenDao;
import com.example.demo.repositories.UserRepository;
import com.example.demo.utils.constants.CommonMessage;
import com.example.demo.utils.customexceptions.RefreshTokenDoesNotExistException;
import com.example.demo.utils.customexceptions.RefreshTokenNotValidException;
import com.example.demo.utils.customexceptions.UserNotFoundException;
import com.example.demo.utils.enums.Status;
import com.example.demo.utils.enums.TokenType;
import com.example.demo.utils.securityUtils.AESUtils;
import com.example.demo.utils.securityUtils.JwtUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.function.BooleanSupplier;

@Service
@RequiredArgsConstructor
public class ApplicationTokenService extends BaseService {
    @Value("${app.jwtSecret}")
    private String jwt_secret;
    @Value("${app.jwtExpirationMinutes}")
    private int jwt_expiry_minutes;
    @Value("${app.refreshTokenExpirationMinutes}")
    private int refresh_token_expiry_minutes;
    private static final int MAX_RETRY_ATTEMPTS = 3;
    private static final int RETRY_DELAY_MS = 100;
    @Autowired
    @Lazy
    private AuthService authService;
    private final RoleService roleService;
    private final RefreshTokenDao refreshTokenDao;

    private final AESUtils aesUtils;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;

    public String generateJwtToken(User user) {

        long jwtExpirationMillis = (long) jwt_expiry_minutes * 60 * 1000;
        Date validity = new Date(new Date().getTime() + jwtExpirationMillis);
        List<String> authorities = roleService.getAuthoritiesFromUser(user);
        String jwt = Jwts.builder()
                .claim("id", user.getId())
                .claim("firstName", user.getFirstName())
                .claim("lastName", Objects.equals(user.getLastName(), null) ? "" : user.getLastName())
                .claim("authorities", authorities)
                .setIssuedAt(new Date())
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS512, jwt_secret)
                .compact();
        return aesUtils.encrypt(jwt);
    }

    public String generateJwtRefreshToken() {
        long refreshTokenExpiryMillis = (long) refresh_token_expiry_minutes * 60 * 1000;
        Date validity = new Date(new Date().getTime() + refreshTokenExpiryMillis);
        String refresh_token = Jwts.builder()
                .setIssuedAt(new Date())
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS512, jwt_secret)
                .compact();
        return aesUtils.encrypt(refresh_token);
    }

    public ResponseModelDTO renewToken(String authToken, String refreshToken) throws Exception {
        try {
            User user = userRepository.findById(contextService.getCurrentUserIdFromRequestContext())
                    .orElseThrow(() -> new UserNotFoundException(
                            CommonMessage.USER_NOT_FOUND));
            String encryptedRefreshToken = aesUtils.encrypt(refreshToken);
            if (!jwtUtils.isJwtTokenValid(refreshToken)) {
                throw new RefreshTokenNotValidException("refresh token is not valid!");
            }
            if (!doesRefreshTokenExist(encryptedRefreshToken, user.getId())) {
                throw new RefreshTokenDoesNotExistException(CommonMessage.REFRESH_TOKEN_DOESN_T_EXIST);
            }
            String new_encrypted_jwt = generateJwtToken(user);
            String new_encrypted_refreshToken = generateJwtRefreshToken();

            // Retry save up to 3 times
            boolean saved = retry(() -> refreshTokenDao.save(new_encrypted_refreshToken, user.getId()));
            if (!saved) {
                throw new RefreshTokenNotValidException("Error while saving new refresh token!");
            }

            // Retry update up to 3 times
            boolean updated = retry(
                    () -> refreshTokenDao.updatePreviousRefreshToken(encryptedRefreshToken));
            if (!updated) {
                throw new RefreshTokenNotValidException("Error while deleting previous refresh token!");
            }

            //refreshTokenDao.save(new_encrypted_refreshToken, user.getId());
            //refreshTokenDao.delete(aesUtils.encrypt(refreshToken));
            // instead of deleting previous token, updating its ttl to 15 seconds.

            authService.signOutUser(authToken);
            LoginResponseDTO loginResponseDTO = LoginResponseDTO.builder()
                    .access_token(new_encrypted_jwt)
                    .expires_in(jwt_expiry_minutes * 60)
                    .refresh_token(
                            new_encrypted_refreshToken)
                    .token_type(TokenType.BEARER)
                    .build();
            return ResponseModelDTO.builder()
                    .status(Status.SUCCESS.getValue())
                    .message("Token renew success!")
                    .data(loginResponseDTO)
                    .build();

        } catch (Exception e) {
            processException(e);
            return null;
        }


    }

    public boolean doesRefreshTokenExist(String encryptedRefreshToken, String userId) throws Exception {
        String tokenJson = refreshTokenDao.getRefreshToken(encryptedRefreshToken);
        if (tokenJson == null) {
            throw new RefreshTokenDoesNotExistException("Refresh token does not exist!");
        }
        TokenDTO tokenDTO = new ObjectMapper().readValue(tokenJson, TokenDTO.class);
        return userId.equals(tokenDTO.getUser_id());
    }

    private boolean retry(BooleanSupplier action) {
        int attempts = 0;
        while (attempts < MAX_RETRY_ATTEMPTS) {
            if (action.getAsBoolean()) {
                return true;
            }
            attempts++;
            try {
                Thread.sleep(RETRY_DELAY_MS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        return false;
    }
}
