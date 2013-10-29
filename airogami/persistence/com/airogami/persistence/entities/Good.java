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
 * Good entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "GOOD", catalog = "Airogami")
public class Good implements java.io.Serializable {

	// Fields
	private static final long serialVersionUID = 1L;

	private Integer accountId;

	private Account account;

	private Short facebook = (short) 0;

	private Short twitter = (short) 0;

	// Constructors

	/** default constructor */
	public Good() {
	}

	/** full constructor */
	public Good(Integer accountId, Account account, Short facebook,
			Short twitter) {
		this.accountId = accountId;
		this.account = account;
		this.facebook = facebook;
		this.twitter = twitter;
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

	@Column(name = "FACEBOOK", nullable = false)
	public Short getFacebook() {
		return this.facebook;
	}

	public void setFacebook(Short facebook) {
		this.facebook = facebook;
	}

	@Column(name = "TWITTER", nullable = false)
	public Short getTwitter() {
		return this.twitter;
	}

	public void setTwitter(Short twitter) {
		this.twitter = twitter;
	}

	/**/

}