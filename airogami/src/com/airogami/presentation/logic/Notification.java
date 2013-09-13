package com.airogami.presentation.logic;

import java.util.Arrays;
import java.util.List;

import org.json.JSONException;

import javapns.notification.PushNotificationPayload;

public class Notification {

	public static final int TypeReceivedPlane = 1;
	public static final int TypeReceivedChain = 2;
	public static final int TypeReceivedPlaneMessage = 3;
	public static final int TypeReceivedChainMessage = 4;
	public static final int TypeLikedPlaneMessage = 5;
	
	public static final String LocKeyReceivedPlane = "LKRP";
	public static final String LocKeyReceivedChain = "LKRC";
	public static final String LocKeyReceivedPlaneMessage = "LKRPM";
	public static final String LocKeyReceivedChainMessage = "LKRCM";
	public static final String LocKeyLikedPlaneMessage = "LKLPM";

	private int type;
	private Object accountIds;
	private String message;
	private PushNotificationPayload payload = PushNotificationPayload.complex();
	
	private Notification(){
	}
	
	public static Notification receivedChain(Long accountId){
		Notification notification = new Notification();
		notification.type = TypeReceivedChain;
		notification.accountIds = accountId;
		try {
			notification.payload.addCustomDictionary("type", TypeReceivedChain);
			notification.payload.addCustomAlertLocKey(LocKeyReceivedChain);
			//notification.payload.addCustomAlertLocArgs(args);
			notification.payload.addSound("default");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return notification;
	}
	
	public static Notification receivedPlane(Long accountId){
		Notification notification = new Notification();
		notification.type = TypeReceivedPlane;
		notification.accountIds = accountId;
		try {
			notification.payload.addCustomDictionary("type", TypeReceivedPlane);
			notification.payload.addCustomAlertLocKey(LocKeyReceivedPlane);
			//notification.payload.addCustomAlertLocArgs(args);
			notification.payload.addSound("default");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return notification;
	}
	
	public static Notification receivedPlaneMessage(Long accountId, String name, String content){
		Notification notification = new Notification();
		notification.type = TypeReceivedPlaneMessage;
		notification.accountIds = accountId;
		try {
			notification.payload.addCustomDictionary("type", TypeReceivedPlaneMessage);
			notification.payload.addCustomAlertLocKey(LocKeyReceivedPlaneMessage);
			notification.payload.addCustomAlertLocArgs(Arrays.asList(name, content));
			notification.payload.addSound("default");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return notification;
	}
	
	public static Notification likedPlaneMessage(Long accountId, String name){
		Notification notification = new Notification();
		notification.type = TypeLikedPlaneMessage;
		notification.accountIds = accountId;
		try {
			notification.payload.addCustomDictionary("type", TypeLikedPlaneMessage);
			notification.payload.addCustomAlertLocKey(LocKeyLikedPlaneMessage);
			notification.payload.addCustomAlertLocArgs(Arrays.asList(name));
			notification.payload.addSound("default");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return notification;
	}
	
	public static Notification receivedChainMessage(List<Long> accountIds, String name, String content){
		Notification notification = new Notification();
		notification.type = TypeReceivedChainMessage;
		notification.accountIds = accountIds;
		try {
			notification.payload.addCustomDictionary("type", TypeReceivedChainMessage);
			notification.payload.addCustomAlertLocKey(LocKeyReceivedChainMessage);
			notification.payload.addCustomAlertLocArgs(Arrays.asList(name, content));
			notification.payload.addSound("default");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return notification;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Object getAccountIds() {
		return accountIds;
	}

	public void setAccountIds(Object accountIds) {
		this.accountIds = accountIds;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public PushNotificationPayload getPayload() {
		return payload;
	}

	public void setPayload(PushNotificationPayload payload) {
		this.payload = payload;
	}
	
	
}
