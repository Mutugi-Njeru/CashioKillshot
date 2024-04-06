package com.mycompany.cashiokillshot.models;

import com.mycompany.cashiokillshot.utility.Constants;
import org.json.JSONObject;

public class Employee {
    private final int employeeId;
    private final int firmId;
    private final String firstName;
    private final String lastName;
    private final String msisdn;
    private final String idNumber;
    private final double advanceLimit;
    private final boolean active;
    private final boolean hasAcceptedTermsAndConditions;

    public Employee(JSONObject object){
        employeeId=object.optInt("employeeId", 0);
        firmId=object.optInt("firmId", 0);
        firstName=object.optString("firstName", Constants.EMPTY_STRING);
        lastName=object.optString("lastName", Constants.EMPTY_STRING);
        String mobileNumber=object.optString("msisdn", Constants.EMPTY_STRING);
        msisdn=(mobileNumber.startsWith("254") && mobileNumber.length()==12) ? mobileNumber :Constants.EMPTY_STRING;
        idNumber=String.valueOf(object.optInt("idNumber", 0));
        advanceLimit=object.optDouble("advanceLimit", 0.00);
        active=object.optBoolean("isActive", false);
        hasAcceptedTermsAndConditions=object.optBoolean("hasAcceptedTermsAndConditions", false);
    }

    public int getEmployeeId() {
        return employeeId;
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

    public double getAdvanceLimit() {
        return advanceLimit;
    }

    public boolean isActive() {
        return active;
    }

    public boolean isHasAcceptedTermsAndConditions() {
        return hasAcceptedTermsAndConditions;
    }
}
