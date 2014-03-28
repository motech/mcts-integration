package mcts.integration.beneficiary.sync.util;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import sun.misc.BASE64Encoder;
import com.sun.crypto.provider.SunJCE;
import java.security.spec.KeySpec;
import java.util.Calendar;

public class Encryption {

	private static String Encrypt(String D) throws InvalidKeyException,
			InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchPaddingException, UnsupportedEncodingException, InvalidKeySpecException, IllegalBlockSizeException, BadPaddingException {
		Cipher ci = getCipher(Cipher.ENCRYPT_MODE);
		byte[] eVal = ci.doFinal(D.getBytes("UTF-8"));
		return new BASE64Encoder().encode(eVal);
	}

	private static Cipher getCipher(int mode) throws InvalidKeyException,
			InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchPaddingException, UnsupportedEncodingException, InvalidKeySpecException {
		Cipher ci = Cipher.getInstance("AES/CBC/PKCS5Padding", new SunJCE());
		byte[] ivector = "1e3f5e2f4e61e798".getBytes("UTF-8"); // as provided
		ci.init(mode, generateKey(), new IvParameterSpec(ivector));
		return ci;
	}

	static String SALT_STR = "!Y@NM@NG@L";
	static String key = "MCTS@MOTECH";

	private static Key generateKey() throws NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeySpecException  {
		SecretKeyFactory factory = SecretKeyFactory
				.getInstance("PBKDF2WithHmacSHA1");
		char[] Key = key.toCharArray();
		byte[] salt = SALT_STR.getBytes("UTF-8");
		java.security.spec.KeySpec spec = new PBEKeySpec(Key, salt, 65536, 128);
		SecretKey tmp = factory.generateSecret(spec);
		byte[] encoded = tmp.getEncoded();
		return new SecretKeySpec(encoded, "AES");

	}

	public static String EncryptWithTimeInSeconds(String password)
			 {
		try {
		long time = Calendar.getInstance().getTimeInMillis();
		long timeInSeconds = time / 1000;

		String data = password + timeInSeconds;

		return Encrypt(data);
		} catch (Exception e) {
			throw new RuntimeException("Password encryption failed");
		}

	}

}
