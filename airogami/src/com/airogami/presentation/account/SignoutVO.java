package com.airogami.presentation.account;

import com.airogami.common.ClientAgent;

public class SignoutVO {

	private int accountId;
	private int signinCount;
	private ClientAgent clientAgent = new ClientAgent();
	
	public int getAccountId() {
		return accountId;
	}
	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}
	public ClientAgent getClientAgent() {
		return clientAgent;
	}
	public void setClientAgent(ClientAgent clientAgent) {
		this.clientAgent = clientAgent;
	}
	public int getSigninCount() {
		return signinCount;
	}
	public void setSigninCount(int signinCount) {
		this.signinCount = signinCount;
	}
	
}
