package com.airogami.presentation.logic;

public class User {

	private long accountId;
	private int messagesCount;
	private ClientAgent clientAgent;
	
	public User(long accountId, ClientAgent clientAgent){
		this.accountId = accountId;
		this.clientAgent = clientAgent;
	}
	public User(){
		clientAgent = new ClientAgent();
	}
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
	public int getMessagesCount() {
		return messagesCount;
	}
	public void setMessagesCount(int messagesCount) {
		this.messagesCount = messagesCount;
	}
	
}
