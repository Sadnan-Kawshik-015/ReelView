package com.example.demo.service.config;

import com.example.demo.utils.constants.HeaderName;
import com.example.demo.utils.enums.JwtClaimName;
import com.example.demo.utils.enums.TokenType;
import com.example.demo.utils.securityUtils.AESUtils;
import com.example.demo.utils.securityUtils.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ContextService {
    private final JwtUtils jwtUtils;
    private final AESUtils aesUtils;
    private final HttpServletRequest request;

    public String getHeaderFromRequestContext(String headerName) {
        try {
            return request.getHeader(headerName);
        }catch (Exception e){
            return "";
        }
    }

    public String getCorrelationId() {
        try {
            return request.getHeader(HeaderName.X_CORRELATION_ID);
        }catch (Exception e){
            return "";
        }
    }

    public String getClientIpAddress() {
        try {
            String ipAddress = "";
            if (request != null) {
                ipAddress = request.getHeader(HeaderName.X_FORWARDED_FOR); // the real ip, not the proxy
                if (ipAddress == null || "".equals(ipAddress)) {
                    ipAddress = request.getRemoteAddr();
                }
            }
            return ipAddress;
        } catch (Exception e) {
            return "";
        }
    }

    public String getApiEndpoint() {
        try {
            return request.getRequestURL()
                    .toString();
        }catch (Exception e){
            return "";
        }
    }

    public String getCurrentUserIdFromRequestContext() {
        try {
            String jwt = getAuthToken().orElse(null);
            return jwtUtils.getClaimFromJwt(jwt, JwtClaimName.ID);
        } catch (Exception e) {
            return null;
        }
    }


    public String getAuthorNameFromRequestContext() {
        try {
            String jwt = getAuthToken().orElse(null);
            return jwtUtils.getFullNameFromJwt(jwt);
        } catch (Exception e) {
            return null;
        }
    }

    @SneakyThrows
    private Optional<String> getAuthToken() {
        return Optional.ofNullable(getHeaderFromRequestContext(HeaderName.AUTHORIZATION))
                .filter(ObjectUtils::isNotEmpty)
                .map(e -> getDecryptToken(jwtUtils.parseToken(e, TokenType.BEARER)));
    }

    @SneakyThrows
    private String getDecryptToken(String token) {
        return aesUtils.decrypt(token);
    }
}
