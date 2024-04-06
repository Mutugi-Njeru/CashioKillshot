package com.mycompany.cashiokillshot.ruleEngine.rules.Employee;

import com.mycompany.cashiokillshot.enums.RequestTypes;
import com.mycompany.cashiokillshot.enums.ResultIds;
import com.mycompany.cashiokillshot.models.EmployeeSignupRequest;
import com.mycompany.cashiokillshot.records.BeanValidator;
import com.mycompany.cashiokillshot.records.ServiceResponder;
import com.mycompany.cashiokillshot.ruleEngine.interfaces.ServiceRule;
import com.mycompany.cashiokillshot.ruleEngine.service.BeanValidatorService;
import com.mycompany.cashiokillshot.ruleEngine.service.EmployeeService;
import com.mycompany.cashiokillshot.utility.Constants;
import com.mycompany.cashiokillshot.utility.Util;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.json.JSONObject;

@ApplicationScoped
public class EmployeeSignupRule implements ServiceRule {
    @Inject
    EmployeeService employeeService;
    @Inject
    BeanValidatorService beanValidatorService;
    @Override
    public boolean matches(Object input) {
        return (input.toString().equalsIgnoreCase(RequestTypes.REGISTER_EMPLOYEE.name()));
    }

    @Override
    public JSONObject apply(Object input) {
        JSONObject request=new JSONObject(input.toString());
        EmployeeSignupRequest employeeSignupRequest=new EmployeeSignupRequest(request);
        BeanValidator validate=beanValidatorService.validate(employeeSignupRequest);

        if (validate.isValid()){
            ServiceResponder response=employeeService.registerEmployee(employeeSignupRequest);

            return (response.isSuccess())
                    ? Util.buildResponse(Constants.SUCCESS_STATUS_CODE, ResultIds.REGISTRATION_SUCCESSFUL.name(), response.message())
                    :Util.buildResponse(Constants.FAILURE_STATUS_CODE, ResultIds.REGISTRATION_FAILED.name(), response.message());
        }
        else {
            return Util.buildResponse(Constants.FAILURE_STATUS_CODE, ResultIds.VALIDATION_FAILED.name(), false);
        }
    }
}
