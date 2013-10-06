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
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import org.apache.struts2.json.annotations.JSON;

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

	private Integer updateCount = 0;

	private Timestamp updatedTime;

	private Timestamp createdTime;

	private Long updateInc = 0L;

	private Short status = 0;

	private Double longitude;

	private Double latitude;

	private Long lastMsgIdOfTarget = 0L;

	private Long lastMsgIdOfOwner = 0L;

	private String city;

	private String province;

	private String country;

	private Short sex;

	private Short matchCount = 0;

	private Short maxMatchCount;

	private Short likedByOwner;

	private Short likedByTarget;

	private Short deletedByOwner;

	private Short deletedByTarget;

	private Date birthdayLower;

	private Date birthdayUpper;

	private String language;

	private Short source = (short) 0;

	private Long lastMsgId = 0L;

	private List<PlaneHist> planeHists = new ArrayList<PlaneHist>(0);

	private List<Message> messages = new ArrayList<Message>(0);

	// Constructors

	/** default constructor */
	public Plane() {
	}

	/** minimal constructor */
	public Plane(Category category, Integer updateCount, Timestamp updatedTime,
			Timestamp createdTime, Long updateInc, Short status,
			Long lastMsgIdOfTarget, Long lastMsgIdOfOwner, Short sex,
			Short matchCount, Short maxMatchCount, Short likedByOwner,
			Short likedByTarget, Short deletedByOwner, Short deletedByTarget,
			Short source) {
		this.category = category;
		this.updateCount = updateCount;
		this.updatedTime = updatedTime;
		this.createdTime = createdTime;
		this.updateInc = updateInc;
		this.status = status;
		this.lastMsgIdOfTarget = lastMsgIdOfTarget;
		this.lastMsgIdOfOwner = lastMsgIdOfOwner;
		this.sex = sex;
		this.matchCount = matchCount;
		this.maxMatchCount = maxMatchCount;
		this.likedByOwner = likedByOwner;
		this.likedByTarget = likedByTarget;
		this.deletedByOwner = deletedByOwner;
		this.deletedByTarget = deletedByTarget;
		this.source = source;
	}

	/** full constructor */
	public Plane(Account accountByTargetId, Category category,
			Account accountByOwnerId, Integer updateCount,
			Timestamp updatedTime, Timestamp createdTime, Long updateInc,
			Short status, Double longitude, Double latitude,
			Long lastMsgIdOfTarget, Long lastMsgIdOfOwner, String city,
			String province, String country, Short sex, Short matchCount,
			Short maxMatchCount, Short likedByOwner, Short likedByTarget,
			Short deletedByOwner, Short deletedByTarget, Date birthdayLower,
			Date birthdayUpper, String language, Short source, Long lastMsgId,
			List<PlaneHist> planeHists, List<Message> messages) {
		this.accountByTargetId = accountByTargetId;
		this.category = category;
		this.accountByOwnerId = accountByOwnerId;
		this.updateCount = updateCount;
		this.updatedTime = updatedTime;
		this.createdTime = createdTime;
		this.updateInc = updateInc;
		this.status = status;
		this.longitude = longitude;
		this.latitude = latitude;
		this.lastMsgIdOfTarget = lastMsgIdOfTarget;
		this.lastMsgIdOfOwner = lastMsgIdOfOwner;
		this.city = city;
		this.province = province;
		this.country = country;
		this.sex = sex;
		this.matchCount = matchCount;
		this.maxMatchCount = maxMatchCount;
		this.likedByOwner = likedByOwner;
		this.likedByTarget = likedByTarget;
		this.deletedByOwner = deletedByOwner;
		this.deletedByTarget = deletedByTarget;
		this.birthdayLower = birthdayLower;
		this.birthdayUpper = birthdayUpper;
		this.language = language;
		this.source = source;
		this.lastMsgId = lastMsgId;
		this.planeHists = planeHists;
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

	@Column(name = "LAST_MSG_ID_OF_TARGET", nullable = false, updatable = false)
	public Long getLastMsgIdOfTarget() {
		return this.lastMsgIdOfTarget;
	}

	public void setLastMsgIdOfTarget(Long lastMsgIdOfTarget) {
		this.lastMsgIdOfTarget = lastMsgIdOfTarget;
	}

	@Column(name = "LAST_MSG_ID_OF_OWNER", nullable = false, updatable = false)
	public Long getLastMsgIdOfOwner() {
		return this.lastMsgIdOfOwner;
	}

	public void setLastMsgIdOfOwner(Long lastMsgIdOfOwner) {
		this.lastMsgIdOfOwner = lastMsgIdOfOwner;
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

	@Column(name = "COUNTRY")
	public String getCountry() {
		return this.country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	@Column(name = "SEX", nullable = false)
	public Short getSex() {
		return this.sex;
	}

	public void setSex(Short sex) {
		this.sex = sex;
	}

	@Column(name = "MATCH_COUNT", nullable = false, insertable = false, updatable = false)
	public Short getMatchCount() {
		return this.matchCount;
	}

	public void setMatchCount(Short matchCount) {
		this.matchCount = matchCount;
	}

	@Column(name = "MAX_MATCH_COUNT", nullable = false)
	public Short getMaxMatchCount() {
		return this.maxMatchCount;
	}

	public void setMaxMatchCount(Short maxMatchCount) {
		this.maxMatchCount = maxMatchCount;
	}

	@Column(name = "LIKED_BY_OWNER", nullable = false)
	public Short getLikedByOwner() {
		return this.likedByOwner;
	}

	public void setLikedByOwner(Short likedByOwner) {
		this.likedByOwner = likedByOwner;
	}

	@Column(name = "LIKED_BY_TARGET", nullable = false)
	public Short getLikedByTarget() {
		return this.likedByTarget;
	}

	public void setLikedByTarget(Short likedByTarget) {
		this.likedByTarget = likedByTarget;
	}

	@Column(name = "DELETED_BY_OWNER", nullable = false)
	public Short getDeletedByOwner() {
		return this.deletedByOwner;
	}

	public void setDeletedByOwner(Short deletedByOwner) {
		this.deletedByOwner = deletedByOwner;
	}

	@Column(name = "DELETED_BY_TARGET", nullable = false)
	public Short getDeletedByTarget() {
		return this.deletedByTarget;
	}

	public void setDeletedByTarget(Short deletedByTarget) {
		this.deletedByTarget = deletedByTarget;
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

	@Column(name = "SOURCE", nullable = false)
	public Short getSource() {
		return this.source;
	}

	public void setSource(Short source) {
		this.source = source;
	}

	@Column(name = "LAST_MSG_ID", updatable = false)
	public Long getLastMsgId() {
		return this.lastMsgId;
	}

	public void setLastMsgId(Long lastMsgId) {
		this.lastMsgId = lastMsgId;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "plane")
	public List<PlaneHist> getPlaneHists() {
		return this.planeHists;
	}

	public void setPlaneHists(List<PlaneHist> planeHists) {
		this.planeHists = planeHists;
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

		setUpdatedTime(timestamp);

		setCreatedTime(timestamp);

	}

	/*
	 * @PreUpdate protected void onPreUpdate(){ Timestamp timestamp = new
	 * Timestamp(System.currentTimeMillis()); setUpdatedTime(timestamp); }
	 */

}