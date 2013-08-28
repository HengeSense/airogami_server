package com.airogami.persistence.entities;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import org.apache.struts2.json.annotations.JSON;

/**
 * PlaneHistId entity. @author MyEclipse Persistence Tools
 */
@Embeddable
public class PlaneHistId implements java.io.Serializable {

	// Fields
	private static final long serialVersionUID = 1L;

	private Long planeId;

	private Long accountId;

	// Constructors

	/** default constructor */
	public PlaneHistId() {
	}

	/** full constructor */
	public PlaneHistId(Long planeId, Long accountId) {
		this.planeId = planeId;
		this.accountId = accountId;
	}

	// Property accessors

	@Column(name = "PLANE_ID", nullable = false)
	public Long getPlaneId() {
		return this.planeId;
	}

	public void setPlaneId(Long planeId) {
		this.planeId = planeId;
	}

	@Column(name = "ACCOUNT_ID", nullable = false)
	public Long getAccountId() {
		return this.accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof PlaneHistId))
			return false;
		PlaneHistId castOther = (PlaneHistId) other;

		return ((this.getPlaneId() == castOther.getPlaneId()) || (this
				.getPlaneId() != null && castOther.getPlaneId() != null && this
				.getPlaneId().equals(castOther.getPlaneId())))
				&& ((this.getAccountId() == castOther.getAccountId()) || (this
						.getAccountId() != null
						&& castOther.getAccountId() != null && this
						.getAccountId().equals(castOther.getAccountId())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getPlaneId() == null ? 0 : this.getPlaneId().hashCode());
		result = 37 * result
				+ (getAccountId() == null ? 0 : this.getAccountId().hashCode());
		return result;
	}

}