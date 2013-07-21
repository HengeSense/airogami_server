package com.airogami.persistence.entities;

import java.sql.Timestamp;
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
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

/**
 * ChainMessage entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "CHAIN_MESSAGE", catalog = "Airogami")
public class ChainMessage implements java.io.Serializable {

	// Fields
	private static final long serialVersionUID = 1L;

	private ChainMessageId id;

	private Chain chain;

	private Account account;

	private String content;

	private Short type;

	private Timestamp updatedTime;

	private Short status = 0;

	private Timestamp lastViewedTime;

	private static Timestamp baseTimestamp = Timestamp
			.valueOf("2013-01-01 00:00:00.0");

	// Constructors

	/** default constructor */
	public ChainMessage() {
	}

	/** minimal constructor */
	public ChainMessage(ChainMessageId id, Chain chain, Account account,
			Timestamp updatedTime, Short status, Timestamp lastViewedTime) {
		this.id = id;
		this.chain = chain;
		this.account = account;
		this.updatedTime = updatedTime;
		this.status = status;
		this.lastViewedTime = lastViewedTime;
	}

	/** full constructor */
	public ChainMessage(ChainMessageId id, Chain chain, Account account,
			String content, Short type, Timestamp updatedTime, Short status,
			Timestamp lastViewedTime) {
		this.id = id;
		this.chain = chain;
		this.account = account;
		this.content = content;
		this.type = type;
		this.updatedTime = updatedTime;
		this.status = status;
		this.lastViewedTime = lastViewedTime;
	}

	// Property accessors
	@EmbeddedId
	@AttributeOverrides({
			@AttributeOverride(name = "chainId", column = @Column(name = "CHAIN_ID", nullable = false)),
			@AttributeOverride(name = "accountId", column = @Column(name = "ACCOUNT_ID", nullable = false)) })
	public ChainMessageId getId() {
		return this.id;
	}

	public void setId(ChainMessageId id) {
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

	@Column(name = "CONTENT", length = 2048)
	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Column(name = "TYPE")
	public Short getType() {
		return this.type;
	}

	public void setType(Short type) {
		this.type = type;
	}

	@Column(name = "UPDATED_TIME", nullable = false, length = 19)
	public Timestamp getUpdatedTime() {
		return this.updatedTime;
	}

	public void setUpdatedTime(Timestamp updatedTime) {
		this.updatedTime = updatedTime;
	}

	@Column(name = "STATUS", nullable = false)
	public Short getStatus() {
		return this.status;
	}

	public void setStatus(Short status) {
		this.status = status;
	}

	@Column(name = "LAST_VIEWED_TIME", nullable = false, length = 19)
	public Timestamp getLastViewedTime() {
		return this.lastViewedTime;
	}

	public void setLastViewedTime(Timestamp lastViewedTime) {
		this.lastViewedTime = lastViewedTime;
	}

	@PrePersist
	protected void onPrePersist() {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());

		setUpdatedTime(timestamp);

		setLastViewedTime(baseTimestamp);

	}

	@PreUpdate
	protected void onPreUpdate() {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		setUpdatedTime(timestamp);
	}

}