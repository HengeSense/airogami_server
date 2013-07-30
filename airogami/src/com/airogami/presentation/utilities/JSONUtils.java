package com.airogami.presentation.utilities;

import java.util.Map;

import com.airogami.exception.AirogamiError;
import com.amazonaws.util.json.JSONException;
import com.amazonaws.util.json.JSONObject;

public class JSONUtils implements JSONConstants {

	public static String statusToJSONString(int status, String msg) {
		return "{\"" + JSONConstants.KEY_STATUS + "\":" + status + ",\""
				+ JSONConstants.KEY_MESSAGE + "\":\"" + msg + "\"}";
	}

	public static String OKJSONString() {
		return '"' + JSONConstants.KEY_STATUS + "\":" + AirogamiError.OK_Status
				+ ",\"" + JSONConstants.KEY_MESSAGE + "\":\""
				+ AirogamiError.OK_Message + '"';

	}
	public static void putStatus(Map<String,Object> map,int status,String message){
		map.put(JSONConstants.KEY_STATUS , status);
		map.put(JSONConstants.KEY_MESSAGE , message);
	}
	
	public static void putStatus(Map<String,Object> map, String message){
		map.put(JSONConstants.KEY_STATUS , AirogamiError.Others_Status);
		map.put(JSONConstants.KEY_MESSAGE , message);
	}
	
	public static void putOKStatus(JSONObject json){
		try {
			json.put(JSONConstants.KEY_STATUS , AirogamiError.OK_Status);
			json.put(JSONConstants.KEY_MESSAGE , AirogamiError.OK_Message);
		} catch (JSONException e) {
			//e.printStackTrace();
		}		
	}
	
	public static void putOKStatus(Map<String,Object> map){
		map.put(JSONConstants.KEY_STATUS , AirogamiError.OK_Status);
		map.put(JSONConstants.KEY_MESSAGE , AirogamiError.OK_Message);
	}

}
