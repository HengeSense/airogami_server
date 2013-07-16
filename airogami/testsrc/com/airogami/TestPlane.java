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
import com.airogami.persitence.entities.Message;
import com.airogami.persitence.entities.Plane;
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
		long ownerId = 2L;
		Message message = new Message();
		message.setContent("hello!");
        message.setType((short) 0);
		plane.getMessages().add(message);

		try {
			plane = ManagerUtils.planeManager.sendPlane(plane, ownerId);
			ObjectUtils.printObject(plane);
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
		long ownerId = 3;
		long planeId = 3;
		try {
			message = ManagerUtils.planeManager.replyPlane(planeId, ownerId, message);
			ObjectUtils.printObject(message);
		} catch (AirogamiException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	@Ignore
	@Test
	public void testThrowPlane() {
		long accountId = 3;
		long planeId = 3;
		try {
			ManagerUtils.planeManager.throwPlane(planeId, accountId);
		} catch (AirogamiException e) {
			e.printStackTrace();
			fail();
		}
	}
	@Ignore
	@Test
	public void testLikePlane() {
		long accountId = 2;
		long planeId = 5;
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
	
	@Ignore
	@Test
	public void testDeletePlane() {
		long accountId = 3;
		long planeId = 3;
		boolean byOwner = false;
		try {
			ManagerUtils.planeManager.deletePlane(planeId, accountId, byOwner);
		} catch (AirogamiException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	@Ignore
	@Test
	public void testObtainPlanes() {
		long accountId = 4;
		String start = "2013-07-15 11:33:59.0";
		String end = "2013-07-15 11:34:06.0";
		int startIdx = 0;
		int limit = 50;
		boolean forward = true;
		try {
			Map<String, Object> result = ManagerUtils.planeManager.obtainPlanes(accountId, startIdx, start, end, limit, forward);
			//ObjectUtils.printObject(result);
			List<Plane> planes = (List<Plane>) result.get("planes");
			Iterator<Plane> iter = planes.iterator();
			while(iter.hasNext()){
				Plane plane = iter.next();
				System.out.print(plane.getPlaneId());
				System.out.println(": " + plane.getUpdatedTime());
			}
		} catch (AirogamiException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testReceivePlanes() {
		long accountId = 4;
		String start = "2013-06-15 11:33:59.0";
		String end = "2013-08-15 11:34:06.0";
		int startIdx = 0;
		int limit = 50;
		boolean forward = false;
		try {
			Map<String, Object> result = ManagerUtils.planeManager.receivePlanes(accountId, startIdx, start, end, limit, forward);
			//ObjectUtils.printObject(result);
			List<Plane> planes = (List<Plane>) result.get("planes");
			Iterator<Plane> iter = planes.iterator();
			while(iter.hasNext()){
				Plane plane = iter.next();
				System.out.print(plane.getPlaneId());
				System.out.println(": " + plane.getUpdatedTime());
			}
		} catch (AirogamiException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	@Ignore
	@Test
	public void testPickupPlane() {
		long accountId = 3;
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
		long accountId = 4;
		String start = "2013-07-15 11:33:04.0";
		int limit = 2; 
		long planeId = 14;
		try {
			Map<String, Object> result = ManagerUtils.planeManager.obtainMessages(accountId, planeId, 0, start, limit);
			ObjectUtils.printObject(result);
		} catch (AirogamiException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Ignore
	@Test
	public void testViewedMessage() {
		long accountId = 1;
		long planeId = 4;
		String last = "2013-07-15 11:33:07";
		boolean byOwner = true, succeed;
		try {
			succeed = ManagerUtils.planeManager.viewedMessages(accountId, planeId, last, byOwner);
		    System.out.println(succeed);
		} catch (AirogamiException e) {
			e.printStackTrace();
			fail();
		}
	}

}
