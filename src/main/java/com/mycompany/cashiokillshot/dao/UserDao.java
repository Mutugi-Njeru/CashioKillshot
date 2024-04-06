package com.mycompany.cashiokillshot.dao;

import com.mycompany.cashiokillshot.cipher.Sha256Hasher;
import com.mycompany.cashiokillshot.database.MysqlConnector;
import com.mycompany.cashiokillshot.models.User;
import com.mycompany.cashiokillshot.models.UserSignupRequest;
import com.mycompany.cashiokillshot.utility.Constants;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.*;

@ApplicationScoped
public class UserDao {

    @Inject
    MysqlConnector mysqlConnector;
    @Inject
    Sha256Hasher sha256Hasher;

    private static final Logger logger= LogManager.getLogger(UserDao.class);

    public int createUser(UserSignupRequest user){
        String password= sha256Hasher.createHashText(user.getPasword());
        String query="INSERT IGNORE INTO users(firm_id, user_category_id, username, password) VALUES (?,?,?,?)";
        int userId=0;


        try(Connection connection= mysqlConnector.getConnection(); PreparedStatement preparedStatement=connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setInt(1, user.getFirmId());
            preparedStatement.setInt(2, user.getUserCategoryId());
            preparedStatement.setString(3, user.getUsername());
            preparedStatement.setString(4, password);
            preparedStatement.executeUpdate();

            ResultSet resultSet= preparedStatement.getGeneratedKeys();
            while (resultSet.next()){
                userId=resultSet.getInt(1);
            }

        } catch (SQLException ex) {
            logger.error(Constants.ERROR_LOG_TEMPLATE, Constants.ERROR, ex.getClass().getSimpleName(), ex.getMessage());
            return userId;
        }
        return userId;
    }


    public boolean saveUserDetails(UserSignupRequest user, int userId){
        String query="INSERT INTO users_details(user_id, first_name, last_name, msisdn, email) VALUES (?,?,?,?,?)";
        boolean status=false;

        try(Connection connection= mysqlConnector.getConnection(); PreparedStatement preparedStatement= connection.prepareStatement(query)) {
            preparedStatement.setInt(1, userId);
            preparedStatement.setString(2, user.getFirstName());
            preparedStatement.setString(3, user.getLastName());
            preparedStatement.setString(4, user.getMsisdn());
            preparedStatement.setString(5, user.getEmail());
            status=preparedStatement.executeUpdate()>0;
        } catch (SQLException ex) {
            logger.error(Constants.ERROR_LOG_TEMPLATE, Constants.ERROR, ex.getClass().getSimpleName(), ex.getMessage());
            return  status;
        }
        return status;
    }

    public User getUser(int userId){
        JSONObject object=new JSONObject();
        String query="""
                     SELECT u.user_id, u.firm_id, u.user_category_id, ud.first_name, ud.last_name, ud.msisdn, ud.email \n" +
                "FROM users u \n" +
                "inner join  users_details ud on u.user_id=ud.user_id\n" +
                "WHERE u.user_id=?
                     """;

        try (Connection connection= mysqlConnector.getConnection(); PreparedStatement preparedStatement=connection.prepareStatement(query)){
            preparedStatement.setInt(1, userId);
            ResultSet resultSet= preparedStatement.executeQuery();

            while (resultSet.next()){
                object.put("userId", resultSet.getInt(1))
                        .put("firmId", resultSet.getInt(2))
                        .put("userCategoryId", resultSet.getInt(3))
                        .put("firstName", resultSet.getString(4))
                        .put("lastName", resultSet.getString(5))
                        .put("msisdn", resultSet.getString(6))
                        .put("email", resultSet.getString(7));
            }

        } catch (SQLException ex) {
            logger.error(Constants.ERROR_LOG_TEMPLATE, Constants.ERROR, ex.getClass().getSimpleName(), ex.getMessage());
            return new User(object);
        }
        return new User(object);
    }

    public JSONObject getUsers(int firmId, int lastRecordId){
        JSONArray result=new JSONArray();
        String query= """
                SELECT u.user_id, f.business_name, ud.first_name, ud.last_name, ud.msisdn, ud.email, uc.title, u.is_active\s
                FROM users u
                inner join firms f on u.firm_id=f.firm_id
                inner join  user_categories uc on uc.user_category_id=u.user_category_id\s
                inner join users_details ud on ud.user_id=u.user_id
                WHERE u.firm_id = ? \s
                AND u.user_id > ? LIMIT 1000;
                """;
        try (Connection connection= mysqlConnector.getConnection(); PreparedStatement preparedStatement= connection.prepareStatement(query)){
            preparedStatement.setInt(1, firmId);
            preparedStatement.setInt(2, lastRecordId);

            ResultSet resultSet= preparedStatement.executeQuery();
            while (resultSet.next()){
                JSONObject object=new JSONObject().put("userId", resultSet.getInt(1))
                        .put("firm", resultSet.getString(2))
                        .put("firstName", resultSet.getString(3))
                        .put("lastName", resultSet.getString(4))
                        .put("msisdn", resultSet.getString(5))
                        .put("email", resultSet.getString(6))
                        .put("userCategory", resultSet.getString(7))
                        .put("isActive", resultSet.getString(8));
                result.put(object);

            }
        } catch (SQLException ex) {
            logger.error(Constants.ERROR_LOG_TEMPLATE, Constants.ERROR, ex.getClass().getSimpleName(), ex.getMessage());
            return new JSONObject().put("users", result);
        }
        return new JSONObject().put("users", result);
    }
    public boolean updateUerStatus(String isActive, int firmUserId){
        String query="UPDATE users SET is_active=? WHERE user_id=? LIMIT 1";
        boolean status=false;

        try (Connection connection= mysqlConnector.getConnection(); PreparedStatement preparedStatement=connection.prepareStatement(query)){
            preparedStatement.setString(1, isActive);
            preparedStatement.setInt(2, firmUserId);
            status=preparedStatement.executeUpdate()>0;

        } catch (SQLException ex) {
            logger.error(Constants.ERROR_LOG_TEMPLATE, Constants.ERROR, ex.getClass().getSimpleName(), ex.getMessage());
            return status;
        }
        return status;
    }

    public boolean deleteUser(int firmUserId){
        String query="DELETE FROM users WHERE user_id=? LIMIT 1";
        boolean status=false;

        try(Connection connection= mysqlConnector.getConnection(); PreparedStatement preparedStatement=connection.prepareStatement(query)) {
            preparedStatement.setInt(1, firmUserId);
            status=preparedStatement.executeUpdate()>0;
        } catch (SQLException ex) {
            logger.error(Constants.ERROR_LOG_TEMPLATE, Constants.ERROR, ex.getClass().getSimpleName(), ex.getMessage());
            return status;
        }
        return status;
    }

    public boolean updateUserPassword(int userId, String password){
        String query="UPDATE users SET password=? WHERE user_id=? LIMIT 1";
        boolean status=false;
        String hashPassword=sha256Hasher.createHashText(password);

        try(Connection connection= mysqlConnector.getConnection(); PreparedStatement preparedStatement=connection.prepareStatement(query)) {
            preparedStatement.setString(1, hashPassword);
            preparedStatement.setInt(2, userId);
            status= preparedStatement.executeUpdate()>0;

        } catch (SQLException ex) {
            logger.error(Constants.ERROR_LOG_TEMPLATE, Constants.ERROR, ex.getClass().getSimpleName(), ex.getMessage());
            return status;
        }
        return  status;
    }
}
