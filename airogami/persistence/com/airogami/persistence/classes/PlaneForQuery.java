package com.airogami.persistence.classes;

import java.io.Serializable;

public class PlaneForQuery implements Serializable{

	private static final long serialVersionUID = 1L;

	private Long planeId;

	private Short status;

	private Short deletedByO;

	private Short deletedByT;
	
	public PlaneForQuery(){}
	
	public PlaneForQuery(Long planeId, Short status, Short deletedByO, Short deletedByT){
		this.planeId = planeId;
		this.status = status;
		this.deletedByO = deletedByO;
		this.deletedByT = deletedByT;
	}

	public Long getPlaneId() {
		return planeId;
	}

	public void setPlaneId(Long planeId) {
		this.planeId = planeId;
	}

	public Short getStatus() {
		return status;
	}

	public void setStatus(Short status) {
		this.status = status;
	}

	public Short getDeletedByO() {
		return deletedByO;
	}

	public void setDeletedByO(Short deletedByO) {
		this.deletedByO = deletedByO;
	}

	public Short getDeletedByT() {
		return deletedByT;
	}

	public void setDeletedByT(Short deletedByT) {
		this.deletedByT = deletedByT;
	}

}
