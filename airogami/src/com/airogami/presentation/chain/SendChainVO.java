package com.airogami.presentation.chain;

public class SendChainVO {
	
	private ChainMessageVO chainMessageVO;
	private Double longitude;
	private Double latitude;
	private String city;
	private String province;
	private String country;
	private Short sex;
	
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
	
	
}
