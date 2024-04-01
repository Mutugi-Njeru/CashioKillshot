package com.mycompany.cashiokillshot.ruleEngine.engine;

import com.mycompany.cashiokillshot.enums.RequestTypes;
import com.mycompany.cashiokillshot.enums.ResultIds;
import com.mycompany.cashiokillshot.records.Token;
import com.mycompany.cashiokillshot.ruleEngine.interfaces.ServiceRule;
import com.mycompany.cashiokillshot.ruleEngine.service.JwtService;
import com.mycompany.cashiokillshot.utility.Constants;
import com.mycompany.cashiokillshot.utility.Util;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import org.json.JSONObject;

@ApplicationScoped
public class Engine {

    @Inject
    JwtService jwtService;
    @Inject
    private Instance<ServiceRule>rules;

    public JSONObject init(JSONObject request, String requestType, String bearerToken ){

        Token tokenDetails=authorizeUser(requestType,bearerToken);

        return (tokenDetails.isValid())
                ? start(request, requestType)
                :Util.buildResponse(Constants.FAILURE_STATUS_CODE, ResultIds.REQUEST_FAILED.name(), "Invalid token");
    }
    private Token authorizeUser(String requestType, String bearerToken){
        String token=(bearerToken.startsWith("Bearer"))
                ?bearerToken.replace("Bearer ", "").trim()
                :bearerToken;

        return (requestType.equalsIgnoreCase(RequestTypes.AUTHENTICATE.name()))
                ?new Token(true, "")
                :jwtService.decodeAccessToken(token);

    }


    private JSONObject start(JSONObject request, String requestType){
        JSONObject response=new JSONObject();
        boolean isValid=false;

        for (ServiceRule rule: rules){
            if (rule.matches(requestType)){
                isValid=true;
                response=rule.apply(request);
                break;
            }
        }
        return (isValid)
                ?response
                :Util.buildResponse(Constants.FAILURE_STATUS_CODE, ResultIds.REQUEST_FAILED.name(), "unknown request type");
    }

}
