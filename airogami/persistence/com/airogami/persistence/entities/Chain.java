package com.airogami.persistence.entities;

import java.sql.Date;
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
import org.apache.struts2.json.annotations.JSON;

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

	private Integer updateCount = 0;

	private Timestamp updatedTime;

	private Timestamp createdTime;

	private Long updateInc = 0L;

	private Short status = 0;

	private String city;

	private String province;

	private Double longitude;

	private Double latitude;

	private Short sex;

	private String country;

	private Short passCount = 0;

	private Short matchCount = 0;

	private Short maxPassCount;

	private Short maxMatchCount;

	private Date birthdayLower;

	private Date birthdayUpper;

	private String language;

	private List<ChainHist> chainHists = new ArrayList<ChainHist>(0);

	private List<ChainMessage> chainMessages = new ArrayList<ChainMessage>(0);

	// Constructors

	/** default constructor */
	public Chain() {
	}

	/** minimal constructor */
	public Chain(Account account, Integer updateCount, Timestamp updatedTime,
			Timestamp createdTime, Long updateInc, Short status, Short sex,
			Short passCount, Short matchCount, Short maxPassCount,
			Short maxMatchCount) {
		this.account = account;
		this.updateCount = updateCount;
		this.updatedTime = updatedTime;
		this.createdTime = createdTime;
		this.updateInc = updateInc;
		this.status = status;
		this.sex = sex;
		this.passCount = passCount;
		this.matchCount = matchCount;
		this.maxPassCount = maxPassCount;
		this.maxMatchCount = maxMatchCount;
	}

	/** full constructor */
	public Chain(Account account, Integer updateCount, Timestamp updatedTime,
			Timestamp createdTime, Long updateInc, Short status, String city,
			String province, Double longitude, Double latitude, Short sex,
			String country, Short passCount, Short matchCount,
			Short maxPassCount, Short maxMatchCount, Date birthdayLower,
			Date birthdayUpper, String language, List<ChainHist> chainHists,
			List<ChainMessage> chainMessages) {
		this.account = account;
		this.updateCount = updateCount;
		this.updatedTime = updatedTime;
		this.createdTime = createdTime;
		this.updateInc = updateInc;
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
		this.birthdayLower = birthdayLower;
		this.birthdayUpper = birthdayUpper;
		this.language = language;
		this.chainHists = chainHists;
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

	@Column(name = "UPDATE_COUNT", nullable = false, insertable = false, updatable = false)
	public Integer getUpdateCount() {
		return this.updateCount;
	}

	public void setUpdateCount(Integer updateCount) {
		this.updateCount = updateCount;
	}

	@Column(name = "UPDATED_TIME", nullable = false, length = 19)
	@JSON(format = "yyyy-MM-dd HH:mm:ss")
	public Timestamp getUpdatedTime() {
		return this.updatedTime;
	}

	public void setUpdatedTime(Timestamp updatedTime) {
		this.updatedTime = updatedTime;
	}

	@Column(name = "CREATED_TIME", nullable = false, updatable = false, length = 19)
	@JSON(format = "yyyy-MM-dd HH:mm:ss")
	public Timestamp getCreatedTime() {
		return this.createdTime;
	}

	public void setCreatedTime(Timestamp createdTime) {
		this.createdTime = createdTime;
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

	@Column(name = "CITY")
	public String getCity() {
		return this.city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@Column(name = "PROVINCE")
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

	@Column(name = "COUNTRY")
	public String getCountry() {
		return this.country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	@Column(name = "PASS_COUNT", nullable = false, insertable = false, updatable = false)
	public Short getPassCount() {
		return this.passCount;
	}

	public void setPassCount(Short passCount) {
		this.passCount = passCount;
	}

	@Column(name = "MATCH_COUNT", nullable = false, insertable = false, updatable = false)
	public Short getMatchCount() {
		return this.matchCount;
	}

	public void setMatchCount(Short matchCount) {
		this.matchCount = matchCount;
	}

	@Column(name = "MAX_PASS_COUNT", nullable = false)
	public Short getMaxPassCount() {
		return this.maxPassCount;
	}

	public void setMaxPassCount(Short maxPassCount) {
		this.maxPassCount = maxPassCount;
	}

	@Column(name = "MAX_MATCH_COUNT", nullable = false)
	public Short getMaxMatchCount() {
		return this.maxMatchCount;
	}

	public void setMaxMatchCount(Short maxMatchCount) {
		this.maxMatchCount = maxMatchCount;
	}

	@Column(name = "BIRTHDAY_LOWER", length = 10)
	@JSON(format = "yyyy-MM-dd HH:mm:ss")
	public Date getBirthdayLower() {
		return this.birthdayLower;
	}

	public void setBirthdayLower(Date birthdayLower) {
		this.birthdayLower = birthdayLower;
	}

	@Column(name = "BIRTHDAY_UPPER", length = 10)
	@JSON(format = "yyyy-MM-dd HH:mm:ss")
	public Date getBirthdayUpper() {
		return this.birthdayUpper;
	}

	public void setBirthdayUpper(Date birthdayUpper) {
		this.birthdayUpper = birthdayUpper;
	}

	@Column(name = "LANGUAGE")
	public String getLanguage() {
		return this.language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "chain")
	public List<ChainHist> getChainHists() {
		return this.chainHists;
	}

	public void setChainHists(List<ChainHist> chainHists) {
		this.chainHists = chainHists;
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

		setUpdatedTime(timestamp);

		setCreatedTime(timestamp);

	}

	@PreUpdate
	protected void onPreUpdate() {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		setUpdatedTime(timestamp);
	}

}