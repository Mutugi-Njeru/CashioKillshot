package com.mycompany.cashiokillshot.dao;

import com.mycompany.cashiokillshot.database.MysqlConnector;
import com.mycompany.cashiokillshot.models.AdvanceRequest;
import com.mycompany.cashiokillshot.utility.Constants;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;


@ApplicationScoped
public class AdvanceRequestsDao {
    @Inject
    MysqlConnector mysqlConnector;

    private static final Logger logger= LogManager.getLogger(AdvanceRequestsDao.class);

    public boolean hasUnapprovedAdvanceRequest(int employeeId){
        boolean status=false;
        String query="SELECT COUNT(advance_request_id) FROM advance_requests WHERE employee_Id =? AND request_status = 'pending' AND DATE(created_at) = CURDATE()";

        try(Connection connection= mysqlConnector.getConnection(); PreparedStatement preparedStatement= connection.prepareStatement(query)) {
            preparedStatement.setInt(1, employeeId);
            ResultSet resultSet=preparedStatement.executeQuery();

            while(resultSet.next()){
                status=resultSet.getInt(1)==1;
            }

        } catch (SQLException ex) {
            logger.error(Constants.ERROR_LOG_TEMPLATE, Constants.ERROR, ex.getClass().getSimpleName(), ex.getMessage());
            return status;
        }
        return status;
    }

    public int saveAdvanceRequest(AdvanceRequest request){
        String query="INSERT INTO advance_requests(firm_deduction_plan_id, employee_id, amount_applied, channel) VALUES (?,?,?,?)";
        int id=0;

        try (Connection connection= mysqlConnector.getConnection(); PreparedStatement preparedStatement= connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)){
            preparedStatement.setInt(1, request.getFirmDeductionPlanId());
            preparedStatement.setInt(2, request.getEmployeeId());
            preparedStatement.setInt(3, request.getAmount());
            preparedStatement.setString(4, request.getChannel());
            preparedStatement.executeUpdate();

            ResultSet resultSet= preparedStatement.getGeneratedKeys();
            while (resultSet.next()){
                id=resultSet.getInt(1);
            }

        } catch (SQLException ex) {
            logger.error(Constants.ERROR_LOG_TEMPLATE, Constants.ERROR, ex.getClass().getSimpleName(), ex.getMessage());
            return id;
        }
        return id;
    }
}
