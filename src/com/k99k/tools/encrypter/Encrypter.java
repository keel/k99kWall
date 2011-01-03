package com.k99k.tools.encrypter;

/**
 * @author keel
 * 
 */
//import java.security.SecureRandom;
import javax.crypto.Cipher;
//import javax.crypto.KeyGenerator;
//import javax.crypto.SecretKey;


/**
 * 
 * 使用DES加密与解密,可对String类型进行加密与解密,密文可使用String存储.
 * 
 */

public class Encrypter {
	Cipher ecipher;

	Cipher dcipher;
	
	/**
	 * 用于加解密的key
	 */
	private static final byte[] key = new byte[]{79, 13, 33, -66, -58, 103, 3, -34, -45, 53, 9, 45, 28, -124, 50, -2};

	public Encrypter() throws Exception {
		ecipher = Cipher.getInstance("AES");
		dcipher = Cipher.getInstance("AES");
//		KeyGenerator _generator = KeyGenerator.getInstance("AES");
//		SecureRandom sr = SecureRandom.getInstance("SHA1PRNG"); 
//		sr.setSeed(strKey.getBytes("UTF8"));
//		//_generator.init(new SecureRandom(strKey.getBytes()));
//		_generator.init(128,sr);
//		SecretKey key = _generator.generateKey();
		SKey key2 = new SKey("AES","RAW",key);
		ecipher.init(Cipher.ENCRYPT_MODE, key2);
		dcipher.init(Cipher.DECRYPT_MODE, key2);
	}

	public String encrypt(String str) throws Exception {
		
		//return SimpleCrypto.encrypt(this.key, str);
	
		// Encode the string into bytes using utf-8
		byte[] utf8 = str.getBytes("UTF8");

		// Encrypt
		byte[] enc = ecipher.doFinal(utf8);

		// Encode bytes to base64 to get a string
		return Base64Coder.encode(enc);
	
	}

	public String decrypt(String str) throws Exception {
		//return SimpleCrypto.decrypt(this.key, str);
		
		// Decode base64 to get bytes
		byte[] dec = Base64Coder.decode(str);

		byte[] utf8 = dcipher.doFinal(dec);

		// Decode using utf-8
		return new String(utf8, "UTF8");
		
		
	}

	
}
