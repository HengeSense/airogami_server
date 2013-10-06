package com.airogami.common;

public class MessageNotifiedInfo extends NotifiedInfo {

	private Integer accountId;
	private Long messagesCount;
	private String name, content;
	
	public MessageNotifiedInfo(){}
	
	public MessageNotifiedInfo(Integer accountId, Long messagesCount, String name){
		this.accountId = accountId;
		this.messagesCount = messagesCount;
		this.name = name;
	}
	
	public Integer getAccountId() {
		return accountId;
	}

	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
	}

	public Long getMessagesCount() {
		return messagesCount;
	}
	public void setMessagesCount(Long messagesCount) {
		this.messagesCount = messagesCount;
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
		if(content.length() > MessageLength){
			StringBuffer stringBuffer = new StringBuffer(MessageLength);
			stringBuffer.append(content.substring(0, MessageLength - 3));
			stringBuffer.append("...");
			content = stringBuffer.toString();
		}
		this.content = content;
	}
}
