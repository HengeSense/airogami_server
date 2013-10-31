package com.airogami.presentation.plane;

public class MessageVO {

	private String content = "";
	private Short type;
	private String link;
	private int prop;
	
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content.replace("\r", "").replace("\n", "");
	}
	public Short getType() {
		return type;
	}
	public void setType(Short type) {
		this.type = type;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public int getProp() {
		return prop;
	}
	public void setProp(int prop) {
		this.prop = prop;
	}
	
}
