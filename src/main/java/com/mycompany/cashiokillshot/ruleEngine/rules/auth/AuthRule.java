package com.mycompany.cashiokillshot.ruleEngine.rules.auth;

import com.mycompany.cashiokillshot.enums.RequestTypes;
import com.mycompany.cashiokillshot.enums.ResultIds;
import com.mycompany.cashiokillshot.records.ServiceResponder;
import com.mycompany.cashiokillshot.ruleEngine.interfaces.ServiceRule;
import com.mycompany.cashiokillshot.ruleEngine.service.AuthService;
import com.mycompany.cashiokillshot.utility.Constants;
import com.mycompany.cashiokillshot.utility.Util;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.json.JSONObject;

@ApplicationScoped
public class AuthRule implements ServiceRule {

    @Inject
    AuthService authService;
    @Override
    public boolean matches(Object input) {
        return (input.toString().equalsIgnoreCase(RequestTypes.AUTHENTICATE.name()));
    }

    @Override
    public JSONObject apply(Object input) {

        JSONObject object=new JSONObject(input.toString());
        ServiceResponder response=authService.authenticateUser(object);

        return (response.isSuccess())
                ? Util.buildResponse(Constants.SUCCESS_STATUS_CODE, ResultIds.AUTHENTICATION_SUCCESSFUL.name(), response.message())
                :Util.buildResponse(Constants.FAILURE_STATUS_CODE, ResultIds.AUTHENTICATION_FAILED.name(), response.message());
    }
}
