package com.airogami;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.airogami.exception.AirogamiException;
import com.airogami.persitence.entities.Account;
import com.airogami.persitence.entities.Authenticate;
import com.airogami.presentation.logic.ManagerUtils;

public class TestAccount {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}
	
	
	@Test
	public void testSignup() {		
		try {
			for(int i=0;i<5;++i){				
				Authenticate authenticate = new Authenticate();				
				authenticate.setEmail("tianhuyang" + i + "@gmail.com");
				
				authenticate.setPassword("12345678");
				Account account = new Account();
				account.setFullName("yangtianhu");
				account.setScreenName("tianhuyang" + i);
				account.setAuthenticate(authenticate);
				account.setSex((short)0);
				account.setStatus((short)0);
				account.setIcon("icon");
				account.setCity("shanghai");
				account.setProvince("shanghai");
				account.setCountry("China");
				account.setLatitude(0.0);
				account.setLongitude(0.0);
				account.setAccountId(null);
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
		try {
			Account account = ManagerUtils.accountManager.signinWithScreenName(screenName, password);
			ObjectUtils.printObject(account);
		} catch (AirogamiException e) {
			e.printStackTrace();
			Assert.fail();
		}
	}
	@Ignore
	@Test
	public void testSigninWithEmail() {
		String email = "tianhuyang0@gmail.com";
		String password = "12345678";
		try {
			Account account = ManagerUtils.accountManager.signinWithEmail(email, password);
			ObjectUtils.printObject(account);
		} catch (AirogamiException e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

}
