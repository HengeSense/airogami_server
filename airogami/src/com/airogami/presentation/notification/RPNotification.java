package com.airogami.presentation.notification;

import javapns.notification.PushNotificationPayload;

import org.json.JSONException;

import com.airogami.common.NotifiedInfo;
import com.airogami.common.ReceiveNotifiedInfo;
import com.airogami.presentation.logic.ClientAgent;

// received plane
public class RPNotification extends Notification{
	
	public RPNotification(NotifiedInfo notifiedInfos){
		super(notifiedInfos);
		type = TypeReceivedPlane;
	}
	
	public RPNotification(Integer accountId){
		this.notifiedInfos = new ReceiveNotifiedInfo(accountId); 
		type = TypeReceivedPlane;
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
