package com.airogami.presentation.logic;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import com.airogami.application.ServiceUtils;
import com.airogami.application.exception.ApplicationException;

public class UpdateManager {
	
	//private final long period = 5 * 1000;
	private final long period = 24 * 3600 * 1000;
	private Timer timer = new Timer(true);
	private TimerTask timerTask;
	
	public UpdateManager(){
		timerTask = new TimerTask() {
			@Override
			public void run() {
				int exceptionCount = 3;
				while(true){
					System.out.println("updateAccountStating ...");
					try {
						ServiceUtils.accountService.updateAccountStat();
						break;
					} catch (ApplicationException ae) {
						ae.printStackTrace();
						--exceptionCount;
						
					} catch (Throwable t) {
						t.printStackTrace();
						--exceptionCount;
					}
					
					if(exceptionCount < 0){
						break;
					}
				}
			}
		};
		
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        //System.out.println(calendar.getTime());
        
        long delay = calendar.getTimeInMillis() - System.currentTimeMillis();
        //delay = 1000;
		timer.scheduleAtFixedRate(timerTask, delay, period);
	}
}
