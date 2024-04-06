package com.mycompany.cashiokillshot.ruleEngine.rules.Employee;

import com.mycompany.cashiokillshot.enums.RequestTypes;
import com.mycompany.cashiokillshot.models.Employee;
import com.mycompany.cashiokillshot.models.Firm;
import com.mycompany.cashiokillshot.ruleEngine.interfaces.ServiceRule;
import com.mycompany.cashiokillshot.ruleEngine.service.EmployeeService;
import com.mycompany.cashiokillshot.ruleEngine.service.FirmService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.json.JSONObject;

//@ApplicationScoped
//public class EmployeePollerRule implements ServiceRule {
//    @Inject
//    EmployeeService employeeService;
//    @Inject
//    FirmService firmService;
//    @Override
//    public boolean matches(Object input) {
//        return (input.toString().equalsIgnoreCase(RequestTypes.GET_EMPLOYEE.name()));
//    }
//
//    @Override
//    public JSONObject apply(Object input) {
//        JSONObject object = new JSONObject(input.toString());
//
//        Employee employee= (Employee) employeeService.getEmployee(object).message();
//        JSONObject firmDetails= (JSONObject) firmService.getFirmDetails(employee.getFirmId()).message();
//        Firm firm=new Firm(firmDetails);
//    }
//}
