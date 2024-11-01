package com.project.shopiibackend.domain.utility;


import com.project.shopiibackend.constant.TokenConstant;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.util.StringUtils;

import java.util.Date;

public class TokenUtility {

    private static final Logger LOGGER = LoggerFactory.getLogger(TokenUtility.class);

    public static String generateToken(Authentication authentication) {
        LOGGER.info("TokenUtility -> generateToken invoked");
        Date createdDate = new Date();
        Date expiryDate = new Date(createdDate.getTime() + TokenConstant.JWT_EXPIRATION_IN_MS);
        String token = Jwts.builder()
                .setSubject(authentication.getName())
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, TokenConstant.JWT_SECRET_KEY)
                .compact();
        return token;
    }

//    public static String getEmailFromJwt(String jwtString) {
//        LOGGER.debug("TokenUtility -> getUsernameFromJwt invoked!");
//        Claims claims = Jwts.parser()
//                .setSigningKey(TokenConstant.JWT_SECRET_KEY)
//                .parseClaimsJws(jwtString)
//                .getBody();
//        return claims.getSubject();
//    }

    public static String getEmailFromJwt(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(TokenConstant.JWT_SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();

        return claims.get("email", String.class); // Trích xuất email từ claims
    }


    public static String getTokenFromRequest(HttpServletRequest request) {
        LOGGER.debug("TokenUtility -> getTokenFromRequest invoked!");
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("bearer ")) {
            return bearerToken.substring(TokenConstant.STARTING_LETTER_IN_BEARER_TOKEN);
        }
        return null;
    }
}
