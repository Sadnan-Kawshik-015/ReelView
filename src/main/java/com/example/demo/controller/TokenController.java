package com.example.demo.controller;

import com.example.demo.dtos.output.ResponseModelDTO;
import com.example.demo.service.ApplicationTokenService;
import com.example.demo.service.config.ContextService;
import com.example.demo.utils.constants.CommonMessage;
import com.example.demo.utils.constants.HeaderName;
import com.example.demo.utils.enums.TokenType;
import com.example.demo.utils.securityUtils.AESUtils;
import com.example.demo.utils.securityUtils.JwtUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api", produces = "application/json;charset=UTF-8")
@Tag(name = "token management api")
@Validated
@RequiredArgsConstructor
public class TokenController extends BaseController {
    private final ApplicationTokenService applicationTokenService;
    private final JwtUtils jwtUtils;
    private final ContextService contextService;
    private final AESUtils aesUtils;

    @Tags(value = @Tag(name = "RV003"))
    @GetMapping("/token/renew")
    public ResponseEntity<?> renewToken() {

        try {
            String authHeader = contextService.getHeaderFromRequestContext(HeaderName.AUTHORIZATION);
            String encrypted_auth_token = jwtUtils.parseToken(authHeader,
                    TokenType.BEARER);
            String authToken = aesUtils.decrypt(encrypted_auth_token);

            String refreshHeader = contextService.getHeaderFromRequestContext(HeaderName.REFRESH_TOKEN);
            String encrypted_refresh_token = jwtUtils.parseToken(refreshHeader,
                    TokenType.BEARER);
            String refreshToken = aesUtils.decrypt(encrypted_refresh_token);
            ResponseModelDTO responseModelDTO = applicationTokenService.renewToken(authToken, refreshToken);
            return ResponseEntity.ok(
                    ResponseModelDTO.builder()
                            .status(responseModelDTO.getStatus())
                            .message(CommonMessage.TOKEN_RENEW_SUCCESSFUL)
                            .data(responseModelDTO.getData())
                            .build()
            );
        } catch (Exception e) {
            return doExceptionTask(e);
        }
    }

}
