package com.airogami.presentation.notification;

import com.airogami.common.notification.NotifiedInfo;

import javapns.notification.PushNotificationPayload;

public abstract class Notification {

	public static final int TypeSilent = 0;
	public static final int TypeReceivedPlane = 1;
	public static final int TypeReceivedChain = 2;
	public static final int TypePlaneMessage = 3;
	public static final int TypePlaneImageMessage = 4;
	public static final int TypeChainMessage = 5;
	public static final int TypeChainImageMessage = 6;
	public static final int TypeLikedPlaneMessage = 7;
	
	protected static final String LocKeys[] = {"", "LKRP", "LKRC", "LKRPM", "LKRPIM", "LKRCM", "LKRCIM", "LKLPM"};

	protected int type;
	protected NotifiedInfo notifiedInfos;
	
	public Notification(){
	}
	
	public Notification(NotifiedInfo notifiedInfos){
		this.notifiedInfos = notifiedInfos;
	}
	
	public abstract PushNotificationPayload buildIOSPayload();

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public NotifiedInfo getNotifiedInfos() {
		return notifiedInfos;
	}

	public void setNotifiedInfos(NotifiedInfo notifiedInfos) {
		this.notifiedInfos = notifiedInfos;
	}
	
}
