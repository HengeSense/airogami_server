package com.airogami.persistence.classes;

import java.io.Serializable;

public class OldPlane implements Serializable {

	private static final long serialVersionUID = 1L;
    private Long planeId;
    private Short status;
    private Long lastMsgIdOfOwner;
    private Long lastMsgIdOfTarget;
    
    public OldPlane(Long planeId, Short status, Long lastMsgIdOfOwner, Long lastMsgIdOfTarget){
    	this.planeId = planeId;
    	this.status = status;
    	this.lastMsgIdOfOwner = lastMsgIdOfOwner;
    	this.lastMsgIdOfTarget = lastMsgIdOfTarget;
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

	public Long getLastMsgIdOfOwner() {
		return lastMsgIdOfOwner;
	}

	public void setLastMsgIdOfOwner(Long lastMsgIdOfOwner) {
		this.lastMsgIdOfOwner = lastMsgIdOfOwner;
	}

	public Long getLastMsgIdOfTarget() {
		return lastMsgIdOfTarget;
	}

	public void setLastMsgIdOfTarget(Long lastMsgIdOfTarget) {
		this.lastMsgIdOfTarget = lastMsgIdOfTarget;
	}
	
}
