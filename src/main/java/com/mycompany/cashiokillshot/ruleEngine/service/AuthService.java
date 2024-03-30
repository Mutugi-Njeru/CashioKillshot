package com.mycompany.cashiokillshot.ruleEngine.service;

import com.mycompany.cashiokillshot.cipher.Sha256Hasher;
import com.mycompany.cashiokillshot.dao.AuthenticationDao;
import com.mycompany.cashiokillshot.records.Authentication;
import com.mycompany.cashiokillshot.records.ServiceResponder;
import com.mycompany.cashiokillshot.utility.Constants;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import java.util.Base64;

@ApplicationScoped
public class AuthService {
    @Inject
    AuthenticationDao authenticationDao;
    @Inject
    JwtService jwtService;

    private  static final Logger logger= LogManager.getLogger(AuthService.class);

    public ServiceResponder authenticateUser(JSONObject object){
        String username= object.optString(Constants.USERNAME_KEY, Constants.EMPTY_STRING);
        String password= object.optString(Constants.PASSWORD_KEY, Constants.EMPTY_STRING);
        //to add hashed password

        Authentication authentication=authenticationDao.authenticateUser(username, password);

        if (authentication.isAuthenticated()){
            String accessToken=generateAccessToken(authentication);

            JSONObject response=new JSONObject()
                    .put("authentication", new JSONObject()
                            .put(Constants.ACCESS_TOKEN_KEY, accessToken)
                            .put(Constants.TYPE_KEY, Constants.ACCESS_TOKEN_KEY)
                            .put(Constants.EXPIRES_IN_KEY, Constants.TOKEN_EXPIRY_TIME_IN_SECONDS));
            return new ServiceResponder(true, response);
        }
        else {
            return new ServiceResponder(false, "Sorry wrong username or password");
        }
    }

    private String generateAccessToken(Authentication authentication){
        String accessToken= Base64.getEncoder().encodeToString(jwtService.generateAccessToken(authentication).getBytes());

        return accessToken;
    }
}
