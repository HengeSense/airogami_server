package com.airogami.presentation.notification;

import java.util.Arrays;

import javapns.notification.PushNotificationPayload;

import org.json.JSONException;

import com.airogami.common.notification.ClientAgent;
import com.airogami.common.notification.MessageNotifiedInfo;
import com.airogami.common.notification.NotifiedInfo;

//received plane message
public class RPIMNotification extends Notification{
	
	public RPIMNotification(NotifiedInfo notifiedInfos){
		super(notifiedInfos);
		type = TypePlaneImageMessage;
	}
	
	public PushNotificationPayload buildIOSPayload(){
		PushNotificationPayload payload = PushNotificationPayload.complex();
		try {
			payload.addCustomDictionary("type", type);
			payload.addCustomAlertLocKey(LocKeys[type]);
			payload.addCustomAlertLocArgs(Arrays.asList(notifiedInfos.getName()));
			payload.addSound("default");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return payload;
	}
}
