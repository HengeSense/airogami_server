package com.airogami.presentation.plane;

public class SendPlaneVO {
	
	private CategoryVO categoryVO;
	private MessageVO messageVO;
	private Double longitude;
	private Double latitude;
	private String city;
	private String province;
	private String country;
	private Short sex;
	
	public SendPlaneVO(){
		categoryVO = new CategoryVO();
		messageVO = new MessageVO();
	}
	
	public CategoryVO getCategoryVO() {
		return categoryVO;
	}
	public void setCategoryVO(CategoryVO categoryVO) {
		this.categoryVO = categoryVO;
	}
	public MessageVO getMessageVO() {
		return messageVO;
	}
	public void setMessageVO(MessageVO messageVO) {
		this.messageVO = messageVO;
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
