package com.example.util;

import com.example.dto.JwtDTO;
import com.example.enums.ProfileRole;
import com.example.exp.AppForbiddenException;
import com.example.exp.TokenNotValidException;
import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;

import java.util.Date;
//import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties
public class JwtUtil {
    public static final String secretKey="topSecretKey2000";

    public static String encode(Integer profileId, ProfileRole role){
        JwtBuilder jwtbuilder = Jwts.builder();
        jwtbuilder.setIssuedAt(new Date());
        jwtbuilder.signWith(SignatureAlgorithm.HS256,secretKey);

        jwtbuilder.claim("id",profileId);
        jwtbuilder.claim("role",role);

        int TokenLiveTime=10000*360*24;
        jwtbuilder.setExpiration(new Date(System.currentTimeMillis()+(TokenLiveTime)));
        jwtbuilder.setIssuer("Gulom");

        return jwtbuilder.compact();
    }
    public static String encode(Integer profileId) {
        JwtBuilder jwtBuilder = Jwts.builder();
        jwtBuilder.setIssuedAt(new Date());
        jwtBuilder.signWith(SignatureAlgorithm.HS512, secretKey);

        jwtBuilder.claim("id", profileId);
        int tokenLiveTime = 1000 * 3600 * 1;
        jwtBuilder.setExpiration(new Date(System.currentTimeMillis() + (tokenLiveTime)));
        jwtBuilder.setIssuer("Mazgi");

        return jwtBuilder.compact();
    }
    public static Integer decodeForEmailVerification(String token) {
        JwtParser jwtParser = Jwts.parser();
        jwtParser.setSigningKey(secretKey);

        Jws<Claims> jws = jwtParser.parseClaimsJws(token);

        Claims claims = jws.getBody();

        Integer id = (Integer) claims.get("id");
        return id;
    }
//    public static JwtDTO decode(String token){
//        JwtParser jwtParser=Jwts.parser();
//        jwtParser.setSigningKey(secretKey);
//        Jws<Claims> jws=jwtParser.parseClaimsJws(token);
//        Claims claims=jws.getBody();
//        Integer phone= (String) claims.get("id");
//        String role=(String)claims.get("role");
//        ProfileRole profileRole=ProfileRole.valueOf(role);
//        return new JwtDTO(phone,profileRole);
//
//    }
    public static Integer getIdFromHeader(HttpServletRequest request) {
        try {
            return (Integer) request.getAttribute("id");
        } catch (RuntimeException e) {
            throw new TokenNotValidException("Not Authorized");
        }
    }
    public static Integer getIdFromHeader(HttpServletRequest request,ProfileRole role){
        Integer pId= (Integer) request.getAttribute("id");
        ProfileRole jwtRole= (ProfileRole) request.getAttribute("role");

        if (!role.equals(jwtRole)) {
            throw new AppForbiddenException("method not allowed");
        }
        return pId;
        }
    }


