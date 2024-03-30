package com.mycompany.cashiokillshot.ruleEngine.rules.firm;

import com.mycompany.cashiokillshot.enums.RequestTypes;
import com.mycompany.cashiokillshot.enums.ResultIds;
import com.mycompany.cashiokillshot.models.FirmSignupRequest;
import com.mycompany.cashiokillshot.records.BeanValidator;
import com.mycompany.cashiokillshot.records.ServiceResponder;
import com.mycompany.cashiokillshot.ruleEngine.interfaces.ServiceRule;
import com.mycompany.cashiokillshot.ruleEngine.service.BeanValidatorService;
import com.mycompany.cashiokillshot.ruleEngine.service.FirmService;
import com.mycompany.cashiokillshot.utility.Constants;
import com.mycompany.cashiokillshot.utility.Util;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.json.JSONObject;

@ApplicationScoped
public class FirmSignupRule implements ServiceRule {
    @Inject
    FirmService firmService;
    @Inject
    BeanValidatorService beanValidator;


    @Override
    public boolean matches(Object input) {
        return (input.toString().equalsIgnoreCase(RequestTypes.REGISTER_FIRM.name()));

    }

    @Override
    public JSONObject apply(Object input) {
        JSONObject request=new JSONObject(input.toString());
        FirmSignupRequest firmSignupModel=new FirmSignupRequest(request);
        BeanValidator validate=beanValidator.validate(firmSignupModel);

        if (validate.isValid()){
            ServiceResponder response=firmService.registerFirm(firmSignupModel);
            return (response.isSuccess())
                    ?Util.buildResponse(Constants.SUCCESS_STATUS_CODE, ResultIds.REGISTRATION_SUCCESSFUL.name(), response.message())
                    :Util.buildResponse(Constants.FAILURE_STATUS_CODE, ResultIds.REGISTRATION_FAILED.name(), response.message());
        }
        else {
            return Util.buildResponse(Constants.FAILURE_STATUS_CODE, ResultIds.VALIDATION_FAILED.name(), validate.violations());
        }

    }
}
