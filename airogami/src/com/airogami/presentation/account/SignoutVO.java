package com.airogami.presentation.account;

import com.airogami.presentation.logic.ClientAgent;

public class SignoutVO {

	private long accountId;
	private int signinCount;
	private ClientAgent clientAgent = new ClientAgent();
	
	public long getAccountId() {
		return accountId;
	}
	public void setAccountId(long accountId) {
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
