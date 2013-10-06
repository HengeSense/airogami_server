package com.airogami.presentation.notification;

import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javapns.Push;
import javapns.communication.exceptions.CommunicationException;
import javapns.communication.exceptions.KeystoreException;
import javapns.devices.Device;
import javapns.devices.exceptions.InvalidDeviceTokenFormatException;
import javapns.notification.PushNotificationPayload;
import javapns.notification.PushedNotification;
import javapns.notification.PushedNotifications;
import javapns.notification.transmission.PushQueue;

import org.json.JSONException;

import com.airogami.presentation.logic.ClientAgent;
import com.airogami.presentation.logic.ManagerUtils;
import com.airogami.presentation.logic.User;

public class AppleNotification implements Runnable {
	private final int threads = 100;
	private URL keystore = getClass().getResource("/com/airogami/presentation/notification/keystore.p12");
	private final String password = "$Airogami2013";
	private final boolean production = false;
	private long internal = 60 * 1000;// 60s
	private final long period = 24 * 3600 * 1000;

	private PushQueue queue;

	private Timer timer = new Timer(true);
	private TimerTask timerTask;

	public AppleNotification() {
		try {
			queue = Push.queue(keystore.getPath(), password, production, threads);
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

	public void sendNotification(int accountId, String token, Long badge,
			PushNotificationPayload payload) {		
		try {
			if(badge != null){
				payload.addBadge((int)(long)badge);
			}
			payload.addCustomDictionary("accountId", accountId);
			queue.add(payload, token);
			System.out.printf("sending (apple) token to (%d): " + token + "\n",
					accountId);
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (InvalidDeviceTokenFormatException e) {
			e.printStackTrace();
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
}
