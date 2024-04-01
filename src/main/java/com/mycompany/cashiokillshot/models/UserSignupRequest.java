package com.mycompany.cashiokillshot.models;

import com.mycompany.cashiokillshot.utility.Constants;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.json.JSONObject;

public class UserSignupRequest {
    @Min(value=1, message = "firmId cannot be zero or negative")
    private final int firmId;
    @NotBlank(message = "firstName cannot be blank")
    private final String firstName;
    @NotBlank(message = "lastName cannot be blank")
    private final String lastName;
    @NotBlank(message = "msisdn cannot be empty")
    @Size(min = 12, message = "msisdn should have 12 digits")
    private final String msisdn;
    @Email(message = "email should be valid")
    private final String email;
    @NotBlank(message = "username cannot be empty")
    private final String username;
    @NotBlank(message = "password cannot be empty")
    private final String pasword;
    @Min(value = 1, message = "userCategoryId cannot be zero or negative")
    private final int userCategoryId;

    public UserSignupRequest(JSONObject object){
         firmId= object.optInt("firmId", 0);
         firstName= object.optString("firstName", Constants.EMPTY_STRING);
         lastName= object.optString("lastName", Constants.EMPTY_STRING);
         msisdn= object.optString("msisdn", Constants.EMPTY_STRING);
         email= object.optString("email", Constants.EMPTY_STRING);
         username= object.optString("username", Constants.EMPTY_STRING);
         pasword= object.optString("password", Constants.EMPTY_STRING);
         userCategoryId= object.optInt("userCategoryId", 0);
    }

    public int getFirmId() {
        return firmId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getPasword() {
        return pasword;
    }

    public int getUserCategoryId() {
        return userCategoryId;
    }
}
