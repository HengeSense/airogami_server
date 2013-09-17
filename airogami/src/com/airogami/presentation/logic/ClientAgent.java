package com.airogami.presentation.logic;

public class ClientAgent {
	
	public static final byte DeviceTypeIOS = 1;
	public static final byte DeviceTypeAndroid = 2;
	private byte deviceType;
	private byte clientVersion;
	private String deviceToken;
	
	
	public byte getDeviceType() {
		return deviceType;
	}
	public void setDeviceType(byte deviceType) {
		this.deviceType = deviceType;
	}
	public byte getClientVersion() {
		return clientVersion;
	}
	public void setClientVersion(byte clientVersion) {
		this.clientVersion = clientVersion;
	}
	public String getDeviceToken() {
		return deviceToken;
	}
	public void setDeviceToken(String deviceToken) {
		this.deviceToken = deviceToken;
	}
	
}
