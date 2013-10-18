//Silent Notification

package com.airogami.presentation.notification;

import javapns.notification.PushNotificationPayload;

import org.json.JSONException;

import com.airogami.common.notification.NotifiedInfo;
import com.airogami.common.notification.SilentNotifiedInfo;

// received plane
public class SNotification extends Notification{
	
	public SNotification(NotifiedInfo notifiedInfos){
		super(notifiedInfos);
		type = TypeSilent;
	}
	
	public SNotification(Integer accountId){
		this.notifiedInfos = new SilentNotifiedInfo(accountId); 
		type = TypeSilent;
	}
	
	public PushNotificationPayload buildIOSPayload(){
		PushNotificationPayload payload = PushNotificationPayload.complex();
		try {
			payload.addCustomDictionary("type", type);
			payload.addCustomDictionary("content-available", 1);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return payload;
	}
}
