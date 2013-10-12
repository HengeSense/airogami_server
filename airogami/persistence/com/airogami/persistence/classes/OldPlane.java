package com.airogami.persistence.classes;

import java.io.Serializable;

public class OldPlane implements Serializable {

	private static final long serialVersionUID = 1L;
    private Long planeId;
    private Short status;
    private Long lastMsgIdOfO;
    private Long lastMsgIdOfT;
    
    public OldPlane(Long planeId, Short status, Long lastMsgIdOfO, Long lastMsgIdOfT){
    	this.planeId = planeId;
    	this.status = status;
    	this.lastMsgIdOfO = lastMsgIdOfO;
    	this.lastMsgIdOfT = lastMsgIdOfT;
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

	public Long getLastMsgIdOfO() {
		return lastMsgIdOfO;
	}

	public void setLastMsgIdOfO(Long lastMsgIdOfO) {
		this.lastMsgIdOfO = lastMsgIdOfO;
	}

	public Long getLastMsgIdOfT() {
		return lastMsgIdOfT;
	}

	public void setLastMsgIdOfT(Long lastMsgIdOfT) {
		this.lastMsgIdOfT = lastMsgIdOfT;
	}
	
}
