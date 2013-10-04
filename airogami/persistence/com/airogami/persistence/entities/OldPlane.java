package com.airogami.persistence.entities;

import java.io.Serializable;

public class OldPlane implements Serializable {

	private static final long serialVersionUID = 1L;
    private Long planeId;
    private Short status;
    
    public OldPlane(Long planeId, Short status){
    	this.planeId = planeId;
    	this.status = status;
    }
    
    public Long getPlaneId() {
		return planeId;
	}
	public void setPlaneId(Long chainId) {
		this.planeId = chainId;
	}
	public Short getStatus() {
		return status;
	}
	public void setStatus(Short status) {
		this.status = status;
	}
	
}
