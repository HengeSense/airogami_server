package com.airogami.presentation.logic;

import java.io.IOException;
import java.io.InputStream;
import java.security.SignatureException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import javax.servlet.http.HttpServletRequest;

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
	
	public String accountIcon(long accoutId) {
		Date date = new Date(System.currentTimeMillis() + expiration);
		String str = sdf.format(date);
		JSONObject json = new JSONObject();
		JSONUtils.putOKStatus(json);
		try {
			JSONObject results = new JSONObject();
			String types[] = {"medium", "small"};
			String keys[] = {"accounts/" + accoutId + "/account/icon-medium.jpg",
					"accounts/" + accoutId + "/account/icon.jpg"};
			for(int i = 0; i < keys.length; ++i){
				JSONObject jsonObj = new JSONObject(accountIconPolicy);
				jsonObj.put("expiration", str);
				
				JSONArray jsonArray = jsonObj.getJSONArray("conditions");				
				jsonArray.optJSONObject(0).put("key", keys[i]);
				String acl = jsonArray.optJSONObject(1).getString("acl");
				String[] data = SigGenerator.instance.calculateRFC2104HMAC(
						jsonObj.toString());
				JSONObject result = new JSONObject();
				result.put("policy", data[0]);
				result.put("signature", data[1]);
				result.put("key",keys[i]);
				result.put("acl",acl);
				result.put("AWSAccessKeyId", "AKIAIZ2JO6JPGKMSGQZA");
				results.put(types[i], result);
			}
			json.put("result", results); 
		} catch (SignatureException e) {
			//e.printStackTrace();
		} catch (JSONException e) {
			//e.printStackTrace();
		} 
		return json.toString();
	}
}
