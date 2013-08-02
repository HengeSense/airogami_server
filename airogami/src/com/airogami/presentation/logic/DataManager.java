package com.airogami.presentation.logic;

import java.io.IOException;
import java.io.InputStream;
import java.security.SignatureException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import com.airogami.presentation.utilities.JSONUtils;
import com.airogami.presentation.utilities.SigGenerator;
import com.amazonaws.util.json.JSONArray;
import com.amazonaws.util.json.JSONException;
import com.amazonaws.util.json.JSONObject;

public class DataManager {
	
	public final static String Method_Upload = "upload";
	public final static String Method_Download = "download";
	private String accountIconPolicy;
	private final int expiration = 100 * 3600 * 1000;// 1h
	private final SimpleDateFormat sdf = new SimpleDateFormat(
			"yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");	
	
	public DataManager(){
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
	
	public String accountIcon(String method, long accoutId) {
		Date date = new Date(System.currentTimeMillis() + expiration);
		String str = sdf.format(date);
		JSONObject json = new JSONObject();
		JSONUtils.putOKStatus(json);
		try {
			JSONObject jsonObj = new JSONObject(accountIconPolicy);
			jsonObj.put("expiration", str);
			
			JSONArray jsonArray = jsonObj.getJSONArray("conditions");
			jsonArray.optJSONObject(0).put("key", "accounts/" + accoutId + "/account/icon.jpg");
			String[] result = SigGenerator.instance.calculateRFC2104HMAC(
					jsonObj.toString());
			json.put("policy", result[0]);
			json.put("signature", result[1]);
			json.put("conditions",jsonArray);
		} catch (SignatureException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		} 
		return json.toString();
	}
}
