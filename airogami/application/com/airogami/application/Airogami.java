package com.airogami.application;

import java.util.Map;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

import com.airogami.persistence.entities.Chain;
import com.airogami.persistence.entities.Plane;

public class Airogami implements Delayed{

	public static final byte TypePlane = 0;
	public static final byte TypeChain = 1;
	public static final byte MaxExceptionCount = 3;
	public static long wait = 20 * 1000;//60s
	
	private long id;
	private byte type;
	private byte exceptionCount;// matched exception count 
	private long end;
	
	public Airogami(long id, byte type){
		this.id = id;
		this.type = type;
		end = wait + System.currentTimeMillis();
	}
	
	public void reset(){
	    end = wait + System.currentTimeMillis();
	}
	
	public String toString(){
		StringBuffer stringBuffer = new StringBuffer(20);
		if(type == TypePlane){
			stringBuffer.append("Plane: ");
		}
		else if(type == TypeChain){
			stringBuffer.append("Chain: ");
		}
		stringBuffer.append(id);
		stringBuffer.append(" [");
		stringBuffer.append("exceptionCount = ");
		stringBuffer.append(exceptionCount);
		stringBuffer.append("]");
		return stringBuffer.toString();
	}
	
	// return whether needs rematching
	public boolean match(){
		Map<String, Object> result;
		Long accountId;
		boolean rematch = false;
		if(type == TypePlane){
			try {
				result = ServiceUtils.planeService.matchPlane(id);
				Plane plane = (Plane)result.get("plane");
				if(plane != null){
					rematch = true;
				}
				else{
					//match succeed
					accountId = (Long)result.get("accountId");
				}				
			     exceptionCount = 0;
			} catch (Throwable t) {
				t.printStackTrace();
				if(++exceptionCount <= MaxExceptionCount)
				{
					rematch = true;
				}
			}
		}
		else if(type == TypeChain){
			
			try {
				result = ServiceUtils.chainService.matchChain(id);
				Chain chain = (Chain)result.get("chain");
				if(chain != null){
					rematch = true;
				}
				else{
					//match succeed
					accountId = (Long)result.get("accountId");
				}
				exceptionCount = 0;
			} catch (Throwable t) {
				t.printStackTrace();
				if(++exceptionCount <= MaxExceptionCount)
				{
					rematch = true;
				}
			}
		}
		return rematch;
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
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

	@Override
	public int compareTo(Delayed o) {
		Airogami airogami = (Airogami)o;
		int ret = 0;
		if(this.end > airogami.end){
			ret = 1;
		}
		else if(this.end < airogami.end){
			ret = -1;
		}
		return ret;
	}

	@Override
	public long getDelay(TimeUnit unit) {
		return unit.convert(end - System.currentTimeMillis(), TimeUnit.MILLISECONDS); 
	}
	
}
