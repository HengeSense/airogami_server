package com.airogami.presentation.chain;

public class ChainMessageVO {

	private String content = "";
	private Short type;
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
	public int getProp() {
		return prop;
	}
	public void setProp(int prop) {
		this.prop = prop;
	}
	
}
