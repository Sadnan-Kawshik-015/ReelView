package com.example.demo.utils.securityUtils;

import com.example.demo.utils.enums.JwtClaimName;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.xml.bind.DatatypeConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class JwtUtils {
    private final String _jwtSecret;

    public JwtUtils(@Value("${app.jwtSecret}") final String _jwtSecret) {
        this._jwtSecret = _jwtSecret;
    }

    public String getClaimFromJwt(String jwt, JwtClaimName claimName) {
        return jwt == null ? null : this.parseClaimsFromJwt(jwt).get(claimName.getValue()).toString();
    }

    public String getFullNameFromJwt(String jwt) {
        if (jwt == null) {
            return null;
        } else {
            String var10000 = this.getClaimFromJwt(jwt, JwtClaimName.FIRST_NAME);
            return var10000 + " " + this.getClaimFromJwt(jwt, JwtClaimName.LAST_NAME);
        }
    }

    private Claims parseClaimsFromJwt(String jwt) {
        return (Claims) Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(this._jwtSecret)).parseClaimsJws(jwt).getBody();
    }

    public List<String> getAuthoritiesFromJwt(String jwt) {
        return (List)this.parseClaimsFromJwt(jwt).get(JwtClaimName.AUTHORITIES.getValue());
    }

    public boolean isJwtTokenValid(String authToken) {
        Jwts.parser().setSigningKey(this._jwtSecret).parseClaimsJws(authToken);
        return true;
    }

    public Date getExpiryFromJwt(String jwt) {
        return ((Claims)Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(this._jwtSecret)).parseClaimsJws(jwt).getBody()).getExpiration();
    }

    public String parseToken(String header, String tokenType) {
        if (header != null && header.length() > 5) {
            String prefix = header.substring(0, 6);
            return prefix.equalsIgnoreCase(tokenType) ? header.substring(7) : null;
        } else {
            return null;
        }
    }
}
