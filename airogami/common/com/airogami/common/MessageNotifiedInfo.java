package com.airogami.common;

public class MessageNotifiedInfo extends NotifiedInfo {

	private Integer accountId;
	private Integer messagesCount;
	private String name, content;
	
	public MessageNotifiedInfo(){}
	
	public MessageNotifiedInfo(Integer accountId, Long messagesCount, String name){
		this.accountId = accountId;
		this.messagesCount = (int)(long)messagesCount;
		this.name = name;
	}
	
	public MessageNotifiedInfo(Integer accountId, Integer messagesCount, String name){
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

	public Integer getMessagesCount() {
		return messagesCount;
	}
	public void setMessagesCount(Integer messagesCount) {
		this.messagesCount = messagesCount;
	}
	public void setMessagesCount(Long messagesCount) {
		if(messagesCount == null){
			this.messagesCount = null;
		}
		else{
			this.messagesCount = (int)(long)messagesCount;
		}
		
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
