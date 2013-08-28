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
 * ChainHist entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "CHAIN_HIST", catalog = "Airogami")
public class ChainHist implements java.io.Serializable {

	// Fields
	private static final long serialVersionUID = 1L;

	private ChainHistId id;

	private Chain chain;

	private Account account;

	// Constructors

	/** default constructor */
	public ChainHist() {
	}

	/** full constructor */
	public ChainHist(ChainHistId id, Chain chain, Account account) {
		this.id = id;
		this.chain = chain;
		this.account = account;
	}

	// Property accessors
	@EmbeddedId
	@AttributeOverrides({
			@AttributeOverride(name = "accountId", column = @Column(name = "ACCOUNT_ID", nullable = false)),
			@AttributeOverride(name = "chainId", column = @Column(name = "CHAIN_ID", nullable = false)) })
	public ChainHistId getId() {
		return this.id;
	}

	public void setId(ChainHistId id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CHAIN_ID", nullable = false, insertable = false, updatable = false)
	public Chain getChain() {
		return this.chain;
	}

	public void setChain(Chain chain) {
		this.chain = chain;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ACCOUNT_ID", nullable = false, insertable = false, updatable = false)
	public Account getAccount() {
		return this.account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

}