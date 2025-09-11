package com.example.demo.utils.securityUtils;

import com.example.demo.utils.customexceptions.TokenDecryptionException;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.MessageDigest;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Base64;

@Component
public class AESUtils {
    private final String ENCRYPTION_KEY;
    private final String ENCRYPTION_IV;

    public AESUtils(
            @Value("${app.aesEncryptionKey}") final String ENCRYPTION_KEY,
            @Value("${app.aesEncryptionIV}") final String ENCRYPTION_IV
    ) {

        this.ENCRYPTION_KEY = ENCRYPTION_KEY;
        this.ENCRYPTION_IV = ENCRYPTION_IV;
    }

    @SneakyThrows
    public String encrypt(String strToEncrypt)
    {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, makeKey(), makeIv());
        return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
    }

    public String decrypt(String strToDecrypt) throws TokenDecryptionException {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, makeKey(), makeIv());
            return  new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
        } catch (Exception e) {
            throw new TokenDecryptionException("can't decrypt token! : " + e.getCause());
        }
    }

    @SneakyThrows
    private AlgorithmParameterSpec makeIv(){
        return new IvParameterSpec(ENCRYPTION_IV.getBytes("UTF-8"));
    }

    @SneakyThrows
    private Key makeKey(){
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] key = md.digest(ENCRYPTION_KEY.getBytes("UTF-8"));
        return new SecretKeySpec(key, "AES");
    }

}
