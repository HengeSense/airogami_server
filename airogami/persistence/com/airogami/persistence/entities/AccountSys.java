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
 * AccountSys entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "ACCOUNT_SYS", catalog = "Airogami")
public class AccountSys implements java.io.Serializable {

	// Fields
	private static final long serialVersionUID = 1L;

	private Integer accountId;

	private Account account;

	private Long chainInc = 0L;

	private Long planeInc = 0L;

	private Integer msgDataInc = 0;

	// Constructors

	/** default constructor */
	public AccountSys() {
	}

	/** full constructor */
	public AccountSys(Integer accountId, Account account, Long chainInc,
			Long planeInc, Integer msgDataInc) {
		this.accountId = accountId;
		this.account = account;
		this.chainInc = chainInc;
		this.planeInc = planeInc;
		this.msgDataInc = msgDataInc;
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

	@Column(name = "CHAIN_INC", nullable = false, insertable = false, updatable = false)
	public Long getChainInc() {
		return this.chainInc;
	}

	public void setChainInc(Long chainInc) {
		this.chainInc = chainInc;
	}

	@Column(name = "PLANE_INC", nullable = false, insertable = false, updatable = false)
	public Long getPlaneInc() {
		return this.planeInc;
	}

	public void setPlaneInc(Long planeInc) {
		this.planeInc = planeInc;
	}

	@Column(name = "MSG_DATA_INC", nullable = false, insertable = false, updatable = false)
	public Integer getMsgDataInc() {
		return this.msgDataInc;
	}

	public void setMsgDataInc(Integer msgDataInc) {
		this.msgDataInc = msgDataInc;
	}

	/**/

}