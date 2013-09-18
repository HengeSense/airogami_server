package com.airogami.presentation.logic;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.DelayQueue;

import org.apache.struts2.ServletActionContext;
import org.json.JSONException;

import com.airogami.application.Airogami;
import com.airogami.presentation.notification.AppleNotification;
import com.airogami.presentation.notification.Notification;

import javapns.Push;
import javapns.communication.exceptions.CommunicationException;
import javapns.communication.exceptions.KeystoreException;
import javapns.devices.Device;
import javapns.devices.exceptions.InvalidDeviceTokenFormatException;
import javapns.notification.Payload;
import javapns.notification.PushNotificationPayload;
import javapns.notification.PushedNotification;
import javapns.notification.PushedNotifications;
import javapns.notification.transmission.PushQueue;

public class NotificationManager extends Thread{
	
	private AppleNotification appleNotification = new AppleNotification();
	
	private final int queueCapacity = 10000;
	private ArrayBlockingQueue<Notification> notificationQueque = new ArrayBlockingQueue<Notification>(queueCapacity);
	
	public NotificationManager() {
		this.start();
	}
	
	@Override
	public void run() {
		while(true){
			try{
				Notification notification = notificationQueque.take();
				if (notification.getType() == Notification.TypeReceivedChainMessage) {
					for (Long accountId : (List<Long>) notification.getAccountIds()) {
						this.sendNotification(accountId, notification);
					}
				} else {
					Long accountId = (Long) notification.getAccountIds();
					this.sendNotification(accountId, notification);
				}
			}
			catch(Throwable t){
				t.printStackTrace();
			}
		}		
	}
	
	private void sendNotification(Long accountId, Notification notification){
		User user = ManagerUtils.userManager.getUser(accountId);
		ClientAgent clientAgent;
		String token;
		if(user != null && (clientAgent = user.getClientAgent()) != null
				&& (token = clientAgent.getDeviceToken()) != null){
			if(clientAgent.getDeviceType() == ClientAgent.DeviceTypeIOS){
				PushNotificationPayload payload = (PushNotificationPayload)notification.buildIOSPayload();
				appleNotification.sendNotification(accountId, token, user.getMessagesCount(), payload);
			}
		}
	}

	public void addNotification(Notification notification) {
		notificationQueque.offer(notification);
	}

	

}
