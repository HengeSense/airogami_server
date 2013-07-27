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
import com.airogami.persistence.entities.Category;
import com.airogami.persistence.entities.Chain;
import com.airogami.persistence.entities.ChainMessage;
import com.airogami.persistence.entities.Message;
import com.airogami.persistence.entities.Plane;
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
	
	@Ignore
	@Test
	public void testObtainChains() {
		long accountId = 5;
		String start = "2013-07-15 18:50:39.0";
		String end = "2013-07-15 18:50:39.0";
		int limit = 4;
		int startIdx = -1;
		boolean forward = false;
		try {
			Map<String, Object> result = ManagerUtils.chainManager.obtainChains(
					accountId, startIdx, Timestamp.valueOf(start), Timestamp.valueOf(end), limit, forward);
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
	public void testObtainChainIds() {
		long accountId = 2;
		int limit = 1;
		int startId = 5;
		try {
			Map<String, Object> result = ManagerUtils.chainManager.obtainChainIds(accountId, startId, limit);
			List<Long> planes = (List<Long>) result.get("chainIds");
			Iterator<Long> iter = planes.iterator();
			while(iter.hasNext()){
				Long chainId = iter.next();
				System.out.println(chainId);
			}
			System.out.println(result.get("more"));
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
			Map<String, Object> result = ManagerUtils.chainManager.receiveChains(
					accountId, startIdx, Timestamp.valueOf(start), Timestamp.valueOf(end), limit, forward);
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
	
	
	@Test
	public void testReceiveChainIds() {
		long accountId = 2;
		int limit = 4;
		int startId = 3;
		try {
			Map<String, Object> result = ManagerUtils.chainManager.receiveChainIds(accountId, startId, limit);
			List<Long> planes = (List<Long>) result.get("chainIds");
			Iterator<Long> iter = planes.iterator();
			while(iter.hasNext()){
				Long chainId = iter.next();
				System.out.println(chainId);
			}
			System.out.println(result.get("more"));
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
		String last = "2013-08-15 18:48:44";
		int limit = 2;
		try {
			Map<String, Object> result = ManagerUtils.chainManager.obtainChainMessages(
					accountId, chainId, Timestamp.valueOf(last), limit);
			ObjectUtils.printObject(result);
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
			result = ManagerUtils.chainManager.viewedChainMessages(
					accountId, chainId, Timestamp.valueOf(last));
			System.out.println(result);
		} catch (AirogamiException e) {
			e.printStackTrace();
			fail();
		}
	}

}
