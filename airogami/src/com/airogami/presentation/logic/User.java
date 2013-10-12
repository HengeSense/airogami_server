package com.airogami.presentation.logic;

import com.airogami.common.ClientAgent;
import com.airogami.persistence.entities.AccountStat;

public class User {

	private int accountId;
	//private int messagesCount;
	//private HttpSession session;
	private ClientAgent clientAgent;
	private long signinCount;//for change password and signin other place
	
	public User(int accountId, ClientAgent clientAgent, AccountStat accountStat){
		this.accountId = accountId;
		this.clientAgent = clientAgent;
		this.signinCount = accountStat.getSigninCount();
		//this.messagesCount = accountStat.getChainMsgCount() + accountStat.getMsgCount();
	}
	public User(){
		clientAgent = new ClientAgent();
	}
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
	
	/*public int getMessagesCount() {
		return messagesCount;
	}
	public void setMessagesCount(int messagesCount) {
		this.messagesCount = messagesCount;
	}*/
	
	public long getSigninCount() {
		return signinCount;
	}

	public void setSigninCount(long signinCount) {
		this.signinCount = signinCount;
	}
	
	public void reset(ClientAgent clientAgent, AccountStat accountStat){
		this.clientAgent = clientAgent;
		//this.messagesCount = accountStat.getChainMsgCount() + accountStat.getMsgCount();
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
