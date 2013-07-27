package com.airogami.persistence.entities;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import org.apache.struts2.json.annotations.JSON;

/**
 * Message entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "MESSAGE", catalog = "Airogami")
public class Message implements java.io.Serializable {

	// Fields
	private static final long serialVersionUID = 1L;

	private Long messageId;

	private Account account;

	private Plane plane;

	private String content;

	private Short type;

	private Timestamp createdTime;

	// Constructors

	/** default constructor */
	public Message() {
	}

	/** full constructor */
	public Message(Account account, Plane plane, String content, Short type,
			Timestamp createdTime) {
		this.account = account;
		this.plane = plane;
		this.content = content;
		this.type = type;
		this.createdTime = createdTime;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "MESSAGE_ID", unique = true, nullable = false)
	public Long getMessageId() {
		return this.messageId;
	}

	public void setMessageId(Long messageId) {
		this.messageId = messageId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ACCOUNT_ID", nullable = false)
	public Account getAccount() {
		return this.account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PLANE_ID", nullable = false)
	public Plane getPlane() {
		return this.plane;
	}

	public void setPlane(Plane plane) {
		this.plane = plane;
	}

	@Column(name = "CONTENT", nullable = false, length = 256)
	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Column(name = "TYPE", nullable = false)
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

	@PrePersist
	protected void onPrePersist() {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());

		setCreatedTime(timestamp);

	}

}