package com.airogami.persistence.classes;

import java.io.Serializable;

public class UpdatedChain implements Serializable {
	
	// Fields
	private static final long serialVersionUID = 1L;

	private Long chainId;
	private Integer updateCount;
	
	public UpdatedChain(Long chainId, Integer updateCount)
	{
	    this.chainId = chainId;
	    this.updateCount = updateCount;
	}

	public Long getChainId() {
		return chainId;
	}

	public void setChainId(Long chainId) {
		this.chainId = chainId;
	}

	public Integer getUpdateCount() {
		return updateCount;
	}

	public void setUpdateCount(Integer updateCount) {
		this.updateCount = updateCount;
	}
}
