package com.airogami;

import static org.junit.Assert.*;

import java.sql.Timestamp;
import java.util.Arrays;
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
import com.airogami.persistence.classes.NeoChain;
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

		int ownerId = 4;
		ChainMessage chainMessage = new ChainMessage();
		chainMessage.setContent("hello!");
		chainMessage.setType((short) 0);
		chain.getChainMessages().add(chainMessage);

		try {
			Map<String, Object> result = ManagerUtils.chainManager.sendChain(chain, ownerId);
			ObjectUtils.printObject(result);
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
		int ownerId = 1;
		long chainId = 1;
		try {
			Map<String, Object> result = ManagerUtils.chainManager.replyChain(ownerId, chainId, content, type);
			ObjectUtils.printObject(result);
		} catch (AirogamiException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	@Ignore
	@Test
	public void testThrowChain() {
		int accountId = 2;
		long chainId = 1;
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
		int accountId = 2;
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
		int accountId = 5;
		int limit = 4;
		Long start = Long.MIN_VALUE;
		Long end = Long.MAX_VALUE;
		boolean forward = false;
		try {
			Map<String, Object> result = ManagerUtils.chainManager.obtainChains(
					accountId, start, end, limit, forward);
			List<Chain> planes = (List<Chain>) result.get("chains");
			Iterator<Chain> iter = planes.iterator();
			while(iter.hasNext()){
				Chain chain = iter.next();
				System.out.print(chain.getChainId());
			}
			//ObjectUtils.printObject(result);
		} catch (AirogamiException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Ignore
	@Test
	public void testGetNewChains() {
		int accountId = 1;
		int limit = 4;
		Long start = Long.MIN_VALUE;
		Long end = Long.MAX_VALUE;
		boolean forward = false;
		try {
			Map<String, Object> result = ManagerUtils.chainManager.getNeoChains(
					accountId, start, end, limit, forward);
			List<NeoChain> neoChains = (List<NeoChain>) result.get("newChains");
			Iterator<NeoChain> iter = neoChains.iterator();
			while(iter.hasNext()){
				//ObjectUtils.printObject(iter.next());
				NeoChain neoChain = iter.next();
				System.out.print(neoChain.getChainId());
				System.out.print(": " + neoChain.getUpdateInc());
				System.out.println(": " + neoChain.getUpdateCount());
			}
			//ObjectUtils.printObject(result);
		} catch (AirogamiException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	//@Ignore
	@Test
	public void testGetChains() {
		int accountId = 1;
		List<Long> chainIds = Arrays.asList(1L, 2L);
		try {
			List<Chain> chains = ManagerUtils.chainManager.getChains(
					accountId, chainIds);
			ObjectUtils.printObject(chains);
		} catch (AirogamiException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Ignore
	@Test
	public void testGetChainIds() {
		int accountId = 2;
		int limit = 4;
		Long start = 1L;
		Long end = 2L;
		boolean forward = false;
		try {
			Map<String, Object> result = ManagerUtils.chainManager.getChainIds(
					accountId, start, end, limit, forward);
			List<Long> newChains = (List<Long>) result.get("chainIds");
			Iterator<Long> iter = newChains.iterator();
			while(iter.hasNext()){
				Long chainId = iter.next();
				System.out.println(chainId);
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
		int accountId = 2;
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
		int accountId = 5;
		Long start = Long.MIN_VALUE;
		Long end = Long.MAX_VALUE;
		int limit = 4;
		boolean forward = true;
		try {
			Map<String, Object> result = ManagerUtils.chainManager.receiveChains(
					accountId, start, end, limit, forward);
			List<Chain> planes = (List<Chain>) result.get("chains");
			Iterator<Chain> iter = planes.iterator();
			while(iter.hasNext()){
				Chain chain = iter.next();
				System.out.print(chain.getChainId());
			}
			//ObjectUtils.printObject(result);
		} catch (AirogamiException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Ignore
	@Test
	public void testReceiveChainIds() {
		int accountId = 2;
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
		int accountId = 2;
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
		int accountId = 5;
		long chainId = 3;
		String last = "2013-08-15 18:48:44";
		Map<String, Object> result;
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
