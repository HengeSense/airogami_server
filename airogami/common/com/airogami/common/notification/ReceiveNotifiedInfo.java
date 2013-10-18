package com.airogami.common.notification;

public class ReceiveNotifiedInfo extends NotifiedInfo {

	private Integer accountId;
	
	public ReceiveNotifiedInfo(Integer accountId){
		this.accountId = accountId;
	}
	
	public Integer getAccountId() {
		return accountId;
	}

	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
	}
	
}
