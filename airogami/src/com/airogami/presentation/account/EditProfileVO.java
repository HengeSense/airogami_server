package com.airogami.presentation.account;

import java.io.File;
import java.sql.Timestamp;
import java.sql.Date;

public class EditProfileVO {

	private Short sex;
	private String fullName;
	private Date birthday;
	private Double longitude;
	private Double latitude;
	private String city;
	private String province;
	private String country;	
	private String shout;
	
	public Short getSex() {
		return sex;
	}
	public void setSex(Short sex) {
		this.sex = sex;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName.replace("\n", "").replace("\r", "");
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
		this.city = city.replace("\n", "").replace("\r", "");
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province.replace("\n", "").replace("\r", "");
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country.replace("\n", "").replace("\r", "");
	}
	public Date getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) { 
		if(birthday != null && birthday.length() > 0){
			try{
				this.birthday = Date.valueOf(birthday.substring(0, 10));
			}
			catch(IllegalArgumentException lae){}	
		}
	}
	
	public String getShout() {
		return shout;
	}
	public void setShout(String shout) {
		this.shout = shout;
	}
	
}
