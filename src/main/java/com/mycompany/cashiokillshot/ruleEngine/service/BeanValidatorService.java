package com.mycompany.cashiokillshot.ruleEngine.service;

import com.mycompany.cashiokillshot.records.BeanValidator;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ValidatorFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;

import java.util.Set;

@ApplicationScoped
public class BeanValidatorService {

    @Inject
    private ValidatorFactory validatorFactory;
    private static final Logger logger = LogManager.getLogger(BeanValidatorService.class);

    public BeanValidator validate(Object object)
    {
        JSONArray violations = new JSONArray();
        Set<ConstraintViolation<Object>> beanViolations = validatorFactory.getValidator().validate(object);

        if (!beanViolations.isEmpty())
        {
            beanViolations.forEach(violation -> violations.put(violation.getMessage()));
            logger.error("MESSAGE=> Payload validation failed=> " + violations);

            return new BeanValidator(false, "Invalid payload parameters", violations);
        }
        else
        {
            logger.info("MESSAGE=> Payload validation successful");

            return new BeanValidator(true, "validation successful", violations);
        }
    }
}
