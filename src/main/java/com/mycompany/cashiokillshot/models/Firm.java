package com.mycompany.cashiokillshot.models;

import com.mycompany.cashiokillshot.utility.Constants;
import org.json.JSONObject;

public class Firm {

    private final int firmId;
    private final String businessName;
    private final String businessNature;
    private final String registrationPin;
    private final String kraPin;
    private final JSONObject contacts;
    private final String location;
    private final String email;
    private final String primaryMsisdn;
    private  final String secondaryMsisdn;
    private  final String postalAddress;
    private final String accountStatus;
    private final boolean isActive;
    private final boolean valid;
    private  final boolean autoDispatchAdvance;
    private final double chargeRate;
    private final JSONObject toJson;

    public Firm(JSONObject object){
        this.valid=!object.isEmpty();
        this.firmId= object.optInt("firmId", 0);
        this.businessName=object.optString("businessName", Constants.EMPTY_STRING);
        this.businessNature= object.optString("businessNature", Constants.EMPTY_STRING);
        this.registrationPin=object.optString("registrationPin", Constants.EMPTY_STRING);
        this.kraPin=object.optString("kraPin", Constants.EMPTY_STRING);
        this.autoDispatchAdvance=object.optBoolean("autoDispatchAdvance", false);
        this.chargeRate=object.optDouble("chargeRate", 0.0);
        this.contacts=(object.has("contacts")) ?object.getJSONObject("contacts") :new JSONObject();
        this.location=contacts.optString("location", Constants.EMPTY_STRING);
        this.email=contacts.optString("email", Constants.EMPTY_STRING);
        this.primaryMsisdn=contacts.optString("primaryMsisdn", Constants.EMPTY_STRING);
        this.secondaryMsisdn= contacts.optString("secondaryMsisdn", Constants.EMPTY_STRING);
        this.postalAddress=contacts.optString("postalAddress", Constants.EMPTY_STRING);
        this.accountStatus=object.optString("accountStatus", Constants.EMPTY_STRING);
        this.isActive=object.optBoolean("isActive", false);
        this.toJson=object;
    }

    public int getFirmId() {
        return firmId;
    }

    public String getBusinessName() {
        return businessName;
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

    public String getAccountStatus() {
        return accountStatus;
    }

    public boolean isActive() {
        return isActive;
    }

    public boolean isValid() {
        return valid;
    }

    public boolean isAutoDispatchAdvance() {
        return autoDispatchAdvance;
    }

    public double getChargeRate() {
        return chargeRate;
    }

    public JSONObject getToJson() {
        return toJson;
    }
}
