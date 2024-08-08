package com.airbnb.service;

import com.airbnb.entity.AppUser;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Date;

@Service
public class JWTService {

    @Value("${jwt.algorithm.key}")
    private String algorithmKey;

    @Value("${jwt.issuer}")
    private String issuer;

    @Value("${jwt.expiration}")
    private long expiration;

    private static final String USER_NAME="user";

    private Algorithm algorithm;


    @PostConstruct
    public void postConstruct() throws UnsupportedEncodingException {
        algorithm = Algorithm.HMAC256(algorithmKey);
    }

    public String generateToken(AppUser user){
        return JWT.create().
                withClaim(USER_NAME,user.getUsername()).
                withExpiresAt(new Date(System.currentTimeMillis()+expiration)).
                withIssuer(issuer).
                sign(algorithm);
    }

    public String getUserName(String token){
        //jockey rockey is builder vikram
        DecodedJWT decodedJWT = JWT.require(algorithm).withIssuer(issuer).build().verify(token);
        return decodedJWT.getClaim(USER_NAME).asString();
    }
}
