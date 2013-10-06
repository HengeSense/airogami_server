package com.airogami.common;

public class ChainMessageNotifiedInfo extends NotifiedInfo {

	private Integer accountId;
	private Long messagesCount;
	
	public ChainMessageNotifiedInfo(Integer accountId, Long messagesCount){
		this.accountId = accountId;
		this.messagesCount = messagesCount;
	}
	
	public ChainMessageNotifiedInfo(Integer accountId, int messagesCount){
		this.messagesCount = (long)messagesCount;
	}
	
	public Integer getAccountId() {
		return accountId;
	}

	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
	}

	public Long getMessagesCount() {
		return messagesCount;
	}
	public void setMessagesCount(Long messageCount) {
		this.messagesCount = messageCount;
	}
}
