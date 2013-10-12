package com.airogami.presentation.notification;

import java.util.Arrays;

import javapns.notification.PushNotificationPayload;

import org.json.JSONException;

import com.airogami.common.ClientAgent;
import com.airogami.common.MessageNotifiedInfo;
import com.airogami.common.NotifiedInfo;

//received plane message
public class RPMNotification extends Notification{
	
	public RPMNotification(NotifiedInfo notifiedInfos){
		super(notifiedInfos);
		type = TypeReceivedPlaneMessage;
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
