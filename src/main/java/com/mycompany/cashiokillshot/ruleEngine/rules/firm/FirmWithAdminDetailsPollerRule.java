package com.mycompany.cashiokillshot.ruleEngine.rules.firm;

import com.mycompany.cashiokillshot.enums.RequestTypes;
import com.mycompany.cashiokillshot.enums.ResultIds;
import com.mycompany.cashiokillshot.records.ServiceResponder;
import com.mycompany.cashiokillshot.ruleEngine.interfaces.ServiceRule;
import com.mycompany.cashiokillshot.ruleEngine.service.FirmService;
import com.mycompany.cashiokillshot.utility.Constants;
import com.mycompany.cashiokillshot.utility.Util;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.json.JSONObject;

@ApplicationScoped
public class FirmWithAdminDetailsPollerRule implements ServiceRule {
    @Inject
    FirmService firmService;
    @Override
    public boolean matches(Object input) {
        return (input.toString().equalsIgnoreCase(RequestTypes.GET_FIRMS_WITH_ADMIN.name()));

    }

    @Override
    public JSONObject apply(Object input) {
        JSONObject request=new JSONObject(input.toString());

        ServiceResponder response=firmService.getFirmWithAdminDetails(request);
        return (response.isSuccess())
                ? Util.buildResponse(Constants.SUCCESS_STATUS_CODE, ResultIds.REQUEST_SUCCESSFUL.name(), response.message())
                : Util.buildResponse(Constants.FAILURE_STATUS_CODE, ResultIds.REGISTRATION_FAILED.name(), response.message());
    }
}
