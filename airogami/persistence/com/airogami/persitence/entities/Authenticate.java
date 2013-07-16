package com.airogami.persitence.entities;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * Authenticate entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "AUTHENTICATE", catalog = "Airogami", uniqueConstraints = {
		@UniqueConstraint(columnNames = "SCREEN_NAME"),
		@UniqueConstraint(columnNames = "EMAIL") })
public class Authenticate implements java.io.Serializable {

	// Fields
	private static final long serialVersionUID = 1L;

	private Long accountId;

	private String email;

	private String screenName;

	private String password;

	private Account account;

	// Constructors

	/** default constructor */
	public Authenticate() {
	}

	/** minimal constructor */
	public Authenticate(String email, String password) {
		this.email = email;
		this.password = password;
	}

	/** full constructor */
	public Authenticate(String email, String screenName, String password,
			Account account) {
		this.email = email;
		this.screenName = screenName;
		this.password = password;
		this.account = account;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "ACCOUNT_ID", unique = true, nullable = false)
	public Long getAccountId() {
		return this.accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	@Column(name = "EMAIL", unique = true, nullable = false, length = 256)
	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "SCREEN_NAME", unique = true, length = 32)
	public String getScreenName() {
		return this.screenName;
	}

	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}

	@Column(name = "PASSWORD", nullable = false, length = 20)
	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@OneToOne(fetch = FetchType.LAZY, mappedBy = "authenticate")
	public Account getAccount() {
		return this.account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

}