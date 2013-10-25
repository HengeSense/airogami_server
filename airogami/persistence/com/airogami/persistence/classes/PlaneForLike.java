package com.airogami.persistence.classes;

public class PlaneForLike extends PlaneForQuery{

	private static final long serialVersionUID = 1L;

	private Short likedByO;

	private Short likedByT;

	public PlaneForLike(){}
	
	public PlaneForLike(Long planeId, Short status, Short deletedByO, Short deletedByT, Short likedByO, Short likedByT){
		super(planeId, status, deletedByO, deletedByT);
		this.likedByO = likedByO;
		this.likedByT = likedByT;
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
}
