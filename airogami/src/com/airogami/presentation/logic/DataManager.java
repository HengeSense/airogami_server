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
	private String text;
	private final int expiration = 100 * 3600 * 1000;// 1h
	private final SimpleDateFormat sdf = new SimpleDateFormat(
			"yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
	private final String key = "+9/DARod2Gr8npmNqEAPY6UGjxLW5hcFUAH68DnK";
	
	public DataManager(){
		String filename = "policy.txt";
		InputStream is = this.getClass().getResourceAsStream(filename);
		Scanner scanner = new Scanner(is);  
		scanner.useDelimiter("\\Z");
		text = scanner.next();
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
			JSONObject jsonObj = new JSONObject(text);
			// generate a new timestamp with one extra hour(credential expirese
			// in one hour...)
			jsonObj.put("expiration", str);
			// modify upload directory based on username
			JSONArray jsonArray = jsonObj.getJSONArray("conditions");
			jsonArray.optJSONArray(0).put(2,
					"accounts/" + accoutId + "/account/icon.jpg");
			String[] result = SigGenerator.calculateRFC2104HMAC(
					jsonObj.toString(), key);
			for(int i = 0; i < jsonArray.length(); ++i){
				Object obj = jsonArray.get(i);
				if(obj instanceof JSONArray){
					JSONObject jo = new JSONObject();
					JSONArray ja = (JSONArray)obj;
					jo.put((ja).getString(1).substring(1), ja.getString(2));
					jsonArray.put(i, jo);
				}
			}			
			json.put("policy", result[0]);
			json.put("signature", result[1]);
			json.put("conditions", jsonArray);
		} catch (SignatureException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		} 
		return json.toString();
	}
}
