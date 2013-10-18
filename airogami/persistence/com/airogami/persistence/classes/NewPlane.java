package com.airogami.persistence.classes;

import java.io.Serializable;

public class NewPlane implements Serializable{

	private static final long serialVersionUID = 1L;

	private Long planeId;
	private Long updateInc;
	private Integer updateCount;
	private Long clearMsgId;
	private Long lastMsgId;//the opposite lastMsgId
	
	public NewPlane(){
		
	}
	
	public NewPlane(Long planeId, Long updateInc, Integer updateCount, Long clearMsgId, Long lastMsgId){
		this.planeId = planeId;
		this.updateInc = updateInc;
		this.updateCount = updateCount;
		this.clearMsgId = clearMsgId;
		this.lastMsgId = lastMsgId;
	}
	
	public Long getPlaneId() {
		return planeId;
	}
	public void setPlaneId(Long chainId) {
		this.planeId = chainId;
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

	public Long getClearMsgId() {
		return clearMsgId;
	}

	public void setClearMsgId(Long clearMsgId) {
		this.clearMsgId = clearMsgId;
	}

	public Long getLastMsgId() {
		return lastMsgId;
	}

	public void setLastMsgId(Long lastMsgId) {
		this.lastMsgId = lastMsgId;
	}
	
	
}
