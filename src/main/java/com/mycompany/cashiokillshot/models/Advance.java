package com.mycompany.cashiokillshot.models;

import com.mycompany.cashiokillshot.utility.Constants;
import org.json.JSONObject;

public class Advance {
    private final int employeeId;
    private final int amount;
    private final String description;
    private final String channel;
    private final int advanceRequestId;
    private final int chargeFee;
    private final int salaryLoan;
    private final String dueDate;
    private final boolean dispatched;
    private final boolean open;
    private final String requestStatus;

    public Advance(JSONObject object){
        advanceRequestId= object.optInt("advanceRequestId", 0);
        employeeId= object.optInt("employeeId", 0);
        amount=object.optInt("amount", 0);
        chargeFee=object.optInt("chargeFee", 0);
        salaryLoan=object.optInt("salaryLoan", 0);
        channel= object.optString("channel", Constants.EMPTY_STRING);
        dueDate= object.optString("dueDate", "0000-00-00");
        requestStatus=object.optString("requestStatus", Constants.EMPTY_STRING);
        dispatched= object.optBoolean("isDispatched", false);
        open=object.optBoolean("isOpen", false);
        description= object.optString("description", Constants.EMPTY_STRING);
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public int getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public String getChannel() {
        return channel;
    }

    public int getAdvanceRequestId() {
        return advanceRequestId;
    }

    public int getChargeFee() {
        return chargeFee;
    }

    public int getSalaryLoan() {
        return salaryLoan;
    }

    public String getDueDate() {
        return dueDate;
    }

    public boolean isDispatched() {
        return dispatched;
    }

    public boolean isOpen() {
        return open;
    }

    public String getRequestStatus() {
        return requestStatus;
    }
}
