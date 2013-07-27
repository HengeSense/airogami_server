package com.airogami.presentation.logic;

import java.util.Random;

public class ManagerUtils {

	public static final AccountManager accountManager = new AccountManager();
	public static final PlaneManager planeManager = new PlaneManager();
	public static final ChainManager chainManager = new ChainManager();
	public static final NotificationManager notificationManager = new NotificationManager();
	
	private static final int rand[] = {3, 2, 2, 1, 1, 1};
    private static Random random = new Random(System.currentTimeMillis());
    //count for plane and chain
    public static int[] pickupCount(){
    	int count = rand[Math.abs(random.nextInt()) % 6];
    	int[] counts = new int [2];
    	counts[0] = Math.abs(random.nextInt()) % (count + 1);
    	counts[1] = count - counts[0];
    	return counts;
    }
    
    public static void main(String args[]){
    	for(int i = 0; i < 20; ++i){
    		int[] counts = pickupCount();
    		System.out.println(counts[0] + " " + counts[1]);
    	}
    	
    }
}
