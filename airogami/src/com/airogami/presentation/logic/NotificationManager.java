package com.airogami.presentation.logic;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.struts2.ServletActionContext;
import org.json.JSONException;

import javapns.Push;
import javapns.communication.exceptions.CommunicationException;
import javapns.communication.exceptions.KeystoreException;
import javapns.devices.Device;
import javapns.devices.exceptions.InvalidDeviceTokenFormatException;
import javapns.notification.Payload;
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
	//private final long period = 24 * 3600 * 1000;
	private final long period = 10000;
	
	private PushQueue queue;
	private User[] users = new User[initCapacity];
	
	private Timer timer = new Timer(true);
	private TimerTask timerTask;
	
	public NotificationManager(){
		try {
			queue = Push.queue(keystore, password, production, threads);
		} catch (KeystoreException e) {
			e.printStackTrace();
		}
		new Thread(this).start();
		
		timerTask = new TimerTask(){
			@Override
			public void run() {
				try {
					//System.out.println("Feedback");
					List<Device> devices = Push.feedback(keystore, password, production);
					HashSet<String> hashSet = new HashSet<String>(devices.size());
					for (Device device : devices) {
						hashSet.add(device.getToken());
						//System.out.println("Inactive device: " + device.getToken());
					}
					User[] uu = users;
				    for(User user : uu){
				    	if(user != null){
				    		String token = user.getClientAgent().getDeviceToken();
					    	if(hashSet.remove(token)){
					    		user.getClientAgent().setDeviceToken(null);
					    	}
				    	}
				    	
				    }
				} catch (CommunicationException e) {
					e.printStackTrace();
				} catch (KeystoreException e) {
					e.printStackTrace();
				}
				catch(Throwable t){
					t.printStackTrace();
				}
			}
		};
		
		timer.scheduleAtFixedRate(timerTask, period, period);
	}
	
	public void addNotification(Notification notification){
		if(notification.getType() == Notification.TypeReceivedChainMessage){
			for(Long accountId : (List<Long>)notification.getAccountIds()){
				this.sendNotification(accountId, notification.getPayload());
			}
		}
		else{
			Long accountId = (Long) notification.getAccountIds();
			this.sendNotification(accountId, notification.getPayload());
		}
	}
	
	private void sendNotification(long accountId, PushNotificationPayload payload){
		User user = getUser(accountId);
		if(user != null){
			String token = user.getClientAgent().getDeviceToken();
			if(token != null){
				try {
					payload.addBadge(user.getMessagesCount());
					queue.add(payload, token);
					System.out.printf("sending token (%d): " + token + "\n", accountId);
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
