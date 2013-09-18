package com.airogami.presentation.notification;

import java.util.Arrays;

import javapns.notification.PushNotificationPayload;

import org.json.JSONException;

import com.airogami.presentation.logic.ClientAgent;

//like plane message
public class LPMNotification extends Notification{
	
	private String name;
	
	public LPMNotification(Object accountIds, String name){
		super(accountIds);
		this.name = name;
		type = TypeLikedPlaneMessage;
	}
	
	public PushNotificationPayload buildIOSPayload(){
		PushNotificationPayload payload = PushNotificationPayload.complex();
		try {
			payload.addCustomDictionary("type", type);
			payload.addCustomAlertLocKey(LocKeys[type]);
			payload.addCustomAlertLocArgs(Arrays.asList(name));
			payload.addSound("default");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return payload;
	}
}
