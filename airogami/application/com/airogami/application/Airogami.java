package com.airogami.application;

public class Airogami {

	public static final byte AirogamiTypePlane = 0;
	public static final byte AirogamiTypeChain = 1;
	private Object item;
	private byte type;
	private byte exceptionCount;// matched exception count 
	
	public Airogami(Object item, byte type){
		this.item = item;
		this.type = type;
	}
	
	public boolean match(){
		if(type == AirogamiTypePlane){
			
		}
		else if(type == AirogamiTypeChain){
			
		}
		return false;
	}
	
	public Object getItem() {
		return item;
	}
	public void setItem(Object airogami) {
		this.item = airogami;
	}
	public byte getType() {
		return type;
	}
	public void setType(byte type) {
		this.type = type;
	}
	public byte getExceptionCount() {
		return exceptionCount;
	}
	public void setExceptionCount(byte exceptionCount) {
		this.exceptionCount = exceptionCount;
	}
	
}
