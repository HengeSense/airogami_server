package com.airogami.presentation.logic;

import com.airogami.persistence.entities.AccountStat;

public class User {

	private long accountId;
	private int messagesCount;
	//private HttpSession session;
	private ClientAgent clientAgent;
	private long signinCount;//for change password and signin other place
	
	public User(long accountId, ClientAgent clientAgent, AccountStat accountStat){
		this.accountId = accountId;
		this.clientAgent = clientAgent;
		this.signinCount = accountStat.getSigninCount();
		this.messagesCount = accountStat.getNewChainMsgCount() + accountStat.getNewMsgCount();
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
	
	public long getSigninCount() {
		return signinCount;
	}

	public void setSigninCount(long signinCount) {
		this.signinCount = signinCount;
	}
	
	public void reset(ClientAgent clientAgent, AccountStat accountStat){
		this.clientAgent = clientAgent;
		this.messagesCount = accountStat.getNewChainMsgCount() + accountStat.getNewMsgCount();
		synchronized(this){
			if(accountStat.getSigninCount() > this.signinCount){
				this.signinCount = accountStat.getSigninCount();
			}
			
		}
	}
	
	/*public HttpSession getSession() {
		return session;
	}
	public synchronized void setSession(HttpSession session) {
		if(this.session != null && this.session != session){
			try{
				this.session.removeAttribute("user");
				this.session.invalidate();
			}
			catch(IllegalStateException lse){
				//lse.printStackTrace();
			}
		}
		this.session = session;
	}
	public synchronized void invalidate(HttpSession session){
		if(this.session)
	}*/
	
}
