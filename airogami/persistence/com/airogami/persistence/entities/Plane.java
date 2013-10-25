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

	private Long ownerInc = 0L;

	private Long targetInc = 0L;

	private Short status = 0;

	private Integer updateCount = 0;

	private Long lastMsgIdOfT = 0L;

	private Long lastMsgIdOfO = 0L;

	private Long neoMsgIdOfT = 0L;

	private Long neoMsgIdOfO = 0L;

	private Long clearMsgId = 0L;

	private Timestamp updatedTime;

	private Timestamp createdTime;

	private Short source = (short) 0;

	private Double longitude;

	private Double latitude;

	private Short sex = (short) 0;

	private Short matchCount = 0;

	private Short maxMatchCount = (short) 0;

	private Short likedByO = (short) 0;

	private Short likedByT = (short) 0;

	private Short deletedByO = (short) 0;

	private Short deletedByT = (short) 0;

	private Date birthdayLower;

	private Date birthdayUpper;

	private Long tmpMsgId = 0L;

	private String city;

	private String province;

	private String country;

	private String language;

	private List<PlaneHist> planeHists = new ArrayList<PlaneHist>(0);

	private List<Message> messages = new ArrayList<Message>(0);

	// Constructors

	/** default constructor */
	public Plane() {
	}

	/** minimal constructor */
	public Plane(Category category, Long ownerInc, Long targetInc,
			Short status, Integer updateCount, Long lastMsgIdOfT,
			Long lastMsgIdOfO, Long neoMsgIdOfT, Long neoMsgIdOfO,
			Long clearMsgId, Timestamp updatedTime, Timestamp createdTime,
			Short source, Short sex, Short matchCount, Short maxMatchCount,
			Short likedByO, Short likedByT, Short deletedByO, Short deletedByT) {
		this.category = category;
		this.ownerInc = ownerInc;
		this.targetInc = targetInc;
		this.status = status;
		this.updateCount = updateCount;
		this.lastMsgIdOfT = lastMsgIdOfT;
		this.lastMsgIdOfO = lastMsgIdOfO;
		this.neoMsgIdOfT = neoMsgIdOfT;
		this.neoMsgIdOfO = neoMsgIdOfO;
		this.clearMsgId = clearMsgId;
		this.updatedTime = updatedTime;
		this.createdTime = createdTime;
		this.source = source;
		this.sex = sex;
		this.matchCount = matchCount;
		this.maxMatchCount = maxMatchCount;
		this.likedByO = likedByO;
		this.likedByT = likedByT;
		this.deletedByO = deletedByO;
		this.deletedByT = deletedByT;
	}

	/** full constructor */
	public Plane(Account accountByTargetId, Category category,
			Account accountByOwnerId, Long ownerInc, Long targetInc,
			Short status, Integer updateCount, Long lastMsgIdOfT,
			Long lastMsgIdOfO, Long neoMsgIdOfT, Long neoMsgIdOfO,
			Long clearMsgId, Timestamp updatedTime, Timestamp createdTime,
			Short source, Double longitude, Double latitude, Short sex,
			Short matchCount, Short maxMatchCount, Short likedByO,
			Short likedByT, Short deletedByO, Short deletedByT,
			Date birthdayLower, Date birthdayUpper, Long tmpMsgId, String city,
			String province, String country, String language,
			List<PlaneHist> planeHists, List<Message> messages) {
		this.accountByTargetId = accountByTargetId;
		this.category = category;
		this.accountByOwnerId = accountByOwnerId;
		this.ownerInc = ownerInc;
		this.targetInc = targetInc;
		this.status = status;
		this.updateCount = updateCount;
		this.lastMsgIdOfT = lastMsgIdOfT;
		this.lastMsgIdOfO = lastMsgIdOfO;
		this.neoMsgIdOfT = neoMsgIdOfT;
		this.neoMsgIdOfO = neoMsgIdOfO;
		this.clearMsgId = clearMsgId;
		this.updatedTime = updatedTime;
		this.createdTime = createdTime;
		this.source = source;
		this.longitude = longitude;
		this.latitude = latitude;
		this.sex = sex;
		this.matchCount = matchCount;
		this.maxMatchCount = maxMatchCount;
		this.likedByO = likedByO;
		this.likedByT = likedByT;
		this.deletedByO = deletedByO;
		this.deletedByT = deletedByT;
		this.birthdayLower = birthdayLower;
		this.birthdayUpper = birthdayUpper;
		this.tmpMsgId = tmpMsgId;
		this.city = city;
		this.province = province;
		this.country = country;
		this.language = language;
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

	@Column(name = "OWNER_INC", nullable = false, insertable = false, updatable = false)
	public Long getOwnerInc() {
		return this.ownerInc;
	}

	public void setOwnerInc(Long ownerInc) {
		this.ownerInc = ownerInc;
	}

	@Column(name = "TARGET_INC", nullable = false, insertable = false, updatable = false)
	public Long getTargetInc() {
		return this.targetInc;
	}

	public void setTargetInc(Long targetInc) {
		this.targetInc = targetInc;
	}

	@Column(name = "STATUS", nullable = false)
	public Short getStatus() {
		return this.status;
	}

	public void setStatus(Short status) {
		this.status = status;
	}

	@Column(name = "UPDATE_COUNT", nullable = false, insertable = false, updatable = false)
	public Integer getUpdateCount() {
		return this.updateCount;
	}

	public void setUpdateCount(Integer updateCount) {
		this.updateCount = updateCount;
	}

	@Column(name = "LAST_MSG_ID_OF_T", nullable = false, updatable = false)
	public Long getLastMsgIdOfT() {
		return this.lastMsgIdOfT;
	}

	public void setLastMsgIdOfT(Long lastMsgIdOfT) {
		this.lastMsgIdOfT = lastMsgIdOfT;
	}

	@Column(name = "LAST_MSG_ID_OF_O", nullable = false, updatable = false)
	public Long getLastMsgIdOfO() {
		return this.lastMsgIdOfO;
	}

	public void setLastMsgIdOfO(Long lastMsgIdOfO) {
		this.lastMsgIdOfO = lastMsgIdOfO;
	}

	@Column(name = "NEO_MSG_ID_OF_T", nullable = false)
	public Long getNeoMsgIdOfT() {
		return this.neoMsgIdOfT;
	}

	public void setNeoMsgIdOfT(Long neoMsgIdOfT) {
		this.neoMsgIdOfT = neoMsgIdOfT;
	}

	@Column(name = "NEO_MSG_ID_OF_O", nullable = false)
	public Long getNeoMsgIdOfO() {
		return this.neoMsgIdOfO;
	}

	public void setNeoMsgIdOfO(Long neoMsgIdOfO) {
		this.neoMsgIdOfO = neoMsgIdOfO;
	}

	@Column(name = "CLEAR_MSG_ID", nullable = false)
	public Long getClearMsgId() {
		return this.clearMsgId;
	}

	public void setClearMsgId(Long clearMsgId) {
		this.clearMsgId = clearMsgId;
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

	@Column(name = "SOURCE", nullable = false)
	public Short getSource() {
		return this.source;
	}

	public void setSource(Short source) {
		this.source = source;
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

	@Column(name = "LIKED_BY_O", nullable = false)
	public Short getLikedByO() {
		return this.likedByO;
	}

	public void setLikedByO(Short likedByO) {
		this.likedByO = likedByO;
	}

	@Column(name = "LIKED_BY_T", nullable = false)
	public Short getLikedByT() {
		return this.likedByT;
	}

	public void setLikedByT(Short likedByT) {
		this.likedByT = likedByT;
	}

	@Column(name = "DELETED_BY_O", nullable = false)
	public Short getDeletedByO() {
		return this.deletedByO;
	}

	public void setDeletedByO(Short deletedByO) {
		this.deletedByO = deletedByO;
	}

	@Column(name = "DELETED_BY_T", nullable = false)
	public Short getDeletedByT() {
		return this.deletedByT;
	}

	public void setDeletedByT(Short deletedByT) {
		this.deletedByT = deletedByT;
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

	@Column(name = "TMP_MSG_ID")
	public Long getTmpMsgId() {
		return this.tmpMsgId;
	}

	public void setTmpMsgId(Long tmpMsgId) {
		this.tmpMsgId = tmpMsgId;
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

	@Column(name = "LANGUAGE")
	public String getLanguage() {
		return this.language;
	}

	public void setLanguage(String language) {
		this.language = language;
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