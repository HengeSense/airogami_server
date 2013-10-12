package com.airogami.persistence.classes;

import java.io.Serializable;

public class NewChain implements Serializable{

	private static final long serialVersionUID = 1L;

	private Long chainId;
	private Long updateInc;
	private Integer updateCount;
	
	public NewChain(){}
	
	public NewChain(Long chainId, Long updateInc, Integer updateCount){
		this.chainId = chainId;
		this.updateInc = updateInc;
		this.updateCount = updateCount;
	}
	
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
	public Integer getUpdateCount() {
		return updateCount;
	}
	public void setUpdateCount(Integer updateCount) {
		this.updateCount = updateCount;
	}
	
	
}
