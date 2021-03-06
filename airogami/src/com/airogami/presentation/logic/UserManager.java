package com.airogami.presentation.logic;

import com.airogami.common.notification.ClientAgent;
import com.airogami.persistence.entities.AccountStat;

public class UserManager {// implements HttpSessionListener
	// private final int initCapacity = 100000;
	// private final int incCapacity = 10000;
	private final int initCapacity = 1;
	private final int incCapacity = 1;
	private User[] users = new User[initCapacity];

	public User updateUser(int accountId, ClientAgent clientAgent,
			AccountStat accountStat) {
		int index = (int) accountId;
		if (index >= users.length) {
			adjust(index);
		}
		// assure only one user instance will be created
		if (users[index] == null) {
			synchronized (this) {
				// prevent multiple accesses
				if (users[index] == null) {
					users[index] = new User(accountId, clientAgent,
							accountStat);
				} else {
					users[index].reset(clientAgent,
							accountStat);
				}
			}
		} else {
			users[index].reset(clientAgent, accountStat);
		}
		System.out.println("add User: " + accountId);
		return users[index];
	}
	
	//can set null once set value but user.clientAgent can be null
	public void removeClientAgent(int accountId){
		if(accountId < users.length && users[accountId] != null){
			users[accountId].setClientAgent(null);
		}
	}

	public User getUser(int accountId) {
		User[] uu = users;
		if (accountId >= uu.length) {
			return null;
		} else {
			return uu[accountId];
		}
	}

	// accountId >= users.length
	private synchronized void adjust(int index) {
		if (index >= users.length) {
			int length = incCapacity + index;
			User[] uu = new User[length];
			System.arraycopy(users, 0, uu, 0, users.length);
			users = uu;
		}
	}

	/*
	 * @Override public void sessionCreated(HttpSessionEvent evt) {
	 * System.out.println("Session created"); }
	 * 
	 * @Override public void sessionDestroyed(HttpSessionEvent evt) {
	 * System.out.println("Session destroyed"); HttpSession session =
	 * evt.getSession(); User user = (User)session.getAttribute("user"); if(user
	 * != null){ user.setSession(null); } }
	 */

	public User[] getUsers() {
		return users;
	}
}
