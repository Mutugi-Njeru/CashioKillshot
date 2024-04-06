package com.mycompany.cashiokillshot.dao;

import com.mycompany.cashiokillshot.database.MysqlConnector;
import com.mycompany.cashiokillshot.models.Firm;
import com.mycompany.cashiokillshot.models.FirmSignupRequest;
import com.mycompany.cashiokillshot.utility.Constants;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.*;

@ApplicationScoped
public class FirmsDao {

    @Inject
    MysqlConnector mysqlConnector;

    private final Logger logger= LogManager.getLogger(FirmsDao.class);

    public int registerFirm(FirmSignupRequest firmSignupRequest){
        int id=0;
        String query="INSERT INTO firms (business_name, business_nature, registration_pin, kra_pin, auto_dispatch_advance, advance_interest_rate) VALUES (?,?,?,?,?,?)";


        try(Connection connection= mysqlConnector.getConnection(); PreparedStatement preparedStatement= connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, firmSignupRequest.getBusinessname());
            preparedStatement.setString(2, firmSignupRequest.getBusinessNature());
            preparedStatement.setString(3, firmSignupRequest.getRegistrationPin());
            preparedStatement.setString(4, firmSignupRequest.getKraPin());
            preparedStatement.setString(5, String.valueOf(firmSignupRequest.isAutoDispatchAdvance()));
            preparedStatement.setDouble(6, firmSignupRequest.getAdvanceInterestRate());
            preparedStatement.executeUpdate();

            ResultSet resultSet= preparedStatement.getGeneratedKeys();
            while (resultSet.next()){
                id=resultSet.getInt(1);
            }

        } catch (SQLException ex) {
            logger.error(Constants.ERROR_LOG_TEMPLATE, Constants.ERROR, ex.getClass().getSimpleName(), ex.getMessage());
            return id;
        }
        return  id;
    }

    public boolean saveFirmContacts(int firmId, FirmSignupRequest firmSignupRequest){
        String query="INSERT INTO firms_contact(firm_id, location, email, primary_msisdn, secondary_msisdn, postal_address) VALUES (?,?,?,?,?,?)";
        boolean status=false;


        try (Connection connection= mysqlConnector.getConnection(); PreparedStatement preparedStatement=connection.prepareStatement(query)){
            preparedStatement.setInt(1, firmId);
            preparedStatement.setString(2, firmSignupRequest.getLocation());
            preparedStatement.setString(3, firmSignupRequest.getEmail());
            preparedStatement.setString(4, firmSignupRequest.getPrimaryMsisdn());
            preparedStatement.setString(5, firmSignupRequest.getSecondaryMsisdn());
            preparedStatement.setString(6, firmSignupRequest.getPostalAddress());
            status=preparedStatement.executeUpdate()>0;
        } catch (SQLException ex) {
            logger.error(Constants.ERROR_LOG_TEMPLATE, Constants.ERROR, ex.getClass().getSimpleName(), ex.getMessage());
            return status;
        }
        return  status;
    }

    public boolean createFirmDeductionPlan(int deductionPlanId, int firmId){
        boolean status=false;
        String query="INSERT INTO firm_deduction_plans(deduction_plan_id, firm_id) VALUES (?,?)";

        try(Connection connection= mysqlConnector.getConnection(); PreparedStatement preparedStatement= connection.prepareStatement(query)) {
            preparedStatement.setInt(1, deductionPlanId);
            preparedStatement.setInt(2, firmId);
            status=preparedStatement.executeUpdate()>0;
        } catch (SQLException ex) {
            logger.error(Constants.ERROR_LOG_TEMPLATE, Constants.ERROR, ex.getClass().getSimpleName(), ex.getMessage());
            return status;
        }
        return  status;
    }

    public Firm getFirm(int firmId){
        JSONObject object=new JSONObject();
        String query="SELECT f.firm_id, f.business_name, f.business_nature, f.registration_pin, f.kra_pin, f.auto_dispatch_advance, \n" +
                "f.advance_interest_rate, fc.location, fc.email, fc.primary_msisdn, fc.secondary_msisdn, fc.postal_address, f.is_active,\n" +
                "fdp.firm_deduction_plan_id, fdp.deduction_plan_id, fdp.firm_id, fdp.is_active\n" +
                "FROM firms f\n" +
                "INNER JOIN firms_contact fc ON f.firm_id=fc.firm_id\n" +
                "INNER JOIN firm_deduction_plans fdp ON fdp.firm_id=f.firm_id\n" +
                "WHERE f.firm_id=?";

        try (Connection connection= mysqlConnector.getConnection();PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, firmId);
            ResultSet resultSet=preparedStatement.executeQuery();

            while (resultSet.next()){
                object.put("firmId", resultSet.getInt(1))
                        .put("businessName", resultSet.getString(2))
                        .put("businessNature", resultSet.getString(3))
                        .put("registrationPin", resultSet.getString(4))
                        .put("kraPin", resultSet.getString(5))
                        .put("autoDispatchAdvance",resultSet.getString(6))
                        .put("salaryLoanInterest", resultSet.getInt(7))
                        .put("contacts", new JSONObject().put("location", resultSet.getString(8))
                                .put("email", resultSet.getString(9))
                                .put("primaryMsisdn", resultSet.getString(10))
                                .put("secondaryMsisdn", resultSet.getString(11))
                                .put("postalAddress", resultSet.getString(12)))
                        .put("isActive", resultSet.getString(13))
                        .put("firmDeductionPlanDetails", new JSONObject().put("firmDeductionPlanId", resultSet.getInt(14))
                                .put("deductionPlanId", resultSet.getInt(15))
                                .put("firmId", resultSet.getInt(16))
                                .put("isActive", resultSet.getString(17)));
            }

        } catch (SQLException ex) {
            logger.error(Constants.ERROR_LOG_TEMPLATE, Constants.ERROR, ex.getClass().getSimpleName(), ex.getMessage());
            return new Firm(object);
        }
        return new Firm(object);
    }

    public JSONObject getFirms(int lastRecordId){
        String query="SELECT f.firm_id, f.business_name, f.business_nature, f.registration_pin, f.kra_pin, f.auto_dispatch_advance, \n" +
                "f.advance_interest_rate, fc.location, fc.email, fc.primary_msisdn, fc.secondary_msisdn, fc.postal_address, f.is_active,\n" +
                "fdp.firm_deduction_plan_id, fdp.deduction_plan_id, fdp.firm_id, fdp.is_active\n" +
                "FROM firms f\n" +
                "INNER JOIN firms_contact fc ON f.firm_id=fc.firm_id\n" +
                "INNER JOIN firm_deduction_plans fdp ON fdp.firm_id=f.firm_id\n" +
                "WHERE f.firm_id>? LIMIT 100";
        JSONArray firms=new JSONArray();

        try(Connection connection= mysqlConnector.getConnection(); PreparedStatement preparedStatement= connection.prepareStatement(query)) {
            preparedStatement.setInt(1, lastRecordId);
            ResultSet resultSet= preparedStatement.executeQuery();

            while (resultSet.next()){
                firms.put(new JSONObject()
                        .put("firmId", resultSet.getInt(1))
                        .put("businessName", resultSet.getString(2))
                        .put("businessNature", resultSet.getString(3))
                        .put("registrationPin", resultSet.getString(4))
                        .put("kraPin", resultSet.getString(5))
                        .put("autoDispatchAdvance", resultSet.getString(6))
                        .put("salaryLoanInterestRate", resultSet.getString(7))
                        .put("contacts", new JSONObject().put("location", resultSet.getString(8))
                                .put("email", resultSet.getString(9))
                                .put("primaryMsisdn", resultSet.getString(10))
                                .put("secondaryMsisdn", resultSet.getString(11))
                                .put("postalAddress", resultSet.getString(12)))
                        .put("isActive", resultSet.getString(13))
                        .put("firmDeductionPlanDetails", new JSONObject().put("firmDeductionPlanId", resultSet.getInt(14))
                                .put("deductionPlanId", resultSet.getInt(15))
                                .put("firmId", resultSet.getInt(16))
                                .put("isActive", resultSet.getString(17))));

            }
        } catch (SQLException ex) {
            logger.error(Constants.ERROR_LOG_TEMPLATE, Constants.ERROR, ex.getClass().getSimpleName(), ex.getMessage());
            return new JSONObject();
        }
        return  new JSONObject().put("firms", firms);
    }

    public JSONObject getFirmWithAdminDetails(int firmId, int adminUserCategoryId){
        JSONObject firmWithAdminDetails=new JSONObject();
        JSONArray adminUsersArray = new JSONArray();
        String query= """
                SELECT f.firm_id, f.business_name, f.business_nature, f.registration_pin, f.kra_pin, f.auto_dispatch_advance, f.advance_interest_rate,
                fc.location, fc.email, fc.primary_msisdn, fc.secondary_msisdn, fc.postal_address,\s
                f.is_active, fdp.firm_deduction_plan_id, fdp.deduction_plan_id, fdp.firm_id, fdp.is_active, u.user_id,\s
                u.username, ud.first_name, ud.last_name, ud.msisdn, ud.email\s
                FROM firms f\s
                JOIN firms_contact fc ON f.firm_id = fc.firm_id\s
                JOIN firm_deduction_plans fdp ON f.firm_id = fdp.firm_id\s
                JOIN users u ON f.firm_id = u.firm_id\s
                JOIN users_details ud ON u.user_id = ud.user_id\s
                WHERE f.firm_id = ? AND u.user_category_id = ?
                """;

        try (Connection connection= mysqlConnector.getConnection(); PreparedStatement preparedStatement=connection.prepareStatement(query)){
            preparedStatement.setInt(1, firmId);
            preparedStatement.setInt(2, adminUserCategoryId);
            ResultSet resultSet= preparedStatement.executeQuery();

            while (resultSet.next()){
                firmWithAdminDetails.put("firmId", resultSet.getInt(1))
                        .put("businessName", resultSet.getString(2))
                        .put("businessNature", resultSet.getString(3))
                        .put("registrationPin", resultSet.getString(4))
                        .put("kraPin", resultSet.getString(5))
                        .put("autoDispatchAdvance", resultSet.getString(6))
                        .put("salaryLoanInterest",resultSet.getInt(7) )
                        .put("contacts", new JSONObject().put("location", resultSet.getString(8))
                                .put("email", resultSet.getString(9))
                                .put("primaryMsisdn", resultSet.getString(10))
                                .put("secondaryMsisdn", resultSet.getString(11))
                                .put("postalAddress", resultSet.getString(12)))
                        .put("isActive", resultSet.getString(13))
                        .put("firmDeductionPlanDetails", new JSONObject().put("firmDeductionPlanId", resultSet.getInt(14))
                                .put("deductionPlanId", resultSet.getInt(15))
                                .put("firmId", resultSet.getInt(16))
                                .put("isActive", resultSet.getString(17)));

                JSONObject adminUser=new JSONObject().put("userId", resultSet.getInt(18))
                        .put("username", resultSet.getString(19))
                        .put("firstName", resultSet.getString(20))
                        .put("lastName", resultSet.getString(21))
                        .put("msisdn", resultSet.getString(22))
                        .put("email", resultSet.getString(23));

                adminUsersArray.put(adminUser);
                firmWithAdminDetails.put("adminUsers", adminUsersArray);
            }

        } catch (SQLException ex) {
            logger.error(Constants.ERROR_LOG_TEMPLATE, Constants.ERROR, ex.getClass().getSimpleName(), ex.getMessage());
        }
        return firmWithAdminDetails;
    }



}
