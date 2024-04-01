package com.mycompany.cashiokillshot.ruleEngine.rules.user;

import com.mycompany.cashiokillshot.enums.RequestTypes;
import com.mycompany.cashiokillshot.enums.ResultIds;
import com.mycompany.cashiokillshot.models.UserSignupRequest;
import com.mycompany.cashiokillshot.records.BeanValidator;
import com.mycompany.cashiokillshot.records.ServiceResponder;
import com.mycompany.cashiokillshot.ruleEngine.interfaces.ServiceRule;
import com.mycompany.cashiokillshot.ruleEngine.service.BeanValidatorService;
import com.mycompany.cashiokillshot.ruleEngine.service.UserService;
import com.mycompany.cashiokillshot.utility.Constants;
import com.mycompany.cashiokillshot.utility.Util;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.json.JSONObject;

@ApplicationScoped
public class UserCreatorRule implements ServiceRule {

    @Inject
    UserService userService;
    @Inject
    BeanValidatorService beanValidatorService;
    @Override
    public boolean matches(Object input) {
        return (input.toString().equalsIgnoreCase(RequestTypes.CREATE_USER.name()));
    }

    @Override
    public JSONObject apply(Object input) {
        JSONObject request=new JSONObject(input.toString());

        UserSignupRequest user=new UserSignupRequest(request);
        BeanValidator validator=beanValidatorService.validate(user);

        if (validator.isValid()){
            ServiceResponder response=userService.createUser(user);
            return (response.isSuccess())
                    ? Util.buildResponse(Constants.SUCCESS_STATUS_CODE, ResultIds.REGISTRATION_SUCCESSFUL.name(),response.message())
                    :Util.buildResponse(Constants.FAILURE_STATUS_CODE, ResultIds.REGISTRATION_FAILED.name(), response.message());
        }
        else {
            return Util.buildResponse(Constants.FAILURE_STATUS_CODE, ResultIds.VALIDATION_FAILED.name(), validator.isValid());
        }
    }
}
