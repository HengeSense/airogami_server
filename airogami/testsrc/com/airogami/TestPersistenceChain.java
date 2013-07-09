package com.airogami;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.airogami.application.ServiceUtils;
import com.airogami.application.exception.ApplicationException;
import com.airogami.persitence.daos.DaoUtils;
import com.airogami.persitence.entities.Account;
import com.airogami.persitence.entities.Category;
import com.airogami.persitence.entities.ChainMessage;
import com.airogami.persitence.entities.Chain;

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
			long chainId = 8;
			Chain chain = ServiceUtils.chainService.matchChain(chainId);
			ObjectUtils.printObject(chain);
		} catch (ApplicationException e) {
			e.printStackTrace();
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
			chainMessage = ServiceUtils.chainService.replyChain(ownerId, chainId, content, type);
			ObjectUtils.printObject(chainMessage);
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
	public void testObtainChain() {
		long accountId = 5;
		String start = "2013-05-31 22:36:15";
		int limit = 2;
		boolean forward = true;
		try {
			Map<String, Object> result = ServiceUtils.chainService.obtainChains(accountId, start, limit, forward);
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
		try {
			List<ChainMessage> chains = ServiceUtils.chainService.obtainChainMessages(accountId, chainId);
			ObjectUtils.printObject(chains);
		} catch (ApplicationException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	
	@Test
	public void testViewedChainMessage() {
		long accountId = 4;
		long chainId = 3;
		String last = "2013-06-05 11:44:07";
		try {
			ServiceUtils.chainService.viewedChainMessage(accountId, chainId, last);
		} catch (ApplicationException e) {
			e.printStackTrace();
			fail();
		}
	}

}
