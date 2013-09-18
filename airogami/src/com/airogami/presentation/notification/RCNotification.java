package com.airogami.presentation.notification;

import javapns.notification.PushNotificationPayload;

import org.json.JSONException;

import com.airogami.presentation.logic.ClientAgent;

//received chain
public class RCNotification extends Notification{
	
	public RCNotification(Object accountIds){
		super(accountIds);
		type = TypeReceivedChain;
	}
	
	public PushNotificationPayload buildIOSPayload(){
		PushNotificationPayload payload = PushNotificationPayload.complex();
		try {
			payload.addCustomDictionary("type", type);
			payload.addCustomAlertLocKey(LocKeys[type]);
			//payload.addCustomAlertLocArgs(args);
			payload.addSound("default");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return payload;
	}
}
