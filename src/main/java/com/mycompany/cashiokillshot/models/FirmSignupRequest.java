package com.mycompany.cashiokillshot.models;

import com.mycompany.cashiokillshot.utility.Constants;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import org.json.JSONObject;

public class FirmSignupRequest {
    @NotNull(message = "businessName cannot be empty")
    private final String businessname;
    @NotNull(message = "businessNature cannot be empty")
    private final String businessNature;
    @NotNull(message = "registrationPin cannot be empty")
    private final String registrationPin;
    @NotNull(message = "kraPin cannot be empty")
    private final String kraPin;
    private final JSONObject contacts;
    @NotNull(message = "location cannot be empty")
    private final String location;
    @NotNull(message="email cannot be empty")
    private final String email;
    @NotNull(message = "primaryMsisdn cannot be empty")
    private  final String primaryMsisdn;
    @NotNull(message = "secondaryMsisdn cannot be empty")
    private  final String secondaryMsisdn;
    @NotNull(message = "postalAddress cannot be empty")
    private final String postalAddress;
    @NotNull
    private final JSONObject object;
    @NotNull(message = "autoDispatchAdvance cannot be empty")
    private final boolean autoDispatchAdvance;
    @DecimalMin(value = "0.01", message = "advanceInterestRate is mandatory")
    @Digits(integer = 1, fraction =2)
    private final double advanceInterestRate;

    public FirmSignupRequest(JSONObject object){
        this.object=object;
        this.businessname=object.optString("businessName", Constants.EMPTY_STRING);
        this.businessNature=object.optString("businessNature", Constants.EMPTY_STRING);
        this.registrationPin=object.optString("registrationPin", Constants.EMPTY_STRING);
        this.kraPin= object.optString("kraPin", Constants.EMPTY_STRING);
        this.advanceInterestRate=object.optDouble("advanceInterestRate", 0.00);
        this.autoDispatchAdvance=object.optBoolean("autoDispatchAdvance", false);
        this.contacts=(object.has("contacts"))? object.getJSONObject("contacts"):new JSONObject();
        this.location=contacts.optString("location", Constants.EMPTY_STRING);
        this.email=contacts.optString("email", Constants.EMPTY_STRING);
        this.primaryMsisdn=contacts.optString("primaryMsisdn", Constants.EMPTY_STRING);
        this.secondaryMsisdn=contacts.optString("secondaryMsisdn", Constants.EMPTY_STRING);
        this.postalAddress=contacts.optString("postalAddress", Constants.EMPTY_STRING);
    }

    public String getBusinessname() {
        return businessname;
    }
    public String getBusinessNature() {
        return businessNature;
    }
    public String getRegistrationPin() {
        return registrationPin;
    }
    public String getKraPin() {
        return kraPin;
    }
    public JSONObject getContacts() {
        return contacts;
    }
    public String getLocation() {
        return location;
    }
    public String getEmail() {
        return email;
    }
    public String getPrimaryMsisdn() {
        return primaryMsisdn;
    }
    public String getSecondaryMsisdn() {
        return secondaryMsisdn;
    }
    public String getPostalAddress() {
        return postalAddress;
    }
    public JSONObject getObject() {
        return object;
    }
    public boolean isAutoDispatchAdvance() {
        return autoDispatchAdvance;
    }
    public double getAdvanceInterestRate() {
        return advanceInterestRate;
    }
}
