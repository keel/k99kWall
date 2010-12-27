package com.k99k.tools.encrypter;

import javax.crypto.SecretKey;

public class SKey implements SecretKey {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String algorithm;
	
	private byte[] enc;
	
	private String format;

	public String getAlgorithm() {
		return this.algorithm;
	}

	public byte[] getEncoded() {
		return this.enc;
	}

	public String getFormat() {
		return this.format;
	}

	/**
	 * @return the enc
	 */
	public final byte[] getEnc() {
		return enc;
	}

	/**
	 * @param enc the enc to set
	 */
	public final void setEnc(byte[] enc) {
		this.enc = enc;
	}

	/**
	 * @param algorithm the algorithm to set
	 */
	public final void setAlgorithm(String algorithm) {
		this.algorithm = algorithm;
	}

	/**
	 * @param format the format to set
	 */
	public final void setFormat(String format) {
		this.format = format;
	}

}
