package com.airogami.presentation.logic;

import java.io.IOException;
import java.io.InputStream;
import java.security.SignatureException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

import com.airogami.application.ServiceUtils;
import com.airogami.application.exception.ApplicationException;
import com.airogami.common.constants.MessageConstants;
import com.airogami.exception.AirogamiException;
import com.airogami.presentation.utilities.JSONUtils;
import com.airogami.presentation.utilities.SigGenerator;
import com.amazonaws.util.json.JSONArray;
import com.amazonaws.util.json.JSONException;
import com.amazonaws.util.json.JSONObject;

public class DataManager {

	public final static String Method_Upload = "upload";
	public final static String Method_Download = "download";
	private final static String AWSAccessKeyId = "AKIAI72HDWKODEAAUIQQ";
	private final static String msgDataTypes[] = {".audio", ".jpg"};
	private String accountIconPolicy;
	private final int expiration = 15 * 60 * 1000;// 15min
	private final SimpleDateFormat sdf = new SimpleDateFormat(
			"yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

	public DataManager() {
		String filename = "account_icon_policy.txt";
		InputStream is = this.getClass().getResourceAsStream(filename);
		Scanner scanner = new Scanner(is);
		scanner.useDelimiter("\\Z");
		accountIconPolicy = scanner.next();
		scanner.close();
		try {
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// "accounts/reversed accountId/account/..."
	public String accountIcon(int accountId) {
		JSONObject json = new JSONObject();
		try {
			Date date = new Date(System.currentTimeMillis() + expiration);
			String str = sdf.format(date);
			//
			String types[] = { "medium", "small" };
			StringBuffer buffer = new StringBuffer("" + accountId);
			buffer.reverse();
			String reverse = buffer.toString();
			//
			String keys[] = {
					"accounts/" + reverse + "/account/icon-medium.jpg",
					"accounts/" + reverse + "/account/icon.jpg" };
			for (int i = 0; i < keys.length; ++i) {
				JSONObject jsonObj = new JSONObject(accountIconPolicy);
				jsonObj.put("expiration", str);

				JSONArray jsonArray = jsonObj.getJSONArray("conditions");
				jsonArray.optJSONObject(0).put("key", keys[i]);
				String acl = jsonArray.optJSONObject(1).getString("acl");
				String[] data = SigGenerator.instance
						.calculateRFC2104HMAC(jsonObj.toString());
				JSONObject result = new JSONObject();
				result.put("policy", data[0]);
				result.put("signature", data[1]);
				result.put("key", keys[i]);
				result.put("acl", acl);
				result.put("AWSAccessKeyId", AWSAccessKeyId);
				json.put(types[i], result);
			}
		} catch (SignatureException e) {
			// e.printStackTrace();
		} catch (JSONException e) {
			// e.printStackTrace();
		}
		return json.toString();
	}

	// "accounts/reversed accountId/messagedata/reversed msgDataInc ..."
	public Map<String, Object> messageData(int accountId, short type) throws AirogamiException {
		Map<String, Object> result = new TreeMap<String, Object>();
		try {
			Integer msgDataInc = ServiceUtils.dataService
					.getMsgDataInc(accountId);
			if (msgDataInc != null) {
				Date date = new Date(System.currentTimeMillis() + expiration);
				String str = sdf.format(date);
				//
				StringBuffer buffer = new StringBuffer("" + accountId);
				buffer.reverse();
				String key = "accounts/" + buffer.toString() + "/messagedata/";
				//
				buffer = new StringBuffer("" + msgDataInc);
				buffer.reverse();
				key += buffer.toString() + msgDataTypes[type - MessageConstants.MessageTypeAudio];
				//
				JSONObject jsonObj = new JSONObject(accountIconPolicy);
				jsonObj.put("expiration", str);
				//
				JSONArray jsonArray = jsonObj.getJSONArray("conditions");
				jsonArray.optJSONObject(0).put("key", key);
				String acl = jsonArray.optJSONObject(1).getString("acl");
				String[] data = SigGenerator.instance
						.calculateRFC2104HMAC(jsonObj.toString());
				//
				JSONObject token = new JSONObject();
				token.put("policy", data[0]);
				token.put("signature", data[1]);
				token.put("key", key);
				token.put("acl", acl);
				token.put("AWSAccessKeyId", AWSAccessKeyId);

				result.put("msgDataInc", msgDataInc);
				result.put("token", token.toString());
			}

		} catch (ApplicationException re) {
			throw new AirogamiException(
					AirogamiException.Application_Exception_Status,
					AirogamiException.Application_Exception_Message);
		} catch (SignatureException e) {
			result.put("error", "others");
		} catch (JSONException e) {
			result.put("error", "others");
		}
		return result;
	}

	// "chains/reversed chainId/reversed accountId ..."
	public Map<String, Object> chainData(int accountId, long chainId, short type) throws AirogamiException {
		Map<String, Object> result = new TreeMap<String, Object>();
		try {
			boolean exists = ServiceUtils.chainService
					.chainExists(accountId, chainId);
			if (exists) {
				Date date = new Date(System.currentTimeMillis() + expiration);
				String str = sdf.format(date);
				//
				StringBuffer buffer = new StringBuffer("" + chainId);
				buffer.reverse();
				String key = "chains/" + buffer.toString() + "/";
				//
				buffer = new StringBuffer("" + accountId);
				buffer.reverse();
				key += buffer.toString() + msgDataTypes[type - MessageConstants.MessageTypeAudio];
				//
				JSONObject jsonObj = new JSONObject(accountIconPolicy);
				jsonObj.put("expiration", str);
				//
				JSONArray jsonArray = jsonObj.getJSONArray("conditions");
				jsonArray.optJSONObject(0).put("key", key);
				String acl = jsonArray.optJSONObject(1).getString("acl");
				String[] data = SigGenerator.instance
						.calculateRFC2104HMAC(jsonObj.toString());
				//
				JSONObject token = new JSONObject();
				token.put("policy", data[0]);
				token.put("signature", data[1]);
				token.put("key", key);
				token.put("acl", acl);
				token.put("AWSAccessKeyId", AWSAccessKeyId);

				result.put("token", token.toString());
			}
			else{
				result.put("error", "none");
			}

		} catch (ApplicationException re) {
			throw new AirogamiException(
					AirogamiException.Application_Exception_Status,
					AirogamiException.Application_Exception_Message);
		} catch (SignatureException e) {
			// e.printStackTrace();
			result.put("error", "others");
		} catch (JSONException e) {
			// e.printStackTrace();
			result.put("error", "others");
		}
		return result;
	}
}
