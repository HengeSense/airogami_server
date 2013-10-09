package com.airogami;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.airogami.application.ServiceUtils;
import com.airogami.application.exception.ApplicationException;

public class TestAccountPersistence {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testupdateAccountStat() {		
		try {
			int count = ServiceUtils.accountService.updateAccountStat();	
			System.out.println("count = " + count);
		} catch (ApplicationException e) {
			e.printStackTrace();
			Assert.fail();
		}
	}	

}
