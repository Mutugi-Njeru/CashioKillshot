package com.mycompany.cashiokillshot.ruleEngine.service;

import com.mycompany.cashiokillshot.dao.UserCategoriesDao;
import com.mycompany.cashiokillshot.dao.UserDao;
import com.mycompany.cashiokillshot.models.User;
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

        if (userId>0){
            boolean isSaved= userDao.saveUserDetails(user, userId);
            return (isSaved)
                    ?new ServiceResponder(true, "user created successfully")
                    :new ServiceResponder(false, "unable to create user");
        }
        else {
            return  new ServiceResponder(false, "cannot create user");
        }
    }

    public ServiceResponder getUsers(JSONObject object){
        int firmId= object.optInt(Constants.FIRM_ID_KEY, 0);
        int lastRecordId= object.optInt(Constants.LAST_RECORD_ID_KEY, 0);

        JSONObject users=userDao.getUsers(firmId, lastRecordId);

        return (!users.isEmpty())
                ?new ServiceResponder(true, users)
                :new ServiceResponder(false, "users not found");
    }

    public ServiceResponder changeUserStatus(JSONObject object){
        int firmUserId= object.optInt(Constants.FIRM_USERID_KEY, 0);
        int userId= object.optInt(Constants.USERID_KEY,0);
        User user=userDao.getUser(userId);
        String isActive=object.optString("isActive", Constants.EMPTY_STRING);

        if (firmUserId>0 && user.getUserCategoryId()==1){//super admin
            boolean isChanged=userDao.updateUerStatus(isActive, firmUserId);

            return (isChanged)
                    ?new ServiceResponder(true, "user status updated successfully")
                    :new ServiceResponder(false, "cannot update user status");
        }
        else {
            return new ServiceResponder(false, "unauthorised action or invalid firmUserId");
        }

    }

    public ServiceResponder deleteUser(JSONObject object){
        int firmUserId= object.optInt(Constants.FIRM_USERID_KEY, 0);
        int userId= object.optInt(Constants.USERID_KEY, 0);
        User user=userDao.getUser(userId);

        if (firmUserId>0 && user.getUserCategoryId()==1){//super admin
            boolean isDeleted= userDao.deleteUser(userId);
            return (isDeleted)
                    ?new ServiceResponder(true, "user deleted successfully")
                    :new ServiceResponder(false, "cannot delete user");
        }
        else {
            return new ServiceResponder(false, "unauthorised action or invalid firmUserId");
        }
    }

    public ServiceResponder updateUserPassword(JSONObject object){
        int userId= object.optInt(Constants.USERID_KEY, 0);
        String password= object.optString(Constants.PASSWORD_KEY, Constants.EMPTY_STRING);
         boolean isUpdated= userDao.updateUserPassword(userId, password);

         return (isUpdated)
                 ?new ServiceResponder(true, "password updated successfully")
                 :new ServiceResponder(false, "could not update password");


    }


}
