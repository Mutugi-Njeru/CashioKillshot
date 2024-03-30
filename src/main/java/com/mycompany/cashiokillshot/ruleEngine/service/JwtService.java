package com.mycompany.cashiokillshot.ruleEngine.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.mycompany.cashiokillshot.records.Authentication;
import com.mycompany.cashiokillshot.records.Token;
import com.mycompany.cashiokillshot.utility.Constants;
import com.mycompany.cashiokillshot.utility.Util;
import jakarta.enterprise.context.ApplicationScoped;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;

import java.util.Base64;
import java.util.Date;

@ApplicationScoped
public class JwtService {

    private static final Logger logger= LogManager.getLogger(JwtService.class);

    public String generateAccessToken(Authentication authentication){
        long milliseconds=(new Date().getTime()) + (Constants.TOKEN_EXPIRY_TIME_IN_SECONDS * 1000);
        Date expireDate=new Date(milliseconds);

        return JWT.create()
                .withClaim("createdAt", Util.getTimestamp())
                .withClaim("username", authentication.username())
                .withIssuer("cashio-killshot")
                .withClaim("expiresIn", Constants.TOKEN_EXPIRY_TIME_IN_SECONDS)
                .withExpiresAt(expireDate)
                .sign(Constants.JWT_ALGORITHM);
    }

    public Token decodeAccessToken(String accessToken){
        try {
            byte[] decodedBytes= Base64.getDecoder().decode(accessToken.getBytes());
            accessToken=new String(decodedBytes);

            JWTVerifier verifier=JWT.require(Constants.JWT_ALGORITHM)
                    .withIssuer("cashio-killshot")
                    .acceptExpiresAt(Constants.TOKEN_EXPIRY_TIME_IN_SECONDS)
                    .build();

            DecodedJWT decodedJWT= verifier.verify(accessToken);
            String username=decodedJWT.getClaims().get("username").asString();
            return  new Token(true, username);
        }
        catch (JWTVerificationException | IllegalArgumentException | JSONException ex){
            logger.error("ERROR=> {} | {}", ex.getClass().getSimpleName(), ex.getMessage());
            return new Token(false, "");
        }
    }
}
