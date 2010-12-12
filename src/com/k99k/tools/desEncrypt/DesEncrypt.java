package com.k99k.tools.desEncrypt;

/**
 * @author keel
 * 
 */
import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

/**
 * 
 * 使用DES加密与解密,可对String类型进行加密与解密,密文可使用String存储.
 * 
 */

public class DesEncrypt {
	Cipher ecipher;

	Cipher dcipher;

	public DesEncrypt(String strKey) throws Exception {
		ecipher = Cipher.getInstance("DES");
		dcipher = Cipher.getInstance("DES");
		KeyGenerator _generator = KeyGenerator.getInstance("DES");
		_generator.init(new SecureRandom(strKey.getBytes()));
		SecretKey key = _generator.generateKey();
		ecipher.init(Cipher.ENCRYPT_MODE, key);
		dcipher.init(Cipher.DECRYPT_MODE, key);
	}

	public String encrypt(String str) throws Exception {
		// Encode the string into bytes using utf-8
		byte[] utf8 = str.getBytes("UTF8");

		// Encrypt
		byte[] enc = ecipher.doFinal(utf8);

		// Encode bytes to base64 to get a string
		return new BASE64Encoder().encode(enc);
	}

	public String decrypt(String str) throws Exception {
		// Decode base64 to get bytes
		byte[] dec = new BASE64Decoder().decodeBuffer(str);

		byte[] utf8 = dcipher.doFinal(dec);

		// Decode using utf-8
		return new String(utf8, "UTF8");
	}

}
