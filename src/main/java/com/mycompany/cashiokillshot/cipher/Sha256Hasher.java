package com.mycompany.cashiokillshot.cipher;

import com.mycompany.cashiokillshot.utility.Constants;
import jakarta.enterprise.context.ApplicationScoped;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@ApplicationScoped
public class Sha256Hasher {

    private static final Logger logger= LogManager.getLogger(Sha256Hasher.class);

    public String createHashText(String text){
        try
        {
            if (text.isBlank())
            {
                return "";
            }

            MessageDigest md = MessageDigest.getInstance(Constants.SHA_256_HASH_ALGORITHM);
            byte[] bytes = md.digest(text.getBytes(StandardCharsets.UTF_8));
            return convertBytesToHexadecimal(bytes);
        }
        catch (NoSuchAlgorithmException ex)
        {
            logger.error("ERROR=> {} | {}", ex.getClass().getSimpleName(), ex.getMessage());
            return "";
        }

    }
    private String convertBytesToHexadecimal(final byte[] messageDigest)
    {
        BigInteger bigint = new BigInteger(1, messageDigest);
        String hexText = bigint.toString(16);

        while (hexText.length() < 32)
        {
            hexText = "0".concat(hexText);
        }

        return hexText;
    }
}
