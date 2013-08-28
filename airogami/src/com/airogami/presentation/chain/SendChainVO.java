package com.airogami.presentation.chain;

import java.sql.Date;
import java.util.Calendar;

public class SendChainVO {
	
	private ChainMessageVO chainMessageVO;
	private Double longitude;
	private Double latitude;
	private String city;
	private String province;
	private String country;
	private Short sex;
	private Date birthdayLower;
	private Date birthdayUpper;
	private String language;
	private static int MinimumAge = 13;
	private static int MaximumAge = 99;
	
	public SendChainVO(){
		chainMessageVO = new ChainMessageVO();
	}
	
	public ChainMessageVO getChainMessageVO() {
		return chainMessageVO;
	}
	public void setChainMessageVO(ChainMessageVO chainMessageVO) {
		this.chainMessageVO = chainMessageVO;
	}
	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public Short getSex() {
		return sex;
	}
	public void setSex(Short sex) {
		this.sex = sex;
	}
	
	public Date getBirthdayLower() {
		return birthdayLower;
	}

	public void setBirthdayLower(int maxAge) {
		if(maxAge <= MaximumAge && maxAge >= MinimumAge){
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.YEAR, -(maxAge + 1));
			calendar.add(Calendar.DAY_OF_MONTH, 1);
			this.birthdayLower = new Date(calendar.getTimeInMillis());
		}
		
	}

	public Date getBirthdayUpper() {
		return birthdayUpper;
	}

	public void setBirthdayUpper(int minAge) {
		if(minAge <= MaximumAge && minAge >= MinimumAge){
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.YEAR, -minAge);
			this.birthdayUpper = new Date(calendar.getTimeInMillis());
		}
		
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}
}
