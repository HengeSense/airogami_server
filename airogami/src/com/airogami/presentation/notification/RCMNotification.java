package com.airogami.presentation.notification;

import java.util.Arrays;

import javapns.notification.PushNotificationPayload;

import org.json.JSONException;

import com.airogami.presentation.logic.ClientAgent;

//received chain message
public class RCMNotification extends Notification{
	
	private String name, content;
	
	public RCMNotification(Object accountIds, String name, String content){
		super(accountIds);
		this.name = name;
		if(content.length() > MessageLength){
			StringBuffer stringBuffer = new StringBuffer(MessageLength);
			stringBuffer.append(content.substring(0, MessageLength - 3));
			stringBuffer.append("...");
			content = stringBuffer.toString();
		}
		this.content = content;
		type = TypeReceivedChainMessage;
	}
	
	public PushNotificationPayload buildIOSPayload(){
		PushNotificationPayload payload = PushNotificationPayload.complex();
		try {
			payload.addCustomDictionary("type", type);
			payload.addCustomAlertLocKey(LocKeys[type]);
			payload.addCustomAlertLocArgs(Arrays.asList(name, content));
			payload.addSound("default");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return payload;
	}
}
