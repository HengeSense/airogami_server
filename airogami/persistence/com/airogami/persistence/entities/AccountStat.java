package com.airogami.persistence.entities;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
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

	private Integer likesCount = 0;

	private Timestamp lastSigninTime;

	// Constructors

	/** default constructor */
	public AccountStat() {
	}

	/** full constructor */
	public AccountStat(Long accountId, Account account, Integer likesCount,
			Timestamp lastSigninTime) {
		this.accountId = accountId;
		this.account = account;
		this.likesCount = likesCount;
		this.lastSigninTime = lastSigninTime;
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

	@Column(name = "LIKES_COUNT", nullable = false, insertable = false, updatable = false)
	public Integer getLikesCount() {
		return this.likesCount;
	}

	public void setLikesCount(Integer likesCount) {
		this.likesCount = likesCount;
	}

	@Column(name = "LAST_SIGNIN_TIME", nullable = false, length = 19)
	@JSON(format = "yyyy-MM-dd HH:mm:ss")
	public Timestamp getLastSigninTime() {
		return this.lastSigninTime;
	}

	public void setLastSigninTime(Timestamp lastSigninTime) {
		this.lastSigninTime = lastSigninTime;
	}

	@PrePersist
	protected void onPrePersist() {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());

		setLastSigninTime(timestamp);

	}

}