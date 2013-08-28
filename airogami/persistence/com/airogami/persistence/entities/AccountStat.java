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

	// Constructors

	/** default constructor */
	public AccountStat() {
	}

	/** full constructor */
	public AccountStat(Long accountId, Account account, Long signinCount) {
		this.accountId = accountId;
		this.account = account;
		this.signinCount = signinCount;
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

}