package com.airogami.persistence.entities;

import java.io.Serializable;

public class OldChain implements Serializable {

	private static final long serialVersionUID = 1L;
    private Long chainId;
    private Short status;
    
    public OldChain(Long chainId, Short status){
    	this.chainId = chainId;
    	this.status = status;
    }
    
    public Long getChainId() {
		return chainId;
	}
	public void setChainId(Long chainId) {
		this.chainId = chainId;
	}
	public Short getStatus() {
		return status;
	}
	public void setStatus(Short status) {
		this.status = status;
	}
	
}
