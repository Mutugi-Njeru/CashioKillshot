package com.mycompany.cashiokillshot.models;

import com.mycompany.cashiokillshot.utility.Constants;
import jakarta.validation.constraints.*;
import org.json.JSONObject;

public class User {
    @Min(value = 1, message = "firmId cannot be zero or negative")
    private final int firmId;
    @Min(value = 1, message = "userId cannot be zero or negative")
    private final int userId;
    @NotBlank(message = "firstName cannot be blank")
    private final String firstName;
    @NotBlank(message = "lastName cannot be blank")
    private final String lastName;
    @NotBlank(message = "msisdn cannot be empty")
    @Size(min = 12, message = "msisdn should have 12 digits")
    private final String msisdn;
    @Email(message = "email should be valid")
    private final String email;
    @Size(min = 1, message = "userCategoryId cannot be zero or negative")
    private final int userCategoryId;
    private final String password;
    private final boolean isActive;
    @NotNull(message = "valid cannot be empty")
    private final boolean valid;

    public User (JSONObject object){
        valid=!object.isEmpty();
        firmId=object.optInt("firmId", 0);
        userId=object.optInt("userId", 0);
        firstName=object.optString("firstName", Constants.EMPTY_STRING);
        lastName= object.optString("lastName", Constants.EMPTY_STRING);
        msisdn=object.optString("msisdn", Constants.EMPTY_STRING);
        email=object.optString("email", Constants.EMPTY_STRING);
        userCategoryId=object.optInt("userCategoryId", 0);
        password= object.optString("password", Constants.EMPTY_STRING);
        isActive=object.optBoolean("isActive", false);
    }

    public int getFirmId() {
        return firmId;
    }

    public int getUserId() {
        return userId;
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

    public int getUserCategoryId() {
        return userCategoryId;
    }

    public String getPassword() {
        return password;
    }

    public boolean isActive() {
        return isActive;
    }

    public boolean isValid() {
        return valid;
    }
}
