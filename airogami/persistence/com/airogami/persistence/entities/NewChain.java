package com.airogami.persistence.entities;

import java.io.Serializable;

public class NewChain implements Serializable{

	private static final long serialVersionUID = 1L;

	private Long chainId;
	private Long updateInc;
	private Long updateCount;
	public Long getChainId() {
		return chainId;
	}
	public void setChainId(Long chainId) {
		this.chainId = chainId;
	}
	public Long getUpdateInc() {
		return updateInc;
	}
	public void setUpdateInc(Long updateInc) {
		this.updateInc = updateInc;
	}
	public Long getUpdateCount() {
		return updateCount;
	}
	public void setUpdateCount(Long updateCount) {
		this.updateCount = updateCount;
	}
	
	
}
