package com.mycompany.cashiokillshot.ruleEngine.service;

import com.mycompany.cashiokillshot.dao.EmployeesDao;
import com.mycompany.cashiokillshot.dao.FirmsDao;
import com.mycompany.cashiokillshot.models.Employee;
import com.mycompany.cashiokillshot.models.EmployeeSignupRequest;
import com.mycompany.cashiokillshot.models.Firm;
import com.mycompany.cashiokillshot.records.ServiceResponder;
import com.mycompany.cashiokillshot.utility.Constants;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

@ApplicationScoped
public class EmployeeService {
    @Inject
    EmployeesDao employeesDao;
    @Inject
    FirmsDao firmsDao;

    private static final Logger logger= LogManager.getLogger(EmployeeService.class);


    public ServiceResponder registerEmployee(EmployeeSignupRequest employeeSignupRequest){
        Firm firm=firmsDao.getFirm(employeeSignupRequest.getFirmId());
        if (firm.isValid()){
            int employeeId= employeesDao.registerEmployee(employeeSignupRequest, firm);
            return (employeeId>0)
                    ?new ServiceResponder(true, "Registration successful")
                    :new ServiceResponder(false, "Registration unsuccessful ");
        }
        else {
            return new ServiceResponder(false, "invalid employee");
        }
    }

    public ServiceResponder getEmployee(JSONObject object){
        String msisdn= object.optString("msisdn", Constants.EMPTY_STRING);
        Employee employee = employeesDao.getEmployee(0, msisdn);

        return new ServiceResponder(true, employee);

    }
}
