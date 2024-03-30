package com.mycompany.cashiokillshot.dao;

import com.mycompany.cashiokillshot.database.MysqlConnector;
import com.mycompany.cashiokillshot.records.Authentication;
import com.mycompany.cashiokillshot.utility.Constants;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@ApplicationScoped
public class AuthenticationDao {

    @Inject
    MysqlConnector mysqlConnector;

    private static final Logger logger= LogManager.getLogger(AuthenticationDao.class);

    public Authentication authenticateUser(String username, String password){
        String query="SELECT COUNT(user_id), username FROM users WHERE username=? AND password=?";
        int count = 0;
        String name = null;

        try (Connection connection= mysqlConnector.getConnection();PreparedStatement preparedStatement= connection.prepareStatement(query)){
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet resultSet=preparedStatement.executeQuery();

            while (resultSet.next()){
                count=resultSet.getInt(1);
                name=resultSet.getString(2);
            }
            return new Authentication(count==1, name);

        } catch (SQLException ex) {
            logger.error(Constants.ERROR_LOG_TEMPLATE, Constants.ERROR, ex.getClass().getSimpleName(), ex.getMessage());
            return new Authentication(count==1, name);
        }
    }


}

