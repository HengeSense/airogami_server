package com.airogami.persistence.entities;

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
import javax.persistence.PreUpdate;
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

	private Timestamp updatedTime;

	private Short status = 0;

	private String city;

	private String province;

	private Double longitude;

	private Double latitude;

	private Short sex;

	private String country;

	private Integer passCount = 0;

	private Integer matchCount = 0;

	private Integer maxPassCount;

	private Integer maxMatchCount;

	private List<ChainMessage> chainMessages = new ArrayList<ChainMessage>(0);

	// Constructors

	/** default constructor */
	public Chain() {
	}

	/** minimal constructor */
	public Chain(Account account, Timestamp createdTime, Timestamp updatedTime,
			Short status, Short sex, Integer passCount, Integer matchCount,
			Integer maxPassCount, Integer maxMatchCount) {
		this.account = account;
		this.createdTime = createdTime;
		this.updatedTime = updatedTime;
		this.status = status;
		this.sex = sex;
		this.passCount = passCount;
		this.matchCount = matchCount;
		this.maxPassCount = maxPassCount;
		this.maxMatchCount = maxMatchCount;
	}

	/** full constructor */
	public Chain(Account account, Timestamp createdTime, Timestamp updatedTime,
			Short status, String city, String province, Double longitude,
			Double latitude, Short sex, String country, Integer passCount,
			Integer matchCount, Integer maxPassCount, Integer maxMatchCount,
			List<ChainMessage> chainMessages) {
		this.account = account;
		this.createdTime = createdTime;
		this.updatedTime = updatedTime;
		this.status = status;
		this.city = city;
		this.province = province;
		this.longitude = longitude;
		this.latitude = latitude;
		this.sex = sex;
		this.country = country;
		this.passCount = passCount;
		this.matchCount = matchCount;
		this.maxPassCount = maxPassCount;
		this.maxMatchCount = maxMatchCount;
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

	@Column(name = "LONGITUDE", precision = 10, scale = 6)
	public Double getLongitude() {
		return this.longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	@Column(name = "LATITUDE", precision = 10, scale = 6)
	public Double getLatitude() {
		return this.latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	@Column(name = "SEX", nullable = false)
	public Short getSex() {
		return this.sex;
	}

	public void setSex(Short sex) {
		this.sex = sex;
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

	@Column(name = "MAX_PASS_COUNT", nullable = false)
	public Integer getMaxPassCount() {
		return this.maxPassCount;
	}

	public void setMaxPassCount(Integer maxPassCount) {
		this.maxPassCount = maxPassCount;
	}

	@Column(name = "MAX_MATCH_COUNT", nullable = false)
	public Integer getMaxMatchCount() {
		return this.maxMatchCount;
	}

	public void setMaxMatchCount(Integer maxMatchCount) {
		this.maxMatchCount = maxMatchCount;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "chain")
	@OrderBy("updatedTime desc")
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

		setUpdatedTime(timestamp);

	}

	@PreUpdate
	protected void onPreUpdate() {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		setUpdatedTime(timestamp);
	}

}