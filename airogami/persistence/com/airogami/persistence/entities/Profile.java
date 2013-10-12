package com.airogami.persistence.entities;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import org.apache.struts2.json.annotations.JSON;

/**
 * Profile entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "PROFILE", catalog = "Airogami", uniqueConstraints = @UniqueConstraint(columnNames = "SCREEN_NAME"))
public class Profile implements java.io.Serializable {

	// Fields
	private static final long serialVersionUID = 1L;

	private Integer accountId;

	private Account account;

	private String fullName;

	private String screenName;

	private Short sex = (short) 0;

	private Double longitude;

	private Double latitude;

	private Short status = 0;

	private Timestamp createdTime;

	private String city;

	private String province;

	private String country;

	private Date birthday;

	private Integer updateCount = 0;

	private Integer likesCount = 0;

	private String shout;

	private String language;

	// Constructors

	/** default constructor */
	public Profile() {
	}

	/** minimal constructor */
	public Profile(Integer accountId, Account account, String fullName,
			Short sex, Double longitude, Double latitude, Short status,
			Timestamp createdTime, String city, String province,
			String country, Date birthday, Integer updateCount,
			Integer likesCount) {
		this.accountId = accountId;
		this.account = account;
		this.fullName = fullName;
		this.sex = sex;
		this.longitude = longitude;
		this.latitude = latitude;
		this.status = status;
		this.createdTime = createdTime;
		this.city = city;
		this.province = province;
		this.country = country;
		this.birthday = birthday;
		this.updateCount = updateCount;
		this.likesCount = likesCount;
	}

	/** full constructor */
	public Profile(Integer accountId, Account account, String fullName,
			String screenName, Short sex, Double longitude, Double latitude,
			Short status, Timestamp createdTime, String city, String province,
			String country, Date birthday, Integer updateCount,
			Integer likesCount, String shout, String language) {
		this.accountId = accountId;
		this.account = account;
		this.fullName = fullName;
		this.screenName = screenName;
		this.sex = sex;
		this.longitude = longitude;
		this.latitude = latitude;
		this.status = status;
		this.createdTime = createdTime;
		this.city = city;
		this.province = province;
		this.country = country;
		this.birthday = birthday;
		this.updateCount = updateCount;
		this.likesCount = likesCount;
		this.shout = shout;
		this.language = language;
	}

	// Property accessors
	@Id
	@Column(name = "ACCOUNT_ID", unique = true, nullable = false)
	public Integer getAccountId() {
		return this.accountId;
	}

	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
	}

	@OneToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn
	public Account getAccount() {
		return this.account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	@Column(name = "FULL_NAME", nullable = false, length = 70)
	public String getFullName() {
		return this.fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	@Column(name = "SCREEN_NAME", unique = true, length = 32)
	public String getScreenName() {
		return this.screenName;
	}

	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}

	@Column(name = "SEX", nullable = false)
	public Short getSex() {
		return this.sex;
	}

	public void setSex(Short sex) {
		this.sex = sex;
	}

	@Column(name = "LONGITUDE", nullable = false, precision = 10, scale = 6)
	public Double getLongitude() {
		return this.longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	@Column(name = "LATITUDE", nullable = false, precision = 10, scale = 6)
	public Double getLatitude() {
		return this.latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	@Column(name = "STATUS", nullable = false)
	public Short getStatus() {
		return this.status;
	}

	public void setStatus(Short status) {
		this.status = status;
	}

	@Column(name = "CREATED_TIME", nullable = false, updatable = false, length = 19)
	@JSON(format = "yyyy-MM-dd HH:mm:ss")
	public Timestamp getCreatedTime() {
		return this.createdTime;
	}

	public void setCreatedTime(Timestamp createdTime) {
		this.createdTime = createdTime;
	}

	@Column(name = "CITY", nullable = false)
	public String getCity() {
		return this.city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@Column(name = "PROVINCE", nullable = false)
	public String getProvince() {
		return this.province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	@Column(name = "COUNTRY", nullable = false)
	public String getCountry() {
		return this.country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	@Column(name = "BIRTHDAY", nullable = false, length = 10)
	@JSON(format = "yyyy-MM-dd HH:mm:ss")
	public Date getBirthday() {
		return this.birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	@Column(name = "UPDATE_COUNT", nullable = false, insertable = false, updatable = false)
	public Integer getUpdateCount() {
		return this.updateCount;
	}

	public void setUpdateCount(Integer updateCount) {
		this.updateCount = updateCount;
	}

	@Column(name = "LIKES_COUNT", nullable = false, insertable = false, updatable = false)
	public Integer getLikesCount() {
		return this.likesCount;
	}

	public void setLikesCount(Integer likesCount) {
		this.likesCount = likesCount;
	}

	@Column(name = "SHOUT")
	public String getShout() {
		return this.shout;
	}

	public void setShout(String shout) {
		this.shout = shout;
	}

	@Column(name = "LANGUAGE")
	public String getLanguage() {
		return this.language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	@PrePersist
	protected void onPrePersist() {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());

		setCreatedTime(timestamp);

	}

	/**/

}