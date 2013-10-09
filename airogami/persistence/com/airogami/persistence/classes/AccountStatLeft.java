package com.airogami.persistence.classes;

import java.io.Serializable;

import com.airogami.common.constants.AccountConstants;

public class AccountStatLeft implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Short pickupLeftCount = AccountConstants.PickupMaxCount;

	private Short sendLeftCount = AccountConstants.SendMaxCount;
	
	public AccountStatLeft(Short sendLeftCount, Short pickupLeftCount){
		this.sendLeftCount = sendLeftCount;
		this.pickupLeftCount = pickupLeftCount;
	}

	public Short getPickupLeftCount() {
		return pickupLeftCount;
	}

	public void setPickupLeftCount(Short pickupLeftCount) {
		this.pickupLeftCount = pickupLeftCount;
	}

	public Short getSendLeftCount() {
		return sendLeftCount;
	}

	public void setSendLeftCount(Short sendLeftCount) {
		this.sendLeftCount = sendLeftCount;
	}
}
