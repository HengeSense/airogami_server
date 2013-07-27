package com.airogami.presentation.logic;

public class Notification {

	public static final int TypeReceivedPlanes = 1;
	public static final int TypeReceivedChains = 2;
	public static final int TypeReceivedPlaneMessages = 3;
	public static final int TypeReceivedChainMessages = 4;

	private int type;
	private String source;
	private String message;
	
	public Notification(int type, String source, String message){
		
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	
}
