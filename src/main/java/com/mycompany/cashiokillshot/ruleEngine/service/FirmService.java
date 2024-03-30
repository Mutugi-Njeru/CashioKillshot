package com.mycompany.cashiokillshot.ruleEngine.service;

import com.mycompany.cashiokillshot.dao.FirmsDao;
import com.mycompany.cashiokillshot.models.Firm;
import com.mycompany.cashiokillshot.models.FirmSignupRequest;
import com.mycompany.cashiokillshot.records.ServiceResponder;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import java.util.Optional;

@ApplicationScoped
public class FirmService {
    @Inject
    FirmsDao firmsDao;
    private static final Logger logger= LogManager.getLogger(FirmService.class);

    public ServiceResponder registerFirm(FirmSignupRequest firmSignup){
        int firmId= firmsDao.registerFirm(firmSignup);

        if (firmId>0){
            boolean isFirmContactsSaved=saveFirmContacts(firmId, firmSignup);
            boolean isFirmProductCreated=createFirmDeductionPlan(1, firmId);

            return (isFirmContactsSaved && isFirmProductCreated)
                    ? new ServiceResponder(true, "Registration successful")
                    :new ServiceResponder(false, "Registration failed");
        }
        else {
            return new ServiceResponder(false, "could not register firm");
        }

    }

    private boolean saveFirmContacts(int firmId, FirmSignupRequest firmSignupRequest){
        boolean isSaved=firmsDao.saveFirmContacts(firmId, firmSignupRequest);
        if (!isSaved){
            logger.info("Failed to save firm contacts | firmId=> {} | contacts=> {}", firmId, firmSignupRequest.getContacts());
        }
        return isSaved;
    }

    private boolean createFirmDeductionPlan(int deductionPlanId, int firmId){
        boolean isCreated=firmsDao.createFirmDeductionPlan(deductionPlanId, firmId);

        if (!isCreated){
            logger.info("Failed to create firm deduction plan | firmId=> {} | firmDeductionPlanId=> {}", firmId, deductionPlanId);
        }
        return isCreated;
    }
    

    public ServiceResponder getFirmDetails(int firmId){
        Optional<Firm> optionalFirm = Optional.ofNullable(firmsDao.getFirm(firmId));
        ServiceResponder message = null;

        if (!optionalFirm.isEmpty())
        {
            Firm firmModel = optionalFirm.get();
            if (!firmModel.getToJson().isEmpty())
            {
                message = new ServiceResponder(true, firmModel.getToJson());
            }
            else
            {
                message = new ServiceResponder(false, new JSONObject().put("data", "Firm not Found"));
            }

        }
        else
        {
            message = new ServiceResponder(false, new JSONObject().put("data", "Firm not Found"));

        }
        return message;
    }

    public ServiceResponder getFirms(JSONObject object){
        int lastRecordId= object.optInt("lastRecordId", 0);
        JSONObject firms=firmsDao.getFirms(lastRecordId);

        return (!firms.isEmpty())
                ?new ServiceResponder(true, firms)
                :new ServiceResponder(false, "invalid lastRecordId");
    }
}
