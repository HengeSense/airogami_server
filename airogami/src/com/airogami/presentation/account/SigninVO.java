package com.airogami.presentation.account;

import com.airogami.common.notification.ClientAgent;

public class SigninVO {

	private String email;
	private String password;
	private String screenName;	
	private boolean automatic;
	private boolean updateDev;
	private ClientAgent clientAgent = new ClientAgent();
	private int signinCount = -1;
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}	
	public String getScreenName() {
		return screenName;
	}
	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}
	public ClientAgent getClientAgent() {
		return clientAgent;
	}
	public void setClientAgent(ClientAgent clientAgent) {
		this.clientAgent = clientAgent;
	}
	public boolean getAutomatic() {
		return automatic;
	}
	public void setAutomatic(boolean automatic) {
		this.automatic = automatic;
	}
	public boolean isUpdateDev() {
		return updateDev;
	}
	public void setUpdateDev(boolean updateDev) {
		this.updateDev = updateDev;
	}
	public int getSigninCount() {
		return signinCount;
	}
	public void setSigninCount(int signinCount) {
		this.signinCount = signinCount;
	}
	
}
