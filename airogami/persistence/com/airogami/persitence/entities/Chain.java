package com.airogami.persitence.entities;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.PrePersist;
import javax.persistence.Table;

/**
 * Chain entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "CHAIN", catalog = "Airogami")
public class Chain implements java.io.Serializable {

	// Fields
	private static final long serialVersionUID = 1L;

	private Long chainId;

	private Account account;

	private Timestamp createdTime;

	private Short messageCount = 0;

	private String city;

	private String province;

	private String country;

	private Integer passCount = 0;

	private Integer matchCount = 0;

	private List<ChainMessage> chainMessages = new ArrayList<ChainMessage>(0);

	// Constructors

	/** default constructor */
	public Chain() {
	}

	/** minimal constructor */
	public Chain(Account account, Timestamp createdTime, Short messageCount,
			Integer passCount, Integer matchCount) {
		this.account = account;
		this.createdTime = createdTime;
		this.messageCount = messageCount;
		this.passCount = passCount;
		this.matchCount = matchCount;
	}

	/** full constructor */
	public Chain(Account account, Timestamp createdTime, Short messageCount,
			String city, String province, String country, Integer passCount,
			Integer matchCount, List<ChainMessage> chainMessages) {
		this.account = account;
		this.createdTime = createdTime;
		this.messageCount = messageCount;
		this.city = city;
		this.province = province;
		this.country = country;
		this.passCount = passCount;
		this.matchCount = matchCount;
		this.chainMessages = chainMessages;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "CHAIN_ID", unique = true, nullable = false)
	public Long getChainId() {
		return this.chainId;
	}

	public void setChainId(Long chainId) {
		this.chainId = chainId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ACCOUNT_ID", nullable = false)
	public Account getAccount() {
		return this.account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	@Column(name = "CREATED_TIME", nullable = false, updatable = false, length = 19)
	public Timestamp getCreatedTime() {
		return this.createdTime;
	}

	public void setCreatedTime(Timestamp createdTime) {
		this.createdTime = createdTime;
	}

	@Column(name = "MESSAGE_COUNT", nullable = false, insertable = false, updatable = false)
	public Short getMessageCount() {
		return this.messageCount;
	}

	public void setMessageCount(Short messageCount) {
		this.messageCount = messageCount;
	}

	@Column(name = "CITY", length = 256)
	public String getCity() {
		return this.city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@Column(name = "PROVINCE", length = 256)
	public String getProvince() {
		return this.province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	@Column(name = "COUNTRY", length = 256)
	public String getCountry() {
		return this.country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	@Column(name = "PASS_COUNT", nullable = false, insertable = false, updatable = false)
	public Integer getPassCount() {
		return this.passCount;
	}

	public void setPassCount(Integer passCount) {
		this.passCount = passCount;
	}

	@Column(name = "MATCH_COUNT", nullable = false, insertable = false, updatable = false)
	public Integer getMatchCount() {
		return this.matchCount;
	}

	public void setMatchCount(Integer matchCount) {
		this.matchCount = matchCount;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "chain")
	@OrderBy("createdTime desc")
	public List<ChainMessage> getChainMessages() {
		return this.chainMessages;
	}

	public void setChainMessages(List<ChainMessage> chainMessages) {
		this.chainMessages = chainMessages;
	}

	@PrePersist
	protected void onPrePersist() {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		setCreatedTime(timestamp);

	}

}