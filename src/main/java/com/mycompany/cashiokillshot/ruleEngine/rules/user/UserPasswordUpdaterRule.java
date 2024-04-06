package com.mycompany.cashiokillshot.ruleEngine.rules.user;

import com.mycompany.cashiokillshot.enums.RequestTypes;
import com.mycompany.cashiokillshot.enums.ResultIds;
import com.mycompany.cashiokillshot.records.ServiceResponder;
import com.mycompany.cashiokillshot.ruleEngine.interfaces.ServiceRule;
import com.mycompany.cashiokillshot.ruleEngine.service.UserService;
import com.mycompany.cashiokillshot.utility.Constants;
import com.mycompany.cashiokillshot.utility.Util;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.json.JSONObject;

import javax.swing.plaf.synth.SynthTableUI;

@ApplicationScoped
public class UserPasswordUpdaterRule implements ServiceRule {
    @Inject
    UserService userService;
    @Override
    public boolean matches(Object input) {
        return (input.toString().equalsIgnoreCase(RequestTypes.UPDATE_USER_PASSWORD.name()));

    }

    @Override
    public JSONObject apply(Object input) {
        JSONObject request=new JSONObject(input.toString());
        ServiceResponder response=userService.updateUserPassword(request);

        return (response.isSuccess())
                ? Util.buildResponse(Constants.SUCCESS_STATUS_CODE, ResultIds.PASSWORD_UPDATED.name(), response.message())
                : Util.buildResponse(Constants.FAILURE_STATUS_CODE, ResultIds.REQUEST_FAILED.name(), response.message());
    }
}