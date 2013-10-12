package com.airogami.persistence.entities;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import org.apache.struts2.json.annotations.JSON;

/**
 * Agent entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "AGENT", catalog = "Airogami")
public class Agent implements java.io.Serializable {

	// Fields
	private static final long serialVersionUID = 1L;

	private Integer accountId;

	private Account account;

	private Short devType = (short) 0;

	private Short devVersion = (short) 0;

	private String devToken;

	// Constructors

	/** default constructor */
	public Agent() {
	}

	/** minimal constructor */
	public Agent(Integer accountId, Account account, Short devType,
			Short devVersion) {
		this.accountId = accountId;
		this.account = account;
		this.devType = devType;
		this.devVersion = devVersion;
	}

	/** full constructor */
	public Agent(Integer accountId, Account account, Short devType,
			Short devVersion, String devToken) {
		this.accountId = accountId;
		this.account = account;
		this.devType = devType;
		this.devVersion = devVersion;
		this.devToken = devToken;
	}

	// Property accessors
	@Id
	@Column(name = "ACCOUNT_ID", unique = true, nullable = false)
	public Integer getAccountId() {
		return this.accountId;
	}

	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
	}

	@OneToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn
	public Account getAccount() {
		return this.account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	@Column(name = "DEV_TYPE", nullable = false)
	public Short getDevType() {
		return this.devType;
	}

	public void setDevType(Short devType) {
		this.devType = devType;
	}

	@Column(name = "DEV_VERSION", nullable = false)
	public Short getDevVersion() {
		return this.devVersion;
	}

	public void setDevVersion(Short devVersion) {
		this.devVersion = devVersion;
	}

	@Column(name = "DEV_TOKEN", length = 4096)
	public String getDevToken() {
		return this.devToken;
	}

	public void setDevToken(String devToken) {
		this.devToken = devToken;
	}

	/**/

}