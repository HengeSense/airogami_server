package com.airogami;

import static org.junit.Assert.*;
import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.airogami.common.constants.AccountConstants;
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
			for(int i=0;i<6;++i){				
				Authenticate authenticate = new Authenticate();				
				authenticate.setEmail("tianhuyang" + i + "@gmail.com");
				authenticate.setPassword("12345678");
				Account account = new Account();
				account.setFullName("yangtianhu");
				account.setAuthenticate(authenticate);
				account.setSex((short)0);
				account.setStatus((short)0);
				account.setIcon("icon");
				account.setCity("shanghai");
				account.setProvince("shanghai");
				account.setCountry("China");
				account.setAltitude(0.0);
				account.setLongitude(0.0);
				account.setAccountId(null);
				ManagerUtils.accountManager.signup(account);
			}			
		} catch (AirogamiException e) {
			e.printStackTrace();
			Assert.fail();
		}
	}	
	
	@Ignore
	@Test
	public void testSignin() {
		String email = "tianhuyang0@gmail.com";
		String password = "12345678";
		int type = AccountConstants.AuthenticateTypeEmail;
		try {
			Account account = ManagerUtils.accountManager.signin(new String[]{email,password},type);
			ObjectUtils.printObject(account);
		} catch (AirogamiException e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

}
