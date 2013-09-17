package com.airogami.presentation.logic;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import com.airogami.persistence.entities.AccountStat;

public class UserManager {// implements HttpSessionListener
	// private final int initCapacity = 100000;
	// private final int incCapacity = 10000;
	private final int initCapacity = 1;
	private final int incCapacity = 1;
	private User[] users = new User[initCapacity];

	public User updateUser(long accountId, ClientAgent clientAgent,
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
		System.out.println("addUser: " + accountId);
		return users[index];
	}
	
	//can set null once set value but user.clientAgent can be null
	public void removeUser(long accountId){
		if(accountId < users.length && users[(int)accountId] != null){
			users[(int)accountId].setClientAgent(null);
		}
	}

	public User getUser(long accountId) {
		User[] uu = users;
		if (accountId >= uu.length) {
			return null;
		} else {
			return uu[(int) accountId];
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
