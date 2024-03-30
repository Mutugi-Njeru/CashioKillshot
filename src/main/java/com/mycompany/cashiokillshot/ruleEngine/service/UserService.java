package com.mycompany.cashiokillshot.ruleEngine.service;

import com.mycompany.cashiokillshot.dao.UserCategoriesDao;
import com.mycompany.cashiokillshot.dao.UserDao;
import com.mycompany.cashiokillshot.models.UserSignupRequest;
import com.mycompany.cashiokillshot.records.ServiceResponder;
import com.mycompany.cashiokillshot.utility.Constants;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.json.JSONArray;
import org.json.JSONObject;

@ApplicationScoped
public class UserService {

    @Inject
    UserCategoriesDao userCategoriesDao;
    @Inject
    UserDao userDao;
    @Inject
    BeanValidatorService beanValidatorService;

    public ServiceResponder addUserCategory(JSONObject object){
        String category= object.optString("userCategory", Constants.EMPTY_STRING);

        int categoryId=userCategoriesDao.addUserCategory(category);

        return (categoryId>1)
                ?new ServiceResponder(true, categoryId)
                :new ServiceResponder(false, "user category not created");
    }

    public ServiceResponder getUserCategories(){
        JSONArray response=userCategoriesDao.getUserCategories();
        return (!response.isEmpty())
                ?new ServiceResponder(true, new JSONObject().put("userCategories", response))
                :new ServiceResponder(false, "user categories not found");
    }

    public ServiceResponder createUser(UserSignupRequest user){
        int userId= userDao.createUser(user);
        return (userId>0)
                ?new ServiceResponder(true, "user created successfully")
                :new ServiceResponder(false, "cannot create user");

    }
}
