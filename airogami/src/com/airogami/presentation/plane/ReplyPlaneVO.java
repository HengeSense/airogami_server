package com.airogami.presentation.plane;

public class ReplyPlaneVO {

	private Long planeId;
	private MessageVO messageVO;
	private Boolean byOwner;
	
	public Long getPlaneId() {
		return planeId;
	}
	public void setPlaneId(Long planeId) {
		this.planeId = planeId;
	}
	public MessageVO getMessageVO() {
		return messageVO;
	}
	public void setMessageVO(MessageVO messageVO) {
		this.messageVO = messageVO;
	}
	public Boolean getByOwner() {
		return byOwner;
	}
	public void setByOwner(Boolean byOwner) {
		this.byOwner = byOwner;
	}	
}
