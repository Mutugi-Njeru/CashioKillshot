package com.mycompany.cashiokillshot.ruleEngine.service;

import com.mycompany.cashiokillshot.dao.AdvanceRequestsDao;
import com.mycompany.cashiokillshot.dao.EmployeesDao;
import com.mycompany.cashiokillshot.models.AdvanceRequest;
import com.mycompany.cashiokillshot.models.Employee;
import com.mycompany.cashiokillshot.records.ServiceResponder;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@ApplicationScoped
public class AdvanceService {
    @Inject
    AdvanceRequestsDao advanceRequestsDao;
    @Inject
    EmployeesDao employeesDao;

    private static final Logger logger= LogManager.getLogger(AdvanceService.class);

//    public ServiceResponder handleAdvanceRequest(AdvanceRequest request){
//        Employee employee=employeesDao.getEmployee(request.getEmployeeId(), request.getMsisdn());
//        if (employee.isActive()){
//            boolean hasUnprocessedAdvanceRequest=advanceRequestsDao.hasUnapprovedAdvanceRequest(employee.getEmployeeId());
//
//            if (!hasUnprocessedAdvanceRequest){
//                int requestId= advanceRequestsDao.saveAdvanceRequest(request);
//
//            }
//        }
//
//
//    }
}
