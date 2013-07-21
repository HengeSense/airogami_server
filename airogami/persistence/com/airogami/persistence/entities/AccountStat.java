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

	private Integer unreadMessgeCount = 0;

	// Constructors

	/** default constructor */
	public AccountStat() {
	}

	/** full constructor */
	public AccountStat(Long accountId, Account account, Integer likesCount,
			Integer unreadMessgeCount) {
		this.accountId = accountId;
		this.account = account;
		this.likesCount = likesCount;
		this.unreadMessgeCount = unreadMessgeCount;
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

	@Column(name = "UNREAD_MESSGE_COUNT", nullable = false, insertable = false, updatable = false)
	public Integer getUnreadMessgeCount() {
		return this.unreadMessgeCount;
	}

	public void setUnreadMessgeCount(Integer unreadMessgeCount) {
		this.unreadMessgeCount = unreadMessgeCount;
	}

}