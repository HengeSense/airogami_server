package com.airogami.common.notification;

public class CMNotifiedInfo extends NotifiedInfo {

	private Integer accountId;
	private Integer messagesCount;
	
	public CMNotifiedInfo(Integer accountId, Long messagesCount){
		this.accountId = accountId;
		this.messagesCount = (int)(long)messagesCount;
	}
	
	public CMNotifiedInfo(Integer accountId, Integer messagesCount){
		this.accountId = accountId;
		this.messagesCount = messagesCount;
	}
	
	public Integer getAccountId() {
		return accountId;
	}

	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
	}

	public Integer getMessagesCount() {
		return messagesCount;
	}
	public void setMessagesCount(Integer messageCount) {
		this.messagesCount = messageCount;
	}
	public void setMessagesCount(Long messagesCount) {
		if(messagesCount == null){
			this.messagesCount = null;
		}
		else{
			this.messagesCount = (int)(long)messagesCount;
		}
		
	}
}
