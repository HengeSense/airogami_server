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
import javax.persistence.PrePersist;
import javax.persistence.Table;

/**
 * Plane entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "PLANE", catalog = "Airogami")
public class Plane implements java.io.Serializable {

	// Fields
	private static final long serialVersionUID = 1L;

	private Long planeId;

	private Account accountByTargetId;

	private Category category;

	private Account accountByOwnerId;

	private Timestamp createdTime;

	private Short status = 0;

	private Double longitude;

	private Double altitude;

	private Timestamp ownerViewedTime;

	private Timestamp targetViewedTime;

	private String city;

	private String province;

	private String country;

	private Integer matchCount = 0;

	private List<Message> messages = new ArrayList<Message>(0);

	// Constructors

	/** default constructor */
	public Plane() {
	}

	/** minimal constructor */
	public Plane(Category category, Timestamp createdTime, Short status,
			Double longitude, Double altitude, Timestamp ownerViewedTime,
			Timestamp targetViewedTime, Integer matchCount) {
		this.category = category;
		this.createdTime = createdTime;
		this.status = status;
		this.longitude = longitude;
		this.altitude = altitude;
		this.ownerViewedTime = ownerViewedTime;
		this.targetViewedTime = targetViewedTime;
		this.matchCount = matchCount;
	}

	/** full constructor */
	public Plane(Account accountByTargetId, Category category,
			Account accountByOwnerId, Timestamp createdTime, Short status,
			Double longitude, Double altitude, Timestamp ownerViewedTime,
			Timestamp targetViewedTime, String city, String province,
			String country, Integer matchCount, List<Message> messages) {
		this.accountByTargetId = accountByTargetId;
		this.category = category;
		this.accountByOwnerId = accountByOwnerId;
		this.createdTime = createdTime;
		this.status = status;
		this.longitude = longitude;
		this.altitude = altitude;
		this.ownerViewedTime = ownerViewedTime;
		this.targetViewedTime = targetViewedTime;
		this.city = city;
		this.province = province;
		this.country = country;
		this.matchCount = matchCount;
		this.messages = messages;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "PLANE_ID", unique = true, nullable = false)
	public Long getPlaneId() {
		return this.planeId;
	}

	public void setPlaneId(Long planeId) {
		this.planeId = planeId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TARGET_ID")
	public Account getAccountByTargetId() {
		return this.accountByTargetId;
	}

	public void setAccountByTargetId(Account accountByTargetId) {
		this.accountByTargetId = accountByTargetId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CATEGORY_ID", nullable = false)
	public Category getCategory() {
		return this.category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "OWNER_ID")
	public Account getAccountByOwnerId() {
		return this.accountByOwnerId;
	}

	public void setAccountByOwnerId(Account accountByOwnerId) {
		this.accountByOwnerId = accountByOwnerId;
	}

	@Column(name = "CREATED_TIME", nullable = false, updatable = false, length = 19)
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

	@Column(name = "OWNER_VIEWED_TIME", nullable = false, length = 19)
	public Timestamp getOwnerViewedTime() {
		return this.ownerViewedTime;
	}

	public void setOwnerViewedTime(Timestamp ownerViewedTime) {
		this.ownerViewedTime = ownerViewedTime;
	}

	@Column(name = "TARGET_VIEWED_TIME", nullable = false, length = 19)
	public Timestamp getTargetViewedTime() {
		return this.targetViewedTime;
	}

	public void setTargetViewedTime(Timestamp targetViewedTime) {
		this.targetViewedTime = targetViewedTime;
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

	@Column(name = "MATCH_COUNT", nullable = false, insertable = false, updatable = false)
	public Integer getMatchCount() {
		return this.matchCount;
	}

	public void setMatchCount(Integer matchCount) {
		this.matchCount = matchCount;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "plane")
	public List<Message> getMessages() {
		return this.messages;
	}

	public void setMessages(List<Message> messages) {
		this.messages = messages;
	}

	@PrePersist
	protected void onPrePersist() {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		setCreatedTime(timestamp);
		setOwnerViewedTime(timestamp);
		setTargetViewedTime(timestamp);

	}

}