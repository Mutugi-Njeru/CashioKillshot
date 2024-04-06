package com.mycompany.cashiokillshot.models;

import com.mycompany.cashiokillshot.utility.Constants;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.json.JSONObject;

public class AdvanceRequest {
    @Min(value = 1, message = "employeeId cannot be zero or negative")
    private final int employeeId;
    @Min(value = 1, message = "firmDeductionPlanId cannot be zero or negative")
    private final int firmDeductionPlanId;
    @Min(value = 1, message = "amount cannot be zero")
    private final int amount;
    @NotBlank(message = "channel cannot be empty")
    private final String channel;
    @NotBlank(message = "msisdn cannot be empty")
    private final String msisdn;

    public AdvanceRequest(JSONObject object){
        employeeId=object.optInt("employeeId", 0);
        firmDeductionPlanId= object.optInt("firmDeductionPlanId", 0);
        amount=object.optInt("amount", 0);
        channel= object.optString("channel", Constants.EMPTY_STRING);
        msisdn= object.optString("msisdn", Constants.EMPTY_STRING);
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public int getFirmDeductionPlanId() {
        return firmDeductionPlanId;
    }

    public int getAmount() {
        return amount;
    }

    public String getChannel() {
        return channel;
    }

    public String getMsisdn() {
        return msisdn;
    }
}
