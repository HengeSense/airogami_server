package com.airogami.presentation.notification;

import javapns.notification.PushNotificationPayload;

public abstract class Notification {

	public static final int TypeReceivedPlane = 1;
	public static final int TypeReceivedChain = 2;
	public static final int TypeReceivedPlaneMessage = 3;
	public static final int TypeReceivedChainMessage = 4;
	public static final int TypeLikedPlaneMessage = 5;
	
	protected static final String LocKeys[] = {"", "LKRP", "LKRC", "LKRPM", "LKRCM", "LKLPM"};
	
	protected static final int MessageLength = 50;

	protected int type;
	protected Object accountIds;
	
	public Notification(Object accountIds){
		this.accountIds = accountIds;
	}
	
	public abstract PushNotificationPayload buildIOSPayload();

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
	
}
