package com.airogami.persistence.classes;

import java.io.Serializable;
import java.sql.Timestamp;

import org.apache.struts2.json.annotations.JSON;

public class OldChain implements Serializable {

	private static final long serialVersionUID = 1L;
    private Long chainId;
    private Short status;
    private Timestamp lastViewedTime;
    
    public OldChain(Long chainId, Short status, Timestamp lastViewedTime){
    	this.chainId = chainId;
    	this.status = status;
    	this.lastViewedTime = lastViewedTime;
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
	
	@JSON(format = "yyyy-MM-dd HH:mm:ss")
	public Timestamp getLastViewedTime() {
		return lastViewedTime;
	}

	public void setLastViewedTime(Timestamp lastViewedTime) {
		this.lastViewedTime = lastViewedTime;
	}
	
}
