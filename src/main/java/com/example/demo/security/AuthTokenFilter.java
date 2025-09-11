package com.example.demo.security;

import com.example.demo.service.config.ContextService;
import com.example.demo.service.security.UserDetailsServiceImpl;
import com.example.demo.utils.constants.HeaderName;
import com.example.demo.utils.customexceptions.TokenDecryptionException;
import com.example.demo.utils.enums.TokenType;
import com.example.demo.utils.securityUtils.AESUtils;
import com.example.demo.utils.securityUtils.JwtUtils;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AuthTokenFilter extends OncePerRequestFilter {
    private final JwtUtils jwtUtils;
    private final AESUtils aesUtils;
    private final ContextService contextService;

    @SneakyThrows
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) {
        try {
            String authHeader = contextService.getHeaderFromRequestContext(
                    HeaderName.AUTHORIZATION); // parse jwt token from request header
            if (authHeader == null||!authHeader.startsWith("Bearer ")) {
                SecurityContextHolder.getContext()
                        .setAuthentication(null);
                filterChain.doFilter(request, response);
                return;
            }
            String encrypted_auth_token = jwtUtils.parseToken(authHeader, TokenType.BEARER);
            String authToken = aesUtils.decrypt(encrypted_auth_token);

            List<String> authorities = jwtUtils.getAuthoritiesFromJwt(authToken);
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    null, null, authorities.stream()
                    .map(
                            SimpleGrantedAuthority::new
                    )
                    .toList());

            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext()
                    .setAuthentication(authentication);
        } catch (Exception e) {
            throw new TokenDecryptionException(e.getMessage());
        }
        filterChain.doFilter(request, response);
    }
}
