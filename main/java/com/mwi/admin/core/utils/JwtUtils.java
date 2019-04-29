package com.mwi.admin.core.utils;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Date;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * JWT tool util class
 *
 * @Author Eric
 * @Date 4/17/2019
 **/
@Component
@Slf4j
@Getter
@Setter
@ConfigurationProperties(prefix = "mwi.jwt")
public class JwtUtils {

    private String secret;

    private long expire;

    private String header;

    /**
     * generate a token
     *
     * @param username
     * @return
     */
    public String generateToken(String username) {
        Date nowDate = new Date();
        Date expireDate = new Date(nowDate.getTime() + expire * 1000);
        return Jwts.builder().setHeaderParam("ype", "JWT").setSubject(username).setIssuedAt(nowDate).setExpiration(expireDate).signWith(SignatureAlgorithm.HS512, secret).compact();
    }

    /**
     * get payload from token
     *
     * @param token
     * @return
     */
    public Claims getClaimByToken(String token) {
        try {
            return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        } catch (Exception e) {
            log.debug("validate is token error ", e);
            return null;
        }
    }

    /**
     * get username from token
     *
     * @param token
     * @return
     */
    public String getUsernameFromToken(String token) {
        return getClaimByToken(token).getSubject();
    }

    /**
     * get create date from token
     *
     * @param token
     * @return
     */
    public Date getIssuedAtDateFromToken(String token) {
        return getClaimByToken(token).getIssuedAt();
    }

    /**
     * get expire date from token
     *
     * @param token
     * @return
     */
    public Date getExpirationDateFromToken(String token) {
        return getClaimByToken(token).getExpiration();
    }

    /**
     * check token is expired
     *
     * @param token
     * @return
     */
    public Boolean isTokenExpired(String token) {
        try {
            Date expiration = getExpirationDateFromToken(token);
            return expiration.before(new Date());
        } catch (ExpiredJwtException expiredJwtException) {
            return true;
        }
    }

}
