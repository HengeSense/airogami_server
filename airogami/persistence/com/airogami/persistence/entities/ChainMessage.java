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

	private Long updateInc = 0L;

	private Short status = 0;

	private Short source = (short) 0;

	private Timestamp createdTime;

	private Timestamp lastViewedTime;

	private Short type = (short) 0;

	private Timestamp lastTime;

	private String content;

	// Constructors

	/** default constructor */
	public ChainMessage() {
	}

	/** minimal constructor */
	public ChainMessage(ChainMessageId id, Chain chain, Account account,
			Long updateInc, Short status, Short source, Timestamp createdTime,
			Timestamp lastViewedTime) {
		this.id = id;
		this.chain = chain;
		this.account = account;
		this.updateInc = updateInc;
		this.status = status;
		this.source = source;
		this.createdTime = createdTime;
		this.lastViewedTime = lastViewedTime;
	}

	/** full constructor */
	public ChainMessage(ChainMessageId id, Chain chain, Account account,
			Long updateInc, Short status, Short source, Timestamp createdTime,
			Timestamp lastViewedTime, Short type, Timestamp lastTime,
			String content) {
		this.id = id;
		this.chain = chain;
		this.account = account;
		this.updateInc = updateInc;
		this.status = status;
		this.source = source;
		this.createdTime = createdTime;
		this.lastViewedTime = lastViewedTime;
		this.type = type;
		this.lastTime = lastTime;
		this.content = content;
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

	@Column(name = "UPDATE_INC", nullable = false, insertable = false, updatable = false)
	public Long getUpdateInc() {
		return this.updateInc;
	}

	public void setUpdateInc(Long updateInc) {
		this.updateInc = updateInc;
	}

	@Column(name = "STATUS", nullable = false)
	public Short getStatus() {
		return this.status;
	}

	public void setStatus(Short status) {
		this.status = status;
	}

	@Column(name = "SOURCE", nullable = false)
	public Short getSource() {
		return this.source;
	}

	public void setSource(Short source) {
		this.source = source;
	}

	@Column(name = "CREATED_TIME", nullable = false, updatable = false, length = 19)
	@JSON(format = "yyyy-MM-dd HH:mm:ss")
	public Timestamp getCreatedTime() {
		return this.createdTime;
	}

	public void setCreatedTime(Timestamp createdTime) {
		this.createdTime = createdTime;
	}

	@Column(name = "LAST_VIEWED_TIME", nullable = false, length = 19)
	@JSON(format = "yyyy-MM-dd HH:mm:ss")
	public Timestamp getLastViewedTime() {
		return this.lastViewedTime;
	}

	public void setLastViewedTime(Timestamp lastViewedTime) {
		this.lastViewedTime = lastViewedTime;
	}

	@Column(name = "TYPE")
	public Short getType() {
		return this.type;
	}

	public void setType(Short type) {
		this.type = type;
	}

	@Column(name = "LAST_TIME", length = 19)
	@JSON(format = "yyyy-MM-dd HH:mm:ss")
	public Timestamp getLastTime() {
		return this.lastTime;
	}

	public void setLastTime(Timestamp lastTime) {
		this.lastTime = lastTime;
	}

	@Column(name = "CONTENT")
	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@PrePersist
	protected void onPrePersist() {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());

		setCreatedTime(timestamp);

		setLastViewedTime(timestamp);

		setLastTime(timestamp);

	}

	/**/

}