package org.motechproject.mcts.utils;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Calendar;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.motechproject.mcts.integration.exception.ApplicationErrors;
import org.motechproject.mcts.integration.exception.BeneficiaryException;

public final class Encryption {

    private Encryption() {

    }

    private static String unicode = "UTF-8";

    private static String encrypt(String d) throws InvalidKeyException,
            InvalidAlgorithmParameterException, NoSuchAlgorithmException,
            NoSuchPaddingException, UnsupportedEncodingException,
            InvalidKeySpecException, IllegalBlockSizeException,
            BadPaddingException {
        Cipher ci = getCipher(Cipher.ENCRYPT_MODE);
        byte[] eVal = ci.doFinal(d.getBytes(unicode));
        byte[] encodedBytes = Base64.encodeBase64(eVal);
        return new String(encodedBytes, "UTF-8");
    }

    private static Cipher getCipher(int mode) throws InvalidKeyException,
            InvalidAlgorithmParameterException, NoSuchAlgorithmException,
            NoSuchPaddingException, UnsupportedEncodingException,
            InvalidKeySpecException {
        Cipher ci = Cipher.getInstance("AES/CBC/PKCS5Padding");
        byte[] ivector = "1e3f5e2f4e61e798".getBytes(unicode); // as provided
        ci.init(mode, generateKey(), new IvParameterSpec(ivector));
        return ci;
    }

    private static final String SALT_STR = "!Y@NM@NG@L";
    private static final String KEY = "MCTS@MOTECH";

    private static Key generateKey() throws NoSuchAlgorithmException,
            UnsupportedEncodingException, InvalidKeySpecException {
        SecretKeyFactory factory = SecretKeyFactory
                .getInstance("PBKDF2WithHmacSHA1");
        char[] key = KEY.toCharArray();
        byte[] salt = SALT_STR.getBytes(unicode);
        java.security.spec.KeySpec spec = new PBEKeySpec(key, salt, MctsConstants.ITERATION_COUNT, MctsConstants.KEY_LENGTH);
        SecretKey tmp = factory.generateSecret(spec);
        byte[] encoded = tmp.getEncoded();
        return new SecretKeySpec(encoded, "AES");

    }

    public static String encryptWithTimeInSeconds(String password) {
        try {
            long time = Calendar.getInstance().getTimeInMillis();
            long timeInSeconds = time / MctsConstants.MILLISECOND_CONVERTOR;

            String data = password + timeInSeconds;

            return encrypt(data);
        } catch (Exception e) {
            throw new BeneficiaryException(
                    ApplicationErrors.RUN_TIME_EXCEPTION, e, e.getMessage());
        }

    }

}
