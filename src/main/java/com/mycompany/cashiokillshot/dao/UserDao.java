package com.mycompany.cashiokillshot.dao;

import com.mycompany.cashiokillshot.cipher.Sha256Hasher;
import com.mycompany.cashiokillshot.database.MysqlConnector;
import com.mycompany.cashiokillshot.models.UserSignupRequest;
import com.mycompany.cashiokillshot.utility.Constants;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
        String query="INSERT IGNORE INTO users(firms_id, user_category_id, username, password) VALUES (?,?,?,?)";
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


//    public boolean saveUserDetails(UserSignupRequest user, int userId){
//
//
//    }

}
