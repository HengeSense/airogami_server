package com.airogami.persistence.classes;

import java.io.Serializable;

public class NeoPlane implements Serializable{

	private static final long serialVersionUID = 1L;

	private Long planeId;
	private Long updateInc;
	private Integer updateCount;
	private Long clearMsgId;
	private Long readMsgId;//the opposite lastMsgId
	private Long neoMsgId;
	
	public NeoPlane(){
		
	}
	
	public NeoPlane(Long planeId, Long updateInc, Integer updateCount, Long clearMsgId, Long readMsgId, Long neoMsgId){
		this.planeId = planeId;
		this.updateInc = updateInc;
		this.updateCount = updateCount;
		this.clearMsgId = clearMsgId;
		this.readMsgId = readMsgId;
		this.neoMsgId = neoMsgId;
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

	public Long getReadMsgId() {
		return readMsgId;
	}

	public void setReadMsgId(Long lastMsgId) {
		this.readMsgId = lastMsgId;
	}

	public Long getNeoMsgId() {
		return neoMsgId;
	}

	public void setNeoMsgId(Long newMsgId) {
		this.neoMsgId = newMsgId;
	}
	
	
}
