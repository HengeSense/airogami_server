package com.airogami;

import static org.junit.Assert.*;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.airogami.application.ServiceUtils;
import com.airogami.application.exception.ApplicationException;
import com.airogami.persistence.daos.DaoUtils;
import com.airogami.persistence.entities.Account;
import com.airogami.persistence.entities.Category;
import com.airogami.persistence.entities.Chain;
import com.airogami.persistence.entities.ChainMessage;

public class TestPersistenceChain {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Ignore
	@Test
	public void testMatchChain() {
		try {
			long chainId = 21;
			Map<String, Object> result;
			result = ServiceUtils.chainService.matchChain(chainId);
			ObjectUtils.printObject(result);
		} catch (ApplicationException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Ignore
	@Test
	public void testrandChainAccount() {
		try {

			for(int i = 0; i < 10; ++i){
				//System.out.println(DaoUtils.accountDao.randChainAccount(21));
			   //System.out.println(DaoUtils.accountDao.randChainAccount(21, "China"));
				//System.out.println(DaoUtils.accountDao.randChainAccount(21, "China"));
				//System.out.println(DaoUtils.accountDao.randChainAccount(21, "China", "Zhejiang"));
				System.out.println(DaoUtils.accountDao.randChainAccount(1L));
				
			}
			
		} catch (RuntimeException re) {
			re.printStackTrace();
			fail();
		}
	}

	@Ignore
	@Test
	public void testSendChain() {
		Chain chain = new Chain();
		chain.setCity("shanghai");
		chain.setProvince("shanghai");
		chain.setCountry("China");
		long ownerId = 4L;
		ChainMessage chainMessage = new ChainMessage();
		chainMessage.setContent("hello!");
		chainMessage.setType((short) 0);
		chain.getChainMessages().add(chainMessage);
		chainMessage.setChain(chain);
		try {
			chain = ServiceUtils.chainService.sendChain(chain, ownerId);
			ObjectUtils.printObject(chain);
		} catch (ApplicationException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Ignore
	@Test
	public void testReplyChain() {
		ChainMessage chainMessage = null;
		String content = "hello!";
		int type = 0;
		long ownerId = 5;
		long chainId = 8;
		try {
			Map<String, Object> result = ServiceUtils.chainService.replyChain(ownerId, chainId, content, type);
			ObjectUtils.printObject(result);
		} catch (ApplicationException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Ignore
	@Test
	public void testThrowChain() {
		long accountId = 5;
		long chainId = 8;
		try {
			System.out.println(ServiceUtils.chainService.throwChain(chainId, accountId));
		} catch (ApplicationException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Ignore
	@Test
	public void testDeleteChain() {
		long accountId = 5;
		long chainId = 8;
		try {
			ServiceUtils.chainService.deleteChain(chainId, accountId);
		} catch (ApplicationException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Ignore
	@Test
	public void testObtainChains() {
		long accountId = 5;
		int limit = 2;
		Long start = Long.MIN_VALUE;
		Long end = Long.MAX_VALUE;
		boolean forward = true;
		try {
			Map<String, Object> result = ServiceUtils.chainService.obtainChains(accountId, start, end, limit, forward);
			ObjectUtils.printObject(result);
		} catch (ApplicationException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Ignore
	@Test
	public void testObtainChainMessages() {
		long accountId = 5;
		long chainId = 8;
		Timestamp last = Timestamp.valueOf("2013-05-31 22:36:15");
		int limit = 2;
		try {
			Map<String, Object> result = ServiceUtils.chainService.obtainChainMessages(accountId, chainId, last, limit);
			ObjectUtils.printObject(result);
		} catch (ApplicationException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	//@Ignore
	@Test
	public void testPickupChain() {
		long accountId = 2;
		try {
			List<Chain> chains = ServiceUtils.chainService.pickupChain(accountId, 2);
			ObjectUtils.printObject(chains);
			if(chains.size() > 0){
				ObjectUtils.printObject(chains.get(0).getChainMessages());
			}
		} catch (ApplicationException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Ignore
	@Test
	public void testViewedChainMessage() {
		long accountId = 4;
		long chainId = 3;
		Timestamp last = Timestamp.valueOf("2013-05-31 22:36:15");
		try {
			ServiceUtils.chainService.viewedChainMessages(accountId, chainId, last);
		} catch (ApplicationException e) {
			e.printStackTrace();
			fail();
		}
	}

}
