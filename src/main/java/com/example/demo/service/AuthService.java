package com.example.demo.service;

import com.example.demo.dtos.input.CreateUserDTO;
import com.example.demo.dtos.input.LoginRequestDTO;
import com.example.demo.dtos.output.CreateUserResponseDTO;
import com.example.demo.dtos.output.LoginResponseDTO;
import com.example.demo.entities.Role;
import com.example.demo.entities.User;
import com.example.demo.repositories.BlackListedTokenDAO;
import com.example.demo.repositories.RoleRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.utils.ConstantUtils;
import com.example.demo.utils.MethodUtils;
import com.example.demo.utils.OTPUtils;
import com.example.demo.utils.constants.CommonMessage;
import com.example.demo.utils.customexceptions.RequiredResourceNotFoundException;
import com.example.demo.utils.customexceptions.RoleNotFoundException;
import com.example.demo.utils.customexceptions.UserAlreadyExistsException;
import com.example.demo.utils.customexceptions.UserNotFoundException;
import com.example.demo.utils.enums.JwtClaimName;
import com.example.demo.utils.enums.Status;
import com.example.demo.utils.enums.TokenType;
import com.example.demo.utils.securityUtils.AESUtils;
import com.example.demo.utils.securityUtils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Objects;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService extends BaseService {
    @Value("${app.jwtExpirationMinutes}")
    private int jwt_expiration_minutes;
    @Value("${app.maxLoginFailedAttempt}")
    private int max_login_failed_attempt;
    @Value("${app.maskValue}")
    private String mask_value;
    private final AESUtils aesUtils;

    @Autowired
    @Lazy
    private ApplicationTokenService applicationTokenService;
    private final JwtUtils jwtUtils;
    private final MethodUtils methodUtils;

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authenticationManager;
    private final OTPUtils otpUtils;
    private final BlackListedTokenDAO blacklistedTokenDAO;
    private final RoleRepository roleRepository;

    public Object signInUser(LoginRequestDTO loginRequestDTO) throws Exception {
        User user = null;
        User logUser = new User();
        String login_status = Status.ERROR.getValue();
        try {
            user = userRepository.findByEmail(loginRequestDTO.getEmail())
                    .orElseThrow(() -> new BadCredentialsException(
                            "Incorrect email or password"));
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequestDTO.getEmail(),
                            loginRequestDTO.getPassword()));

            SecurityContextHolder.getContext()
                    .setAuthentication(authentication);
            String encrypted_auth_token = applicationTokenService.generateJwtToken(user);
            String encrypted_refresh_token = applicationTokenService.generateJwtRefreshToken();

            if (user.getFailedLoginAttempt() > 0) {
                user.setFailedLoginAttempt(0);
                userRepository.save(user);
                BeanUtils.copyProperties(user, logUser);

            }
            login_status = Status.SUCCESS.getValue();
            return LoginResponseDTO.builder()
                    .access_token(encrypted_auth_token)
                    .expires_in(jwt_expiration_minutes * 60)
                    .refresh_token(encrypted_refresh_token)
                    .token_type(TokenType.BEARER)
                    .build();
        } catch (BadCredentialsException e) {
            if (user != null) {
                int failed_login_attempt = user.getFailedLoginAttempt();
                failed_login_attempt += 1;
                user.setFailedLoginAttempt(failed_login_attempt);
                if (failed_login_attempt >= max_login_failed_attempt) {
                    user.setLocked(true);
                }
                userRepository.save(user);
                BeanUtils.copyProperties(user, logUser);
            }
            throw new BadCredentialsException("incorrect email or password");
        } catch (Exception e) {
            processException(e);
            return null;
        }
    }

    public LoginResponseDTO generateLoginResponseDTO(User user){
        String encrypted_auth_token = applicationTokenService.generateJwtToken(user);
        String encrypted_refresh_token = applicationTokenService.generateJwtRefreshToken();

        return LoginResponseDTO.builder()
                .access_token(encrypted_auth_token)
                .expires_in(jwt_expiration_minutes * 60)
                .refresh_token(encrypted_refresh_token)
                .token_type(TokenType.BEARER)
                .build();
    }

    public String signOutUser(String authToken) throws Exception {
        String logout_status = null;
        User user = null;
        try {
            user = userRepository.findById(jwtUtils.getClaimFromJwt(authToken, JwtClaimName.ID))
                    .orElseThrow(
                            () -> new UserNotFoundException(CommonMessage.USER_NOT_FOUND));
            Date expire_at = jwtUtils.getExpiryFromJwt(authToken);
            blacklistedTokenDAO.setTokenBlacklisted(user.getId(), aesUtils.encrypt(authToken), expire_at);
            logout_status = Status.SUCCESS.getValue();
            return logout_status;
        } catch (Exception e) {
            processException(e);
            return null;
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public CreateUserResponseDTO signUp(CreateUserDTO inputDTO) throws Exception {

        try {
            if (userRepository.existsByEmail(inputDTO.getEmail())) {
                throw new UserAlreadyExistsException(CommonMessage.EMAIL_ALREADY_EXISTS);
            } else {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                User user = User.builder()
                        .id(ConstantUtils.getUUID())
                        .email(inputDTO.getEmail())
                        .firstName(inputDTO.getFirst_name())
                        .lastName(inputDTO.getLast_name())
                        .mobileNumber(inputDTO.getMobile_number())
                        .password(encoder.encode(inputDTO.getPassword()))
                        .failedLoginAttempt(0)
                        .isLocked(false)
                        .isActive(true)
                        .build();

                Role role = roleRepository.findById(inputDTO.getRole_id())
                        .orElseThrow(() -> new RoleNotFoundException(CommonMessage.ROLE_NOT_FOUND));
                user.setRoles(Set.of(role));
                userRepository.save(user);

                User logUser = new User();
                BeanUtils.copyProperties(user, logUser);

                return CreateUserResponseDTO.builder()
                        .user_id(user.getId())
                        .email(user.getEmail())
                        .first_name(user.getFirstName())
                        .last_name(user.getLastName())
                        .mobile_number(user.getMobileNumber())
                        .role_id(role.getId())
                        .build();
            }
        } catch (Exception e) {
            processException(e);
            return null;
        }

    }


}
