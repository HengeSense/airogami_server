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
 * AccountStat entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "ACCOUNT_STAT", catalog = "Airogami")
public class AccountStat implements java.io.Serializable {

	// Fields
	private static final long serialVersionUID = 1L;

	private Long accountId;

	private Account account;

	private Long signinCount = 0L;

	private Short status = 0;

	private Integer newMsgCount = 0;

	private Integer newChainMsgCount = 0;

	// Constructors

	/** default constructor */
	public AccountStat() {
	}

	/** full constructor */
	public AccountStat(Long accountId, Account account, Long signinCount,
			Short status, Integer newMsgCount, Integer newChainMsgCount) {
		this.accountId = accountId;
		this.account = account;
		this.signinCount = signinCount;
		this.status = status;
		this.newMsgCount = newMsgCount;
		this.newChainMsgCount = newChainMsgCount;
	}

	// Property accessors
	@Id
	@Column(name = "ACCOUNT_ID", unique = true, nullable = false)
	public Long getAccountId() {
		return this.accountId;
	}

	public void setAccountId(Long accountId) {
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

	@Column(name = "SIGNIN_COUNT", nullable = false, insertable = false, updatable = false)
	public Long getSigninCount() {
		return this.signinCount;
	}

	public void setSigninCount(Long signinCount) {
		this.signinCount = signinCount;
	}

	@Column(name = "STATUS", nullable = false)
	public Short getStatus() {
		return this.status;
	}

	public void setStatus(Short status) {
		this.status = status;
	}

	@Column(name = "NEW_MSG_COUNT", nullable = false, insertable = false, updatable = false)
	public Integer getNewMsgCount() {
		return this.newMsgCount;
	}

	public void setNewMsgCount(Integer newMsgCount) {
		this.newMsgCount = newMsgCount;
	}

	@Column(name = "NEW_CHAIN_MSG_COUNT", nullable = false, insertable = false, updatable = false)
	public Integer getNewChainMsgCount() {
		return this.newChainMsgCount;
	}

	public void setNewChainMsgCount(Integer newChainMsgCount) {
		this.newChainMsgCount = newChainMsgCount;
	}

}