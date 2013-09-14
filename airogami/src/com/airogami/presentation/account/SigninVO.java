package com.airogami.presentation.account;

import com.airogami.presentation.logic.ClientAgent;

public class SigninVO {

	private String email;
	private String password;
	private String screenName;	
	private boolean automatic;
	private ClientAgent clientAgent = new ClientAgent();
	
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
	public void setAutomatic(boolean ifInvalid) {
		this.automatic = ifInvalid;
	}
	
}
