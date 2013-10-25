package com.airogami.persistence.classes;

import java.io.Serializable;

public class UpdatedPlane implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private Long planeId;

	private Short status;

	private Short deletedByO;

	private Short deletedByT;
	
	private Short likedByO;

	private Short likedByT;
	
	private Integer updateCount;
	
	public UpdatedPlane(){}
	
	public UpdatedPlane(Long planeId, Short status, Short deletedByO, Short deletedByT, Short likedByO, Short likedByT, Integer updateCount){
		this.planeId = planeId;
		this.status = status;
		this.deletedByO = deletedByO;
		this.deletedByT = deletedByT;
		this.likedByO = likedByO;
		this.likedByT = likedByT;
		this.updateCount = updateCount;
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
	
	public Short getLikedByO() {
		return likedByO;
	}

	public void setLikedByO(Short likedByO) {
		this.likedByO = likedByO;
	}

	public Short getLikedByT() {
		return likedByT;
	}

	public void setLikedByT(Short likedByT) {
		this.likedByT = likedByT;
	}

	public Integer getUpdateCount() {
		return updateCount;
	}

	public void setUpdateCount(Integer updateCount) {
		this.updateCount = updateCount;
	}

}
