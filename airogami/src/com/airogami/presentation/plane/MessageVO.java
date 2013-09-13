package com.airogami.presentation.plane;

public class MessageVO {

	private String content;
	private Short type;
	
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
	
}
