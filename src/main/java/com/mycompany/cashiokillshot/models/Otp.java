package com.mycompany.cashiokillshot.models;

import com.mycompany.cashiokillshot.utility.Constants;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.json.JSONObject;

public class Otp {
    @NotNull(message = "otp cannot be empty")
    private final String otp;
    @NotBlank(message = "msisdn cannot be blank")
    private final String msisdn;
    @NotBlank(message = "operation cannot be blank")
    private final String operation;

    public Otp(JSONObject object){
        otp=object.optString("otp", Constants.EMPTY_STRING).trim();
        msisdn=object.optString("msisdn", Constants.EMPTY_STRING).trim();
        operation= object.optString("operation", Constants.EMPTY_STRING).trim();

    }

    public String getOtp() {
        return otp;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public String getOperation() {
        return operation;
    }
}
