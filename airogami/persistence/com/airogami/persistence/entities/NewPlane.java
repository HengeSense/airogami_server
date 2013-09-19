package com.airogami.persistence.entities;

import java.io.Serializable;

public class NewPlane implements Serializable{

	private static final long serialVersionUID = 1L;

	private Long planeId;
	private Long updateInc;
	private Integer updateCount;
	
	public NewPlane(Long planeId, Long updateInc, Integer updateCount){
		this.planeId = planeId;
		this.updateInc = updateInc;
		this.updateCount = updateCount;
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
	
	
}
