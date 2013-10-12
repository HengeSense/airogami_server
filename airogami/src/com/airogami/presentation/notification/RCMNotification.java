package com.airogami.presentation.notification;

import java.util.Arrays;

import javapns.notification.PushNotificationPayload;

import org.json.JSONException;

import com.airogami.common.ClientAgent;
import com.airogami.common.NotifiedInfo;

//received chain message
public class RCMNotification extends Notification{
	
	public RCMNotification(NotifiedInfo notifiedInfos){
		super(notifiedInfos);
		type = TypeReceivedChainMessage;
	}
	
	public PushNotificationPayload buildIOSPayload(){
		PushNotificationPayload payload = PushNotificationPayload.complex();
		try {
			payload.addCustomDictionary("type", type);
			payload.addCustomAlertLocKey(LocKeys[type]);
			payload.addCustomAlertLocArgs(Arrays.asList(notifiedInfos.getName(), notifiedInfos.getContent()));
			payload.addSound("default");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return payload;
	}
}
