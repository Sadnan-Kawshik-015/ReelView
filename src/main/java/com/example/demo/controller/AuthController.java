package com.example.demo.controller;

import com.example.demo.dtos.input.CreateUserDTO;
import com.example.demo.dtos.input.LoginRequestDTO;
import com.example.demo.dtos.output.CreateUserResponseDTO;
import com.example.demo.dtos.output.LoginResponseDTO;
import com.example.demo.dtos.output.ResponseModelDTO;
import com.example.demo.service.AuthService;
import com.example.demo.service.config.ContextService;
import com.example.demo.utils.constants.CommonMessage;
import com.example.demo.utils.constants.HeaderName;
import com.example.demo.utils.enums.Status;
import com.example.demo.utils.enums.TokenType;
import com.example.demo.utils.securityUtils.AESUtils;
import com.example.demo.utils.securityUtils.JwtUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.extensions.Extension;
import io.swagger.v3.oas.annotations.extensions.ExtensionProperty;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/auth", produces = "application/json;charset=UTF-8")
@Tag(name = "auth management api")
@Validated
@RequiredArgsConstructor
public class AuthController extends BaseController {
    private final AuthService authService;
    private final JwtUtils jwtUtils;
    private final AESUtils aesUtils;
    private final ContextService contextService;


    @Tags(value = @Tag(name = "RV001"))
    @PostMapping("/signin")
    public ResponseEntity<?> signInInUser(HttpSession session, @Valid @RequestBody LoginRequestDTO loginRequestDTO) {
        try {
            var response = authService.signInUser(loginRequestDTO);
            if (response.getClass()
                    .equals(LoginResponseDTO.class)) {
                return ResponseEntity.ok(
                        ResponseModelDTO.builder()
                                .status(Status.SUCCESS.getValue())
                                .message(CommonMessage.LOG_IN_SUCCESSFUL)
                                .data(response)
                                .build()
                );
            } else {
                return ResponseEntity.ok(
                        ResponseModelDTO.builder()
                                .status(Status.ERROR.getValue())
                                .message("unknown error!")
                                .data(null)
                                .build()
                );
            }
        } catch (Exception e) {
            return doExceptionTask(e);
        }
    }
    @Tags(value = @Tag(name = "RV002"))
    @GetMapping("/signout")
    public ResponseEntity<?> signOut() {
        try {
            String authHeader = contextService.getHeaderFromRequestContext(HeaderName.AUTHORIZATION);
            String encrypted_auth_token = jwtUtils.parseToken(authHeader, TokenType.BEARER);
            String authToken = aesUtils.decrypt(encrypted_auth_token);
            String status = authService.signOutUser(authToken);
            return ResponseEntity.ok(
                    ResponseModelDTO.builder()
                            .status(status)
                            .message(CommonMessage.LOG_OUT_SUCCESSFUL)
                            .build()
            );
        } catch (Exception e) {
            return doExceptionTask(e);
        }
    }

    @Tags(value = @Tag(name = "RV004"))
    @PostMapping("/signup")
    public ResponseEntity<ResponseModelDTO> signUp(@Valid @RequestBody CreateUserDTO createUserDTO) {
        try {
            CreateUserResponseDTO responseDTO = authService.signUp(createUserDTO);

            return ResponseEntity.ok(ResponseModelDTO.builder()
                    .data(responseDTO)
                    .message(CommonMessage.USER_CREATED_SUCCESSFULLY)
                    .status(Status.SUCCESS.getValue())
                    .build());
        } catch (Exception e) {
            return doExceptionTask(e);
        }
    }



}
