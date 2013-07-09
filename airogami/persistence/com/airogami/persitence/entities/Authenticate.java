package com.airogami.persitence.entities;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * Authenticate entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "AUTHENTICATE", catalog = "Airogami", uniqueConstraints = @UniqueConstraint(columnNames = "EMAIL"))
public class Authenticate implements java.io.Serializable {

	// Fields
	private static final long serialVersionUID = 1L;

	private Long accountId;

	private Account account;

	private String email;

	private String password;

	// Constructors

	/** default constructor */
	public Authenticate() {
	}

	/** full constructor */
	public Authenticate(Long accountId, Account account, String email,
			String password) {
		this.accountId = accountId;
		this.account = account;
		this.email = email;
		this.password = password;
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

	@Column(name = "EMAIL", unique = true, nullable = false, length = 256)
	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "PASSWORD", nullable = false, length = 20)
	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}