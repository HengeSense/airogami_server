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
import com.airogami.persistence.classes.NeoPlane;
import com.airogami.persistence.entities.Category;
import com.airogami.persistence.entities.Message;
import com.airogami.persistence.entities.Plane;
import com.airogami.presentation.logic.ManagerUtils;

public class TestPlane {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Ignore
	@Test
	public void testSendPlane() {
		Plane plane = new Plane();
		//plane.setCity("chengdu");
		//plane.setProvince("sichuan");
		//plane.setCountry("China");
		//plane.setAltitude(0.0);
		//plane.setLongitude(0.0);
		plane.setCategory(new Category());
		plane.getCategory().setCategoryId((short)1);
		int ownerId = 20;
		Message message = new Message();
		message.setContent("hello!");
        message.setType((short) 0);
		plane.getMessages().add(message);

		try {
			Map<String, Object> result = ManagerUtils.planeManager.sendPlane(plane, ownerId);
			ObjectUtils.printObject(result);
		} catch (AirogamiException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	@Ignore
	@Test
	public void testReplyPlane() {
		Message message = new Message();
		message.setContent("hello!");
		message.setType((short) 0);
		int ownerId = 2;
		long planeId = 3;
		boolean byOwner = false;
		try {
			Map<String, Object> result = ManagerUtils.planeManager.replyPlane(planeId, ownerId, byOwner, message);
			ObjectUtils.printObject(result);
		} catch (AirogamiException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	@Ignore
	@Test
	public void testThrowPlane() {
		int accountId = 5;
		long planeId = 2;
		try {
			Map<String, Object> result = ManagerUtils.planeManager.throwPlane(planeId, accountId);
			ObjectUtils.printObject(result);
		} catch (AirogamiException e) {
			e.printStackTrace();
			fail();
		}
	}
	@Ignore
	@Test
	public void testLikePlane() {
		int accountId = 3;
		long planeId = 3;
		boolean byOwner = true;
		try {
			Map<String, Object> result = ManagerUtils.planeManager.likePlane(planeId, accountId, byOwner);
			//ObjectUtils.printObject(message);
			ObjectUtils.printObject(result);
		} catch (AirogamiException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	//@Ignore
	@Test
	public void testClearPlane() {
		int accountId = 2;
		long planeId = 2;
		try {
			Map<String, Object> result = ManagerUtils.planeManager.clearPlane(planeId, accountId);
		    System.out.println(result);
		} catch (AirogamiException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Ignore
	@Test
	public void testDeletePlane() {
		int accountId = 3;
		long planeId = 3;
		boolean byOwner = true;
		try {
			Map<String, Object> result = ManagerUtils.planeManager.deletePlane(planeId, accountId, byOwner);
			ObjectUtils.printObject(result);
		} catch (AirogamiException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	@Ignore
	@Test
	public void testObtainPlanes() {
		int accountId = 4;
		int limit = 50;
		boolean forward = false;
		Long start = Long.MIN_VALUE;
		Long end = Long.MAX_VALUE;
		try {
			Map<String, Object> result = ManagerUtils.planeManager.obtainPlanes(accountId, start, end, limit, forward);
			//ObjectUtils.printObject(result);
			List<Plane> planes = (List<Plane>) result.get("planes");
			Iterator<Plane> iter = planes.iterator();
			while(iter.hasNext()){
				Plane plane = iter.next();
				System.out.print(plane.getPlaneId());
			}
		} catch (AirogamiException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	@Ignore
	@Test
	public void testGetNewPlanes() {
		int accountId = 2;
		int limit = 50;
		boolean forward = false;
		Long start = Long.MIN_VALUE;
		Long end = Long.MAX_VALUE;
		try {
			Map<String, Object> result = ManagerUtils.planeManager.getNeoPlanes(accountId, start, end, limit, forward);
			//ObjectUtils.printObject(result);
			List<NeoPlane> neoPlanes = (List<NeoPlane>) result.get("newPlanes");
			Iterator<NeoPlane> iter = neoPlanes.iterator();
			while(iter.hasNext()){
				NeoPlane neoPlane = iter.next();
				System.out.print(neoPlane.getPlaneId());
				System.out.print(": " + neoPlane.getUpdateInc());
				System.out.println(": " + neoPlane.getUpdateCount());
			}
		} catch (AirogamiException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	@Ignore
	@Test
	public void testGetPlanes() {
		int accountId = 2;
		List<Long> planeIds = Arrays.asList(6L, 7L);
		boolean updated = false;
		try {
			Object planes = ManagerUtils.planeManager.getPlanes(accountId, planeIds, updated);
			ObjectUtils.printObject(planes);
		} catch (AirogamiException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	@Ignore
	@Test
	public void testGetPlaneIds() {
		int accountId = 1;
		boolean forward = false;
		Long start = 1L;
		Long end = 2L;
		int limit = -1;
		try {
			Map<String, Object> result = ManagerUtils.planeManager.getOldPlanes(accountId, start, end, limit, forward);
			//ObjectUtils.printObject(result);
			List<Long> planes = (List<Long>) result.get("oldPlanes");
			Iterator<Long> iter = planes.iterator();
			while(iter.hasNext()){
				Long planeId = iter.next();
				System.out.println(planeId);
			}
		} catch (AirogamiException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		
		try{
			Thread.sleep(1000 * 3600);
		}
		catch(Throwable t){}
	}
	
	@Ignore
	@Test
	public void testObtainPlaneIds() {
		int accountId = 4;
		int startId = 2;
		int limit = -1;
		try {
			Map<String, Object> result = ManagerUtils.planeManager.obtainPlaneIds(accountId, startId, limit);
			//ObjectUtils.printObject(result);
			List<Long> planes = (List<Long>) result.get("planeIds");
			Iterator<Long> iter = planes.iterator();
			while(iter.hasNext()){
				Long planeId = iter.next();
				System.out.println(planeId);
			}
		} catch (AirogamiException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}	
	
	@Ignore
	@Test
	public void testReceivePlanes() {
		int accountId = 1;
		Long start = Long.MIN_VALUE;
		Long end = Long.MAX_VALUE;
		int limit = 1;
		boolean forward = true;
		try {
			Map<String, Object> result = ManagerUtils.planeManager.receivePlanes(
					accountId, start, end, limit, forward);
			//ObjectUtils.printObject(result);
			List<Plane> planes = (List<Plane>) result.get("planes");
			Iterator<Plane> iter = planes.iterator();
			while(iter.hasNext()){
				Plane plane = iter.next();
				System.out.print(plane.getPlaneId());
			}
		} catch (AirogamiException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Ignore
	@Test
	public void testReceivePlaneIds() {
		int accountId = 4;
		int startId = 9;
		int limit = 50;
		try {
			Map<String, Object> result = ManagerUtils.planeManager.receivePlaneIds(accountId, startId, limit);
			//ObjectUtils.printObject(result);
			List<Long> planes = (List<Long>) result.get("planeIds");
			Iterator<Long> iter = planes.iterator();
			while(iter.hasNext()){
				Long planeId = iter.next();
				System.out.println(planeId);
			}
			System.out.println(result.get("more"));
		} catch (AirogamiException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	@Ignore
	@Test
	public void testPickupPlane() {
		int accountId = 3;
		try {
			Map<String, Object> result = null;
			result = ManagerUtils.planeManager.pickup(accountId);
			ObjectUtils.printObject(result);
		} catch (AirogamiException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	@Ignore
	@Test
	public void testObtainMessages() {
		int accountId = 4;
		Long lastMsgId = null;
		int limit = 2; 
		long planeId = 14;
		try {
			Map<String, Object> result = ManagerUtils.planeManager.obtainMessages(accountId, planeId, lastMsgId, limit);
			ObjectUtils.printObject(result);
		} catch (AirogamiException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Ignore
	@Test
	public void testViewedMessage() {
		int accountId = 1;
		long planeId = 4;
		long lastMsgId = 0;
		boolean byOwner = true;
		try {
			Map<String, Object> result = ManagerUtils.planeManager.viewedMessages(accountId, planeId, lastMsgId, byOwner);
		    System.out.println(result);
		} catch (AirogamiException e) {
			e.printStackTrace();
			fail();
		}
	}

}
