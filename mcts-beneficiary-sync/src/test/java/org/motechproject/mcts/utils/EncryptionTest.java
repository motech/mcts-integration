package org.motechproject.mcts.utils;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
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
import org.junit.Test;
import org.motechproject.mcts.integration.exception.BeneficiaryException;

public class EncryptionTest {
	
	public static String DecryptString(String encryptedString)
			throws InvalidKeyException, InvalidAlgorithmParameterException,
			NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidKeySpecException, IllegalBlockSizeException,
			BadPaddingException, IOException {
		byte[] decoded = Base64.decodeBase64(encryptedString.getBytes());
		Cipher ci = shouldGetCipher(Cipher.DECRYPT_MODE);
		byte[] eVal = ci.doFinal(decoded);
		String s = new String(eVal, "US-ASCII");
		return s;
	}
	
	@Test
	public void shouldTestDecryprtion() throws InvalidKeyException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException, IllegalBlockSizeException, BadPaddingException, IOException, BeneficiaryException
	{
		String password = "motech@motech";
		String encrypted = Encryption.encryptWithTimeInSeconds(password);
		System.out.println("Encrypted Password: " + encrypted);
		String decrypted = DecryptString(encrypted);
		System.out.println("Decrypted password along with timestamp: " + decrypted);
		assertEquals(shouldEncryptWithTimeInSeconds(password).length(), decrypted.length());
		assertEquals(password, decrypted.subSequence(0, password.length()));
	}

	private static Cipher shouldGetCipher(int mode) throws InvalidKeyException,
			InvalidAlgorithmParameterException, NoSuchAlgorithmException,
			NoSuchPaddingException, UnsupportedEncodingException,
			InvalidKeySpecException {
		Cipher ci = Cipher.getInstance("AES/CBC/PKCS5Padding");
		byte[] ivector = "1e3f5e2f4e61e798".getBytes("UTF-8"); // as provided
		ci.init(mode, shouldGenerateKey(), new IvParameterSpec(ivector));
		return ci;
	}

	static String SALT_STR = "!Y@NM@NG@L";
	static String key = "MCTS@MOTECH";

	private static Key shouldGenerateKey() throws NoSuchAlgorithmException,
			UnsupportedEncodingException, InvalidKeySpecException {
		SecretKeyFactory factory = SecretKeyFactory
				.getInstance("PBKDF2WithHmacSHA1");
		char[] Key = key.toCharArray();
		byte[] salt = SALT_STR.getBytes("UTF-8");
		java.security.spec.KeySpec spec = new PBEKeySpec(Key, salt, 65536, 128);
		SecretKey tmp = factory.generateSecret(spec);
		byte[] encoded = tmp.getEncoded();
		return new SecretKeySpec(encoded, "AES");

	}

	public static String shouldEncryptWithTimeInSeconds(String password) {
		try {
			long time = Calendar.getInstance().getTimeInMillis();
			long timeInSeconds = time / 1000;
			String data = password + timeInSeconds;
			return data;
		} catch (Exception e) {
			throw new RuntimeException("Password encryption failed");
		}

	}
}
