package com.airogami.presentation.logic;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts2.ServletActionContext;
import org.json.JSONException;

import javapns.Push;
import javapns.communication.exceptions.KeystoreException;
import javapns.devices.exceptions.InvalidDeviceTokenFormatException;
import javapns.notification.PushNotificationPayload;
import javapns.notification.transmission.PushQueue;

public class NotificationManager implements Runnable {
	private final int threads = 100;
	private final String keystore = ServletActionContext.getServletContext()
			.getRealPath("WEB-INF/keystore.p12");
	private final String password = "$Airogami2013";
	private final boolean production = false;
	private long internal = 60 * 1000;//60s
	private final int initCapacity = 100000;
	private final int incCapacity = 10000;
	
	private PushQueue queue;
	private User[] users = new User[initCapacity];
	
	public NotificationManager(){
		try {
			queue = Push.queue(keystore, password, production, threads);
		} catch (KeystoreException e) {
			e.printStackTrace();
		}
		new Thread(this).start();
	}
	
	public void addNotification(long accountId){
		PushNotificationPayload payload = PushNotificationPayload.complex();
		User user = getUser(accountId);
		if(user != null){
			String token = user.getClientAgent().getDeviceToken();
			if(token != null){
				try {
					payload.addBadge(1);
					payload.addSound("default");
					payload.addCustomDictionary("type", 1);
					payload.addAlert("test");
					queue.add(payload, token);
					System.out.println("sending token: " + token);
				} catch (JSONException e) {
					e.printStackTrace();
				} catch (InvalidDeviceTokenFormatException e) {
					e.printStackTrace();
				}
			}
		}
        
	}
	
	public User updateUser(long accountId, ClientAgent clientAgent){
		int index = (int) accountId;
		if(index >= users.length){
			adjust(index);
		}
		if(users[index] == null){
			synchronized(this){
				users[index] = new User(accountId, clientAgent);
			}
		}
		else{
			users[index].setClientAgent(clientAgent);
		}
		System.out.println("addUser: " +accountId);
		return users[index];
	}
	
	
	private User getUser(long accountId){
		User[] uu = users;
		if(accountId >= uu.length){
			return null;
		}
		else{
			return uu[(int)accountId];
		}
	}
	
	//accountId >= users.length
	private synchronized void adjust(int index){
		if(index >= users.length){
			int length = incCapacity + index;
			User[] uu = new User[length];
			System.arraycopy(users, 0, uu, 0, users.length);
			users = uu;
		}
		
	}

	@Override
	public void run() {
		while(true){
			try {
				Thread.sleep(internal);
				List<Exception> exceptions = queue.getCriticalExceptions();
				if(exceptions != null && exceptions.size() > 0){
					System.err.println("CriticalExceptions in PushQueue");
				}
				queue.getPushedNotifications();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
