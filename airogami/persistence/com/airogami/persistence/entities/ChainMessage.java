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
import javax.persistence.Table;
import org.apache.struts2.json.annotations.JSON;

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

	private Timestamp createdTime;

	private Short status = 0;

	private Timestamp lastViewedTime = baseTimestamp;

	private Short source = (short) 0;

	private Timestamp lastTime;

	private static final Timestamp baseTimestamp = Timestamp
			.valueOf("2013-01-01 00:00:00.0");

	// Constructors

	/** default constructor */
	public ChainMessage() {
	}

	/** minimal constructor */
	public ChainMessage(ChainMessageId id, Chain chain, Account account,
			Timestamp createdTime, Short status, Timestamp lastViewedTime,
			Short source) {
		this.id = id;
		this.chain = chain;
		this.account = account;
		this.createdTime = createdTime;
		this.status = status;
		this.lastViewedTime = lastViewedTime;
		this.source = source;
	}

	/** full constructor */
	public ChainMessage(ChainMessageId id, Chain chain, Account account,
			String content, Short type, Timestamp createdTime, Short status,
			Timestamp lastViewedTime, Short source, Timestamp lastTime) {
		this.id = id;
		this.chain = chain;
		this.account = account;
		this.content = content;
		this.type = type;
		this.createdTime = createdTime;
		this.status = status;
		this.lastViewedTime = lastViewedTime;
		this.source = source;
		this.lastTime = lastTime;
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

	@Column(name = "CONTENT")
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

	@Column(name = "CREATED_TIME", nullable = false, updatable = false, length = 19)
	@JSON(format = "yyyy-MM-dd HH:mm:ss")
	public Timestamp getCreatedTime() {
		return this.createdTime;
	}

	public void setCreatedTime(Timestamp createdTime) {
		this.createdTime = createdTime;
	}

	@Column(name = "STATUS", nullable = false)
	public Short getStatus() {
		return this.status;
	}

	public void setStatus(Short status) {
		this.status = status;
	}

	@Column(name = "LAST_VIEWED_TIME", nullable = false, length = 19)
	@JSON(format = "yyyy-MM-dd HH:mm:ss")
	public Timestamp getLastViewedTime() {
		return this.lastViewedTime;
	}

	public void setLastViewedTime(Timestamp lastViewedTime) {
		this.lastViewedTime = lastViewedTime;
	}

	@Column(name = "SOURCE", nullable = false)
	public Short getSource() {
		return this.source;
	}

	public void setSource(Short source) {
		this.source = source;
	}

	@Column(name = "LAST_TIME", length = 19)
	@JSON(format = "yyyy-MM-dd HH:mm:ss")
	public Timestamp getLastTime() {
		return this.lastTime;
	}

	public void setLastTime(Timestamp lastTime) {
		this.lastTime = lastTime;
	}

	@PrePersist
	protected void onPrePersist() {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());

		setCreatedTime(timestamp);

		setLastViewedTime(baseTimestamp);

		setLastTime(timestamp);

	}

	/**/

}