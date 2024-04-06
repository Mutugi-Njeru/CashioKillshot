package com.mycompany.cashiokillshot.utility;

import com.auth0.jwt.algorithms.Algorithm;

public class Constants {
    private Constants(){}

    public static final String EMPTY_STRING="";
    public static final String DATA_KEY = "data";
    public static final String REQUEST_TYPE_KEY = "requestType";
    public static final int FAILURE_STATUS_CODE = 199;
    public static final int SUCCESS_STATUS_CODE = 200;
    public static final String ERROR="error";
    public static final String LAST_RECORD_ID_KEY="lastRecordId";
    public static final String ERROR_LOG_TEMPLATE="{}=> {} | {}";
    public static final String SHA_256_HASH_ALGORITHM = "SHA-256";
    public static final String USERNAME_KEY = "username";
    public static final String USERID_KEY = "userId";
    public static final String FIRM_USERID_KEY = "firmUserId";
    public static final String PASSWORD_KEY = "password";
    public static final String ACCESS_TOKEN_KEY = "token";
    public static final String TYPE_KEY = "type";
    public static final String FIRM_ID_KEY = "firmId";
    public static final String EXPIRES_IN_KEY = "expiresIn";

    public static final int TOKEN_EXPIRY_TIME_IN_SECONDS = 3600;
    public static final String JWT_SECRET = "savitar";
    public static final Algorithm JWT_ALGORITHM = Algorithm.HMAC512(Constants.JWT_SECRET);
}
