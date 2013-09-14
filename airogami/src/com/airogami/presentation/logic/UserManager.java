package com.airogami.presentation.logic;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class UserManager implements HttpSessionListener{
	// private final int initCapacity = 100000;
	// private final int incCapacity = 10000;
	private final int initCapacity = 1;
	private final int incCapacity = 1;
	private User[] users = new User[initCapacity];

	public User updateUser(long accountId, ClientAgent clientAgent) {
		int index = (int) accountId;
		if (index >= users.length) {
			adjust(index);
		}
		//assure only one user instance will be created
		if (users[index] == null) {
			synchronized (this) {
				//prevent multiple accesses
				if (users[index] == null){
					users[index] = new User(accountId, clientAgent);
				}
				else {
					users[index].setClientAgent(clientAgent);
				}
			}
		} else {
			users[index].setClientAgent(clientAgent);
		}
		System.out.println("addUser: " + accountId);
		return users[index];
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

	@Override
	public void sessionCreated(HttpSessionEvent evt) {
		
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent evt) {
		
	}
	
	public User[] getUsers() {
		return users;
	}
}
