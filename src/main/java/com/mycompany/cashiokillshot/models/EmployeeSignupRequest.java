package com.mycompany.cashiokillshot.models;

import com.mycompany.cashiokillshot.utility.Constants;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.json.JSONObject;

public class EmployeeSignupRequest {
    @NotNull(message="firmId cannot be empty")
    private final int firmId;
    @NotBlank(message = "firstName cannot be empty")
    private final String firstName;
    @NotBlank(message = "lastName cannot be empty")
    private final String lastName;
    @NotBlank(message = "msisdn cannot be empty")
    private final String msisdn;
    @NotBlank(message = "idNumber cannot be empty")
    private final String idNumber;

    public EmployeeSignupRequest(JSONObject object){
        firmId=object.optInt("firmId", 0);
        firstName= object.optString("firstName", Constants.EMPTY_STRING);
        lastName= object.optString("lastName", Constants.EMPTY_STRING);
        String mobileNumber= object.optString("msisdn", Constants.EMPTY_STRING);
        msisdn=(mobileNumber.startsWith("254") && mobileNumber.length()==12)? mobileNumber :Constants.EMPTY_STRING;
        idNumber=String.valueOf(object.optInt("idNumber", 0));
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

    public int getIdNumber() {
        return Integer.parseInt(idNumber);
    }
}
