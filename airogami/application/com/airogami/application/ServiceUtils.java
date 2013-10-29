package com.airogami.application;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Random;
import java.text.SimpleDateFormat;

public class ServiceUtils {

	public static final IAccountService accountService = new AccountService();
	public static final IPlaneService planeService = new PlaneService();
	public static final IChainService chainService = new ChainService();
	public static final AirogamiService airogamiService = new AirogamiService();
	public static final IDataService dataService = new DataService();
	
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
    
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    public static String format(Date date){
    	return simpleDateFormat.format(date);
    }
}
