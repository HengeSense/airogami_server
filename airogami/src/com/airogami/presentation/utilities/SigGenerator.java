package com.airogami.presentation.utilities;

import org.apache.commons.codec.binary.Base64;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;

public class SigGenerator {

	private final String key = "bwU6lfNpxuzj0fI1SeDgLvE09JjPpqJrCTKlyqRW";
	private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";
	private SecretKeySpec signingKey;
	private Mac mac;	
	public static final SigGenerator instance = new SigGenerator();

	private SigGenerator(){
		try {
			// get an hmac_sha1 key from the raw key bytes
			signingKey = new SecretKeySpec(key.getBytes("UTF-8"),
					HMAC_SHA1_ALGORITHM);
			// get an hmac_sha1 Mac instance and initialize with the signing key
			mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
			mac.init(signingKey);
		} catch (UnsupportedEncodingException e) {
		} catch (NoSuchAlgorithmException e) {
		} catch (InvalidKeyException e) {
		}
	}

	/**
	 * Computes RFC 2104-compliant HMAC signature. * @param data The data to be
	 * signed.
	 * 
	 * @param key
	 *            The signing key.
	 * @return The Base64-encoded RFC 2104-compliant HMAC signature.
	 * @throws java.security.SignatureException
	 *             when signature generation fails
	 */
	public String[] calculateRFC2104HMAC(String data)
			throws java.security.SignatureException {
		String[] result = { "policy", "signature" };
		try {
			result[0] = new String(Base64.encodeBase64(data.getBytes("UTF-8")))
					.replaceAll("\n", "").replaceAll("\r", "");
			// compute the hmac on input data bytes
			byte[] rawHmac = mac.doFinal(result[0].getBytes("UTF-8"));
			// base64-encode the hmac
			result[1] = new String(Base64.encodeBase64(rawHmac));
		} catch (Exception e) {
			throw new SignatureException("Failed to generate HMAC : "
					+ e.getMessage());
		}
		return result;
	}
}