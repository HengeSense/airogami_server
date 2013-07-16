package com.airogami;

import static org.junit.Assert.*;

import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.airogami.application.ServiceUtils;
import com.airogami.application.exception.ApplicationException;
import com.airogami.exception.AirogamiException;
import com.airogami.persitence.entities.Category;
import com.airogami.persitence.entities.Chain;
import com.airogami.persitence.entities.ChainMessage;
import com.airogami.persitence.entities.Message;
import com.airogami.persitence.entities.Plane;
import com.airogami.presentation.logic.ManagerUtils;

public class TestChain {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Ignore
	@Test
	public void testSendChain() {
		Chain chain = new Chain();
		//chain.setCity("shanghai");
		//chain.setProvince("shanghai");
		//chain.setCountry("China");
		//chain.setMessageCount((short) 0);
		long ownerId = 4L;
		ChainMessage chainMessage = new ChainMessage();
		chainMessage.setContent("hello!");
		chainMessage.setType((short) 0);
		chain.getChainMessages().add(chainMessage);

		try {
			chain = ManagerUtils.chainManager.sendChain(chain, ownerId);
			ObjectUtils.printObject(chain);
		} catch (AirogamiException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	@Ignore
	@Test
	public void testReplyChain() {
		String content = "hello!";
		int type = 0;
		long ownerId = 4;
		long chainId = 3;
		try {
			ChainMessage chainMessage = ManagerUtils.chainManager.replyChain(ownerId, chainId, content, type);
			ObjectUtils.printObject(chainMessage);
		} catch (AirogamiException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	@Ignore
	@Test
	public void testThrowChain() {
		long accountId = 2;
		long chainId = 3;
		try {
			System.out.println(ManagerUtils.chainManager.throwChain(chainId, accountId));
		} catch (AirogamiException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	@Ignore
	@Test
	public void testDeleteChain() {
		long accountId = 2;
		long chainId = 3;
		try {
			ManagerUtils.chainManager.deleteChain(chainId, accountId);
		} catch (AirogamiException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	
	@Test
	public void testObtainChains() {
		long accountId = 5;
		String start = "2013-07-15 18:50:39.0";
		String end = "2013-07-15 18:50:39.0";
		int limit = 4;
		int startIdx = -1;
		boolean forward = false;
		try {
			Map<String, Object> result = ManagerUtils.chainManager.obtainChains(accountId, startIdx, start, end, limit, forward);
			List<Chain> planes = (List<Chain>) result.get("chains");
			Iterator<Chain> iter = planes.iterator();
			while(iter.hasNext()){
				Chain chain = iter.next();
				System.out.print(chain.getChainId());
				System.out.println(": " + chain.getUpdatedTime());
			}
			//ObjectUtils.printObject(result);
		} catch (AirogamiException e) {
			e.printStackTrace();
			fail();
		}
	}
	@Ignore
	@Test
	public void testReceiveChains() {
		long accountId = 5;
		String start = "2013-06-15 18:50:39.0";
		String end = "2013-08-15 18:50:39.0";
		int limit = 4;
		int startIdx = -1;
		boolean forward = true;
		try {
			Map<String, Object> result = ManagerUtils.chainManager.receiveChains(accountId, startIdx, start, end, limit, forward);
			List<Chain> planes = (List<Chain>) result.get("chains");
			Iterator<Chain> iter = planes.iterator();
			while(iter.hasNext()){
				Chain chain = iter.next();
				System.out.print(chain.getChainId());
				System.out.println(": " + chain.getUpdatedTime());
			}
			//ObjectUtils.printObject(result);
		} catch (AirogamiException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Ignore
	@Test
	public void testObtainChainMessages() {
		long accountId = 2;
		long chainId = 8;
		try {
			List<ChainMessage> chains = ManagerUtils.chainManager.obtainChainMessages(accountId, chainId);
			ObjectUtils.printObject(chains);
		} catch (AirogamiException e) {
			e.printStackTrace();
			fail();
		}
	}
	@Ignore
	@Test
	public void testViewedChainMessage() {
		long accountId = 5;
		long chainId = 3;
		String last = "2013-08-15 18:48:44";
		boolean result;
		try {
			result = ManagerUtils.chainManager.viewedChainMessages(accountId, chainId, last);
			System.out.println(result);
		} catch (AirogamiException e) {
			e.printStackTrace();
			fail();
		}
	}

}
