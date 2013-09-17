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
import javapns.notification.PushedNotification;
import javapns.notification.PushedNotifications;
import javapns.notification.transmission.PushQueue;

public class NotificationManager implements Runnable {
	private final int threads = 100;
	private final String keystore = ServletActionContext.getServletContext()
			.getRealPath("WEB-INF/keystore.p12");
	private final String password = "$Airogami2013";
	private final boolean production = false;
	private long internal = 60 * 1000;// 60s
	private final long period = 24 * 3600 * 1000;

	private PushQueue queue;

	private Timer timer = new Timer(true);
	private TimerTask timerTask;

	public NotificationManager() {
		try {
			queue = Push.queue(keystore, password, production, threads);
		} catch (KeystoreException e) {
			e.printStackTrace();
		}
		new Thread(this).start();

		timerTask = new TimerTask() {
			@Override
			public void run() {
				try {
					// System.out.println("Feedback");
					List<Device> devices = Push.feedback(keystore, password,
							production);
					HashSet<String> hashSet = new HashSet<String>(
							devices.size());
					for (Device device : devices) {
						hashSet.add(device.getToken());
						// System.out.println("Inactive device: " +
						// device.getToken());
					}
					User[] uu = ManagerUtils.userManager.getUsers();
					for (int index = 0; index < uu.length; ++index) {
						User user = uu[index];
						ClientAgent clientAgent;
						if (user != null
								&& (clientAgent = user.getClientAgent()) != null
								&& clientAgent.getDeviceType() == ClientAgent.DeviceTypeIOS) {
							String token = clientAgent.getDeviceToken();
							if (hashSet.remove(token)) {
								ManagerUtils.userManager.removeUser(user
										.getAccountId());
							}
						}
					}
				} catch (CommunicationException e) {
					e.printStackTrace();
				} catch (KeystoreException e) {
					e.printStackTrace();
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
		};

		timer.scheduleAtFixedRate(timerTask, period, period);
	}

	public void addNotification(Notification notification) {
		if (notification.getType() == Notification.TypeReceivedChainMessage) {
			for (Long accountId : (List<Long>) notification.getAccountIds()) {
				this.sendNotification(accountId, notification.getPayload());
			}
		} else {
			Long accountId = (Long) notification.getAccountIds();
			this.sendNotification(accountId, notification.getPayload());
		}
	}

	private void sendNotification(long accountId,
			PushNotificationPayload payload) {
		User user = ManagerUtils.userManager.getUser(accountId);
		ClientAgent clientAgent; 
		if (user != null && (clientAgent = user.getClientAgent()) != null) {
			String token = clientAgent.getDeviceToken();
			if (token != null) {
				try {
					payload.addBadge(user.getMessagesCount());
					payload.addCustomDictionary("accountId", (int) accountId);
					queue.add(payload, token);
					System.out.printf("sending token (%d): " + token + "\n",
							accountId);
				} catch (JSONException e) {
					e.printStackTrace();
				} catch (InvalidDeviceTokenFormatException e) {
					e.printStackTrace();
				}catch(Throwable t){
					t.printStackTrace();
				}
			}
		}
	}

	// digest PushedNotification
	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(internal);
				List<Exception> exceptions = queue.getCriticalExceptions();
				if (exceptions != null && exceptions.size() > 0) {
					for (Exception e : exceptions) {
						System.err.println("CriticalExceptions: "
								+ e.getLocalizedMessage());
					}

				}
				PushedNotifications pushedNotifications = queue
						.getPushedNotifications();
				pushedNotifications = pushedNotifications
						.getFailedNotifications();
				for (PushedNotification pushedNotification : pushedNotifications) {
					Exception e = pushedNotification.getException();
					if (e != null) {
						e.printStackTrace();
					}
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (Throwable t) {
				t.printStackTrace();
			}
		}
	}
}
