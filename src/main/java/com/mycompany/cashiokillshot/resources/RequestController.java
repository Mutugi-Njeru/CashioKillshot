package com.mycompany.cashiokillshot.resources;

import com.mycompany.cashiokillshot.enums.RequestTypes;
import com.mycompany.cashiokillshot.enums.ResultIds;
import com.mycompany.cashiokillshot.ruleEngine.engine.Engine;
import com.mycompany.cashiokillshot.utility.Constants;
import com.mycompany.cashiokillshot.utility.Util;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Path("v1")
public class RequestController {
    @Inject
    Engine engine;
    private static final Logger logger = LogManager.getLogger(RequestController.class);

    @GET
    @Path("authenticate")
    @Produces(MediaType.APPLICATION_JSON)
    public Response authenticate(@HeaderParam(HttpHeaders.AUTHORIZATION) String basicAuth){
        String[] authString=new String(Base64.getDecoder().decode(basicAuth.replace("Basic", "").trim())).split(":");

        logger.info("AUTH REQUEST RECEIVED");

        JSONObject request=new JSONObject().put(Constants.USERNAME_KEY, authString[0]).put(Constants.PASSWORD_KEY, authString[1]);
        JSONObject response=engine.init(request, RequestTypes.AUTHENTICATE.name(), Constants.EMPTY_STRING);

        logger.info("AUTH RESPONSE=> {}", response);

        return Response.ok()
                .entity(response.toString())
                .build();
    }

    @POST
    @Path("/request")
    @Produces(MediaType.APPLICATION_JSON)
    public Response doSomething(InputStream inputStream, @HeaderParam(HttpHeaders.AUTHORIZATION) String bearerToken)
    {

        try
        {
            String inputString = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            JSONObject object = new JSONObject(inputString);

            logger.info("REQUEST RECEIVED=> {}", object);

            JSONObject request = (object.has(Constants.DATA_KEY)) ? object.getJSONObject(Constants.DATA_KEY) : new JSONObject();
            String requestType = object.optString(Constants.REQUEST_TYPE_KEY, Constants.EMPTY_STRING);
            JSONObject response = engine.init(request, requestType, bearerToken);
            logger.info("REQUEST RESPONSE=> {}", response);

            return Response.ok()
                    .entity(response.toString())
                    .build();
        }
        catch (JSONException | IOException ex)
        {
            logger.error("ERROR=> {} | {}", ex.getClass().getSimpleName(), ex);
            JSONObject response = Util.buildResponse(Constants.FAILURE_STATUS_CODE, ResultIds.REQUEST_FAILED.name(), "Invalid request payload format");
            return Response.ok()
                    .entity(response.toString())
                    .build();
        }
    }

}
