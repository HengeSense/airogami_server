package com.airogami.application;

import java.util.concurrent.DelayQueue;

public class AirogamiService extends Thread{

	private DelayQueue<Airogami> airogamiQueque = new DelayQueue<Airogami>();

	public AirogamiService(){
		this.start();
	}
	
	@Override
	public void run() {
		while(true){
			try{
				Airogami airogami = airogamiQueque.take();
				System.out.println("Taking > " + airogami);
				if(airogami.match()){
					airogami.reset();
					airogamiQueque.add(airogami);
					//System.out.println("Rematching > " + airogami);
				}
			}
			catch(Throwable t){
				t.printStackTrace();
			}
		}		
	}
	
	public void appendPlane(long planeId){
		Airogami airogami = new Airogami(planeId, Airogami.TypePlane);
		airogamiQueque.add(airogami);
		//System.out.println("Appending > " + airogami);
	}
	
    public void appendChain(long chainId){
    	Airogami airogami = new Airogami(chainId, Airogami.TypeChain);
		airogamiQueque.add(airogami);
		//System.out.println("Appending > " + airogami);
	}
}
