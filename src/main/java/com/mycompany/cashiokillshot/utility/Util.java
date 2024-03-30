package com.mycompany.cashiokillshot.utility;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Util {
    private Util(){}
    private static final Logger logger= LogManager.getLogger(Util.class);

    public static JSONObject buildResponse(int statusCode, Object status, Object message){
        try {
            return new JSONObject().put("statusCode", statusCode)
                    .put("status", status)
                    .put("message", message);
        }
        catch (JSONException ex) {
            logger.info("ERROR=> {} | {}", ex.getClass().getSimpleName(), ex.getMessage());
            return new JSONObject();
        }
    }

    public static String getTimestamp()
    {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());
    }


}
