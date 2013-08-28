package com.airogami;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Map;
import java.util.TreeMap;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.airogami.exception.AirogamiException;
import com.airogami.persistence.entities.Account;
import com.airogami.persistence.entities.Authenticate;
import com.airogami.persistence.entities.Category;
import com.airogami.persistence.entities.Chain;
import com.airogami.persistence.entities.ChainMessage;
import com.airogami.persistence.entities.Message;
import com.airogami.persistence.entities.Plane;
import com.airogami.persistence.entities.Profile;
import com.airogami.persistence.entities.Report;
import com.airogami.presentation.logic.ManagerUtils;

public class TestAccount {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Ignore
	@Test
	public void testSignup() {		
		try {
			for(int i=0;i<5;++i){				
				Authenticate authenticate = new Authenticate();				
				authenticate.setEmail("tianhuyang" + i + "@gmail.com");
				
				authenticate.setPassword("12345678");
				Account account = new Account();
				Profile profile = new Profile();
				profile.setFullName("yangtianhu");
				profile.setScreenName("tianhuyang" + i);				
				profile.setSex((short)0);
				profile.setStatus((short)0);
				profile.setCity("shanghai");
				profile.setProvince("shanghai");
				profile.setCountry("China");
				profile.setLatitude(0.0);
				profile.setLongitude(0.0);
				profile.setAccountId(null);
				account.setAuthenticate(authenticate);
				account.setProfile(profile);
				ManagerUtils.accountManager.signup(account);
				ObjectUtils.printObject(account);
			}			
		} catch (AirogamiException e) {
			e.printStackTrace();
			Assert.fail();
		}
	}	
	
	@Ignore
	@Test
	public void testSigninWithScreenName() {
		String screenName = "tianhuyang5";
		String password = "12345678";
		Long uuid = 0L;
		try {
			Account account = ManagerUtils.accountManager.signinWithScreenName(screenName, password, null);
			ObjectUtils.printObject(account);
		} catch (AirogamiException e) {
			e.printStackTrace();
			Assert.fail();
		}
	}
	@Ignore
	@Test
	public void testSigninWithEmail() {
		String email = "tianhu@qq.com";
		String password = "12345678";
		Long uuid = 0L;
		try {
			Account account = ManagerUtils.accountManager.signinWithEmail(email, password, null);
			ObjectUtils.printObject(account);
		} catch (AirogamiException e) {
			e.printStackTrace();
			Assert.fail();
		}
	}
	
	@Ignore
	@Test
	public void testEditAccount() {		
		try {
			Map<String, Object> properties = new TreeMap<String, Object>();
			properties.put("accountId", 1L);
			properties.put("fullName", "fullName");
			properties.put("screenName", "screenName");
		    properties.put("sex", (short)3);
		    properties.put("status", (short)1);
		    properties.put("icon", "myicon");
		    properties.put("city", "City");
		    properties.put("province", "Province");
		    properties.put("country", "Country");
		    properties.put("latitude", 1.0);
		    properties.put("longitude", 1.0);
		    properties.put("createdTime", new Timestamp(System.currentTimeMillis()));
		    properties.put("updatedTime", new Timestamp(0));
		    properties.put("birthday", new Date(0));
			Profile profile = ManagerUtils.accountManager.editProfile(properties);
			ObjectUtils.printObject(profile);		
		} catch (AirogamiException e) {
			e.printStackTrace();
			Assert.fail();
		}
	}	
	@Ignore
	@Test
	public void testChangePassword() {
		long accountId = 1L;
		String oldPassword = "12345678";
		String newPassword = "12345678";
		try {
			boolean succeed = ManagerUtils.accountManager.changePassword(accountId, oldPassword, newPassword);
			ObjectUtils.printObject(succeed);
		} catch (AirogamiException e) {
			e.printStackTrace();
			Assert.fail();
		}
	}
	@Ignore
	@Test
	public void testChangeScreenName() {
		long accountId = 0L;
		String screenName = "screenName";
		try {
			boolean succeed = ManagerUtils.accountManager.changeScreenName(accountId, screenName);
			ObjectUtils.printObject(succeed);
		} catch (AirogamiException e) {
			e.printStackTrace();
			Assert.fail();
		}
	}
	
	@Ignore
	@Test
	public void testObtainAccount() {
		long accountId = 0L;
		Long updateCount = null;
		try {
			Profile profile = ManagerUtils.accountManager.obtainProfile(accountId, updateCount);
			ObjectUtils.printObject(profile);
		} catch (AirogamiException e) {
			e.printStackTrace();
			Assert.fail();
		}
	}
	@Ignore
	@Test
	public void testAirogamiServie(){
		new Thread(new Runnable(){
			@Override
			public void run() {
				try {
					if(true)sendPlanes();
				} catch (AirogamiException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}			
		}).start();
		
		new Thread(new Runnable(){
			@Override
			public void run() {
				try {
					if(false)sendChains();
				} catch (AirogamiException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}			
		}).start();
		
		
		new Thread(new Runnable(){
			@Override
			public void run() {
				try {
					if(false){
						ManagerUtils.planeManager.throwPlane(1, 5);
					}
				} catch (AirogamiException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}			
		}).start();
		
		new Thread(new Runnable(){
			@Override
			public void run() {
				try {
					if(false){
						ManagerUtils.chainManager.throwChain(1, 4);
					}
				} catch (AirogamiException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}			
		}).start();
		
		try {
			Thread.sleep(1000000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public  void sendPlanes() throws AirogamiException{
    	for(int i = 0; i < 5; ++i){
    		for(int j = 0; j < 1; ++j){
    			Plane plane = new Plane();
        		//plane.setCity("chengdu");
        		//plane.setProvince("sichuan");
        		//plane.setCountry("Japan");
        		plane.setLatitude(0.0);
        		plane.setLongitude(0.0);
        		plane.setCategory(new Category());
        		plane.getCategory().setCategoryId((short)1);
        		long ownerId = i + 1;
        		Message message = new Message();
        		message.setContent("hello" + i);
                message.setType((short) 0);
        		plane.getMessages().add(message);
        		ManagerUtils.planeManager.sendPlane(plane, ownerId);
    		}
    	}		
    	
	}
	
	public  void sendChains() throws AirogamiException{
    	for(int i = 0; i < 1; ++i){
    		for(int j = 0; j < 1; ++j){
    			Chain chain = new Chain();
    			//chain.setCity("shanghai");
    			//chain.setProvince("shanghai");
    			chain.setCountry("Japan");
    			//chain.setMessageCount((short) 0);
    			long ownerId = i + 1;
    			ChainMessage chainMessage = new ChainMessage();
    			chainMessage.setContent("hello! " + j);
    			chainMessage.setType((short) 0);
    			chain.getChainMessages().add(chainMessage);
                ManagerUtils.chainManager.sendChain(chain, ownerId);
    		}
    	}
	}
	
	@Ignore
	@Test
	public void testReportAccount() {
		long reportId = 10;
		long reportedId = 2;
		String reason = "Good 1 words";
		try {			
			Report report = ManagerUtils.accountManager.reportAccount(reportId, reportedId, reason);
			ObjectUtils.printObject(report);		
		} catch (AirogamiException e) {
			e.printStackTrace();
			Assert.fail();
		}
	}	

}
