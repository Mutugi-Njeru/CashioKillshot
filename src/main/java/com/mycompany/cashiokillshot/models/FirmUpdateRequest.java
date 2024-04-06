package com.mycompany.cashiokillshot.models;

import com.mycompany.cashiokillshot.utility.Constants;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.json.JSONObject;

public class FirmUpdateRequest {
    @Min(value = 1, message = "firmId is mandatory")
    private final int firmId;
    @Min(value =1, message = "userId is mandatory")
    private final int userId;
    @NotNull(message = "userCategory cannot be empty")
    private final String userCategory;
    private final double advanceInterestRate;
    @NotNull(message = "autoDispatchAdvance cannot be empty")
    private final Boolean autoDispatchAdvance;
    private final Boolean isActive;
    private final JSONObject object;

    public FirmUpdateRequest(JSONObject object){
        this.object=object;
        firmId= object.optInt("firmId", 0);
        userId= object.optInt("userId", 0);
        userCategory= object.optString("userCategory", Constants.EMPTY_STRING);
        advanceInterestRate= object.optDouble("advanceInterestRate", 0.00);
        autoDispatchAdvance=object.optBoolean("autoDispatchAdvance", false);
        isActive=object.optBoolean("isActive", false);
    }

    public int getFirmId() {
        return firmId;
    }

    public int getUserId() {
        return userId;
    }

    public String getUserCategory() {
        return userCategory;
    }

    public double getAdvanceInterestRate() {
        return advanceInterestRate;
    }

    public Boolean getAutoDispatchAdvance() {
        return autoDispatchAdvance;
    }

    public Boolean getActive() {
        return isActive;
    }

    public JSONObject getObject() {
        return object;
    }
}
