package com.airogami.common.notification;

public class SilentNotifiedInfo extends NotifiedInfo {

	private Integer accountId;
	
	public SilentNotifiedInfo(){
	}
	
	public SilentNotifiedInfo(Integer accountId){
		this.accountId = accountId;
	}
	
	public Integer getAccountId() {
		return accountId;
	}

	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
	}
}
