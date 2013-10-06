package com.airogami.common;

import java.util.List;

public class ChainMessageNotifiedInfos extends NotifiedInfo {

	private String name;
	private String content;
	private List<NotifiedInfo> notifiedInfos;
	
	public ChainMessageNotifiedInfos(List<NotifiedInfo> notifiedInfos, String name, String content){
		this.notifiedInfos = notifiedInfos;
		this.name = name;
		this.content = content;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public List<NotifiedInfo> getNotifiedInfos(){
		return notifiedInfos;
	}

	public void setNotifiedInfos(List<NotifiedInfo> notifiedInfos) {
		this.notifiedInfos = notifiedInfos;
	}
}
