package org.sayar.net.Security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.sayar.net.Security.Model.JwtUser;
import org.sayar.net.Security.Service.JwtUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import java.util.Date;

@Component
public class JwtTokenUtil {

    private static String signingKey = "dfffffffffffffffffffffffffffffffffffffffwdqwdqwdqwfdqewdqwedweqdqewdqwedqwdqwdqwdqwdqwdqwdqwdfffffffffffffffffffgsrggggggggggggg";

    @Autowired
    private static JwtUserService service;
    private static final String USER_NAME_KEY = "username";
    private static final String USER_ID = "userId";
    private static final String PASSWORD = "password";
    private static final String ORGAN_ID = "organId";
    private static final String USER_TYPE = "userTypeId";
    private static final long TWO_DAYS_MILLIS = 3600 * 48 * 10000000;

    public static String getUserNameFromToken(String token) {
        final Claims claims = getTokenClaimes(token);
        return (String) claims.getSubject();
    }

    public static int getOrganIdFromToken(String token) {
        final Claims claims = getTokenClaimes(token);
        return (int) claims.get(ORGAN_ID);
    }

//    public static int getOrganIdFromToken(String token) {
//        final Claims claims = getTokenClaimes(token);
//        return (int) claims.get(ORGAN_ID);
//    }

    private static Claims getTokenClaimes(String token) {

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims;
    }


    public static String generateToken(JwtUser user) {
        Claims claims = Jwts.claims()
                .setSubject(user.getUsername());
        claims.put(PASSWORD, user.getPassword());
        claims.put(USER_ID, user.getUserId());
        claims.put(USER_TYPE, user.getUserTypeId());
        String s = Jwts.builder()
                .setClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + TWO_DAYS_MILLIS))
                .signWith(SignatureAlgorithm.HS512, signingKey).compact();
        return s;
    }

    private static Date getExpirationDateFromToken(String token) {
        Date expiration;
        try {
            final Claims claims = getTokenClaimes(token);
            expiration = claims.getExpiration();
        } catch (Exception e) {
            expiration = null;
        }
        return expiration;
    }

    public static Boolean validateToken(String token, UserDetails userDetails) {

        JwtUser user = (JwtUser) userDetails;
        final String username = getUserNameFromToken(token);
        final Date expiration = getExpirationDateFromToken(token);

        return (
                username.equals(user.getUsername())
                        && !isTokenExpired(token));
    }

    private static Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public static String refreshToken(String token) {
        String refreshedToken;
        try {
            final Claims claims = getTokenClaimes(token);
            String username = claims.getSubject();
            JwtUser user = (JwtUser) service.loadUserByUsername(username);
            refreshedToken = generateToken(user);
        } catch (Exception e) {
            refreshedToken = null;
        }
        return refreshedToken;
    }
}
