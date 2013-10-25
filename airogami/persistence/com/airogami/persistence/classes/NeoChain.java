package com.airogami.persistence.classes;

import java.io.Serializable;
import java.sql.Timestamp;

import org.apache.struts2.json.annotations.JSON;

public class NeoChain implements Serializable{

	private static final long serialVersionUID = 1L;

	private Long chainId;
	private Long updateInc;
	private Integer updateCount;
	private Timestamp updatedTime;
	
	public NeoChain(){}
	
	public NeoChain(Long chainId, Long updateInc, Integer updateCount, Timestamp updatedTime){
		this.chainId = chainId;
		this.updateInc = updateInc;
		this.updateCount = updateCount;
		this.updatedTime = updatedTime;
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

	@JSON(format = "yyyy-MM-dd HH:mm:ss")
	public Timestamp getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(Timestamp updatedTime) {
		this.updatedTime = updatedTime;
	}
	
	
}
