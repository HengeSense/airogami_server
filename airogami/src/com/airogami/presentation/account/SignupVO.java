package com.airogami.presentation.account;

import java.util.Date;

import com.airogami.presentation.logic.ClientAgent;

public class SignupVO {

	private String email;
	private String password;
	private Short sex;
	private String fullName;
	private Date birthday;
	private Double longitude;
	private Double latitude;
	private String city;
	private String province;
	private String country;	
	private String shout;
	private ClientAgent clientAgent = new ClientAgent();
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
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
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	public String getShout() {
		return shout;
	}
	public void setShout(String shout) {
		this.shout = shout;
	}
	public ClientAgent getClientAgent() {
		return clientAgent;
	}
	public void setClientAgent(ClientAgent clientAgent) {
		this.clientAgent = clientAgent;
	}	
}
