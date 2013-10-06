package com.airogami.persistence.entities;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.apache.struts2.json.annotations.JSON;

/**
 * PlaneHist entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "PLANE_HIST", catalog = "Airogami")
public class PlaneHist implements java.io.Serializable {

	// Fields
	private static final long serialVersionUID = 1L;

	private PlaneHistId id;

	private Account account;

	private Plane plane;

	// Constructors

	/** default constructor */
	public PlaneHist() {
	}

	/** full constructor */
	public PlaneHist(PlaneHistId id, Account account, Plane plane) {
		this.id = id;
		this.account = account;
		this.plane = plane;
	}

	// Property accessors
	@EmbeddedId
	@AttributeOverrides({
			@AttributeOverride(name = "planeId", column = @Column(name = "PLANE_ID", nullable = false)),
			@AttributeOverride(name = "accountId", column = @Column(name = "ACCOUNT_ID", nullable = false)) })
	public PlaneHistId getId() {
		return this.id;
	}

	public void setId(PlaneHistId id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ACCOUNT_ID", nullable = false, insertable = false, updatable = false)
	public Account getAccount() {
		return this.account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PLANE_ID", nullable = false, insertable = false, updatable = false)
	public Plane getPlane() {
		return this.plane;
	}

	public void setPlane(Plane plane) {
		this.plane = plane;
	}

	/**/

}