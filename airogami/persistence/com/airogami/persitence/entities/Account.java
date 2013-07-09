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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.PrePersist;
import javax.persistence.Table;

/**
 * Account entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "ACCOUNT", catalog = "Airogami")
public class Account implements java.io.Serializable {

	// Fields
	private static final long serialVersionUID = 1L;

	private Long accountId;

	private String fullName;

	private Short sex;

	private String icon;

	private Double longitude;

	private Double altitude;

	private Short status = 0;

	private Timestamp createdTime;

	private String city;

	private String province;

	private String country;

	private List<Plane> planesForOwnerId = new ArrayList<Plane>(0);

	private List<Message> messages = new ArrayList<Message>(0);

	private List<Plane> planesForTargetId = new ArrayList<Plane>(0);

	private List<Chain> chains = new ArrayList<Chain>(0);

	private Authenticate authenticate;

	private AccountStat accountStat;

	private List<ChainMessage> chainMessages = new ArrayList<ChainMessage>(0);

	// Constructors

	/** default constructor */
	public Account() {
	}

	/** minimal constructor */
	public Account(String fullName, Short sex, String icon, Double longitude,
			Double altitude, Short status, Timestamp createdTime, String city,
			String province, String country) {
		this.fullName = fullName;
		this.sex = sex;
		this.icon = icon;
		this.longitude = longitude;
		this.altitude = altitude;
		this.status = status;
		this.createdTime = createdTime;
		this.city = city;
		this.province = province;
		this.country = country;
	}

	/** full constructor */
	public Account(String fullName, Short sex, String icon, Double longitude,
			Double altitude, Short status, Timestamp createdTime, String city,
			String province, String country, List<Plane> planesForOwnerId,
			List<Message> messages, List<Plane> planesForTargetId,
			List<Chain> chains, Authenticate authenticate,
			AccountStat accountStat, List<ChainMessage> chainMessages) {
		this.fullName = fullName;
		this.sex = sex;
		this.icon = icon;
		this.longitude = longitude;
		this.altitude = altitude;
		this.status = status;
		this.createdTime = createdTime;
		this.city = city;
		this.province = province;
		this.country = country;
		this.planesForOwnerId = planesForOwnerId;
		this.messages = messages;
		this.planesForTargetId = planesForTargetId;
		this.chains = chains;
		this.authenticate = authenticate;
		this.accountStat = accountStat;
		this.chainMessages = chainMessages;
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

	@Column(name = "FULL_NAME", nullable = false, length = 70)
	public String getFullName() {
		return this.fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	@Column(name = "SEX", nullable = false)
	public Short getSex() {
		return this.sex;
	}

	public void setSex(Short sex) {
		this.sex = sex;
	}

	@Column(name = "ICON", nullable = false, length = 256)
	public String getIcon() {
		return this.icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	@Column(name = "LONGITUDE", nullable = false, precision = 10, scale = 6)
	public Double getLongitude() {
		return this.longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	@Column(name = "ALTITUDE", nullable = false, precision = 10, scale = 6)
	public Double getAltitude() {
		return this.altitude;
	}

	public void setAltitude(Double altitude) {
		this.altitude = altitude;
	}

	@Column(name = "STATUS", nullable = false)
	public Short getStatus() {
		return this.status;
	}

	public void setStatus(Short status) {
		this.status = status;
	}

	@Column(name = "CREATED_TIME", nullable = false, updatable = false, length = 19)
	public Timestamp getCreatedTime() {
		return this.createdTime;
	}

	public void setCreatedTime(Timestamp createdTime) {
		this.createdTime = createdTime;
	}

	@Column(name = "CITY", nullable = false, length = 256)
	public String getCity() {
		return this.city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@Column(name = "PROVINCE", nullable = false, length = 256)
	public String getProvince() {
		return this.province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	@Column(name = "COUNTRY", nullable = false, length = 256)
	public String getCountry() {
		return this.country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "accountByOwnerId")
	public List<Plane> getPlanesForOwnerId() {
		return this.planesForOwnerId;
	}

	public void setPlanesForOwnerId(List<Plane> planesForOwnerId) {
		this.planesForOwnerId = planesForOwnerId;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "account")
	public List<Message> getMessages() {
		return this.messages;
	}

	public void setMessages(List<Message> messages) {
		this.messages = messages;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "accountByTargetId")
	public List<Plane> getPlanesForTargetId() {
		return this.planesForTargetId;
	}

	public void setPlanesForTargetId(List<Plane> planesForTargetId) {
		this.planesForTargetId = planesForTargetId;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "account")
	public List<Chain> getChains() {
		return this.chains;
	}

	public void setChains(List<Chain> chains) {
		this.chains = chains;
	}

	@OneToOne(fetch = FetchType.LAZY, mappedBy = "account")
	public Authenticate getAuthenticate() {
		return this.authenticate;
	}

	public void setAuthenticate(Authenticate authenticate) {
		this.authenticate = authenticate;
	}

	@OneToOne(fetch = FetchType.LAZY, mappedBy = "account")
	public AccountStat getAccountStat() {
		return this.accountStat;
	}

	public void setAccountStat(AccountStat accountStat) {
		this.accountStat = accountStat;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "account")
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