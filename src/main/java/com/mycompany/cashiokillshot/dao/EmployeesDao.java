package com.mycompany.cashiokillshot.dao;

import com.mycompany.cashiokillshot.database.MysqlConnector;
import com.mycompany.cashiokillshot.models.Employee;
import com.mycompany.cashiokillshot.models.EmployeeSignupRequest;
import com.mycompany.cashiokillshot.models.Firm;
import com.mycompany.cashiokillshot.utility.Constants;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import java.sql.*;

@ApplicationScoped
public class EmployeesDao {
    @Inject
    MysqlConnector mysqlConnector;

    private static final Logger logger= LogManager.getLogger(EmployeesDao.class);
    private static final String SELECT_EMPLOYEE="SELECT employee_id, firm_id, first_name, last_name, msisdn, id_number, advance_limit, is_active,\n" +
            "IF(terms ='accepted', true, false) AS hasAcceptedTermsAndConditions FROM employees ";

    public int registerEmployee(EmployeeSignupRequest employeeSignupRequest, Firm firm){
        String query="INSERT IGNORE INTO employees(firm_id, first_name, last_name, msisdn, id_number) VALUES (?,?,?,?,?)";
        int employeeId = 0;

        try(Connection connection= mysqlConnector.getConnection(); PreparedStatement preparedStatement=connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setInt(1, firm.getFirmId());
            preparedStatement.setString(2, employeeSignupRequest.getFirstName());
            preparedStatement.setString(3, employeeSignupRequest.getLastName());
            preparedStatement.setString(4, employeeSignupRequest.getMsisdn());
            preparedStatement.setInt(5, employeeSignupRequest.getIdNumber());
            preparedStatement.executeUpdate();

            ResultSet resultSet=preparedStatement.getGeneratedKeys();
            while (resultSet.next()){
                employeeId=resultSet.getInt(1);

            }
        } catch (SQLException ex) {
            logger.error(Constants.ERROR_LOG_TEMPLATE, Constants.ERROR, ex.getClass().getSimpleName(), ex.getMessage());
            return employeeId;
        }
        return employeeId;
    }

    public Employee getEmployee(int employeeId, String msisdn){
        String query=SELECT_EMPLOYEE + "WHERE employee_id =? AND msisdn =?";
        JSONObject object=new JSONObject();

        try(Connection connection= mysqlConnector.getConnection(); PreparedStatement preparedStatement=connection.prepareStatement(query)) {
            preparedStatement.setInt(1, employeeId);
            preparedStatement.setString(2, msisdn);
            ResultSet resultSet=preparedStatement.executeQuery();

            while (resultSet.next()){
                object.put("employeeId", resultSet.getInt(1))
                        .put("firmId", resultSet.getInt(2))
                        .put("firstName", resultSet.getString(3))
                        .put("lastName", resultSet.getString(4))
                        .put("msisdn", resultSet.getString(5))
                        .put("idNumber", resultSet.getString(6))
                        .put("advanceLimit", resultSet.getInt(7))
                        .put("isActive", resultSet.getBoolean(8))
                        .put("hasAcceptedTermsAndConditions", resultSet.getBoolean(9));
            }

        } catch (SQLException ex) {
            logger.error(Constants.ERROR_LOG_TEMPLATE, Constants.ERROR, ex.getClass().getSimpleName(), ex.getMessage());
            return new Employee(object);
        }
        return new Employee(object);
    }
}
