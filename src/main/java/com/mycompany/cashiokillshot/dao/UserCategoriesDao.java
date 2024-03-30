package com.mycompany.cashiokillshot.dao;

import com.mycompany.cashiokillshot.database.MysqlConnector;
import com.mycompany.cashiokillshot.utility.Constants;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.*;

@ApplicationScoped
public class UserCategoriesDao {

    @Inject
    MysqlConnector mysqlConnector;
    private static final Logger logger= LogManager.getLogger(UserCategoriesDao.class);

    public int addUserCategory(String title){
        String query="INSERT INTO user_categories(title) VALUES (?)";
        int userCategoryId = 0;

        try (Connection connection= mysqlConnector.getConnection(); PreparedStatement preparedStatement= connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)){
            preparedStatement.setString(1, title);
            preparedStatement.executeUpdate();

            ResultSet resultSet= preparedStatement.getGeneratedKeys();
            while (resultSet.next()){
                userCategoryId=resultSet.getInt(1);
            }

        } catch (SQLException ex) {
            logger.error(Constants.ERROR_LOG_TEMPLATE, Constants.ERROR, ex.getClass().getSimpleName(), ex.getMessage());
            return userCategoryId;
        }
        return userCategoryId;
    }

    public JSONArray getUserCategories(){
        JSONArray userCategories=new JSONArray();
        String query="SELECT user_category_id, title FROM user_categories";

        try (Connection connection= mysqlConnector.getConnection(); PreparedStatement preparedStatement= connection.prepareStatement(query)){
            ResultSet resultSet= preparedStatement.executeQuery();

            while (resultSet.next()){
                JSONObject object=new JSONObject().put("userCategoryId", resultSet.getInt(1))
                        .put("title", resultSet.getString(2));
                userCategories.put(object);
            }

        } catch (SQLException ex) {
            logger.error(Constants.ERROR_LOG_TEMPLATE, Constants.ERROR, ex.getClass().getSimpleName(), ex.getMessage());
            return userCategories;
        }
        return userCategories;
    }
}
