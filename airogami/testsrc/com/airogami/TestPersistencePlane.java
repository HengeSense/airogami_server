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
import com.airogami.persitence.entities.Message;
import com.airogami.persitence.entities.Plane;

public class TestPersistencePlane {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Ignore
	@Test
	public void testRandAccount() {
		try {

//		    System.out.println(DaoUtils.accountDao.randAccount(1, "China"));
//			System.out.println(DaoUtils.accountDao.randAccount(1, "China", "shanghai"));
//			System.out.println(DaoUtils.accountDao.randAccount(1, "China", "shanghai", "shanghai"));
//			System.out.println(DaoUtils.accountDao.randAccount(1, "China", "shanghai", "shanghai"));
//			System.out.println(DaoUtils.accountDao.randAccount(1, "China", "shanghai", "shanghai"));
//			System.out.println(DaoUtils.accountDao.randAccount(1, "China", "shanghai", "shanghai"));
//			System.out.println(DaoUtils.accountDao.randAccount(1));
		} catch (RuntimeException re) {
			re.printStackTrace();
			fail();
		}
	}
	
	@Ignore
	@Test
	public void testMatchPlane() {
		try {
			long planeId = 2;
			Plane plane = ServiceUtils.planeService.matchPlane(planeId);
			ObjectUtils.printObject(plane);
		} catch (ApplicationException e) {
			e.printStackTrace();
			fail();
		}
	}

	@Ignore
	@Test
	public void testSendPlane() {
		Plane plane = new Plane();
		plane.setCity("shanghai");
		plane.setProvince("shanghai");
		plane.setCountry("China");
		plane.setAltitude(0.0);
		plane.setLongitude(0.0);
		plane.setCategory(new Category());
		plane.getCategory().setCategoryId((short)1);
		long ownerId = 4L;
		Message message = new Message();
		message.setContent("hello!");
		message.setType((short) 0);
		plane.getMessages().add(message);
		message.setPlane(plane);
		try {
			plane = ServiceUtils.planeService.sendPlane(plane, ownerId);
			ObjectUtils.printObject(plane);
		} catch (ApplicationException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Ignore
	@Test
	public void testReplyPlane() {
		Message message = new Message();
		message.setContent("hello!");
		message.setType((short) 0);
		long ownerId = 4;
		long planeId = 2;
		try {
			message = ServiceUtils.planeService.replyPlane(planeId, ownerId, message);
			ObjectUtils.printObject(message);
		} catch (ApplicationException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Ignore
	@Test
	public void testThrowPlane() {
		long accountId = 4;
		long planeId = 2;
		try {
			ServiceUtils.planeService.throwPlane(planeId, accountId);
		} catch (ApplicationException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Ignore
	@Test
	public void testDeletePlane() {
		long accountId = 3;
		long planeId = 2;
		boolean byOwner = false;
		try {
			ServiceUtils.planeService.deletePlane(planeId, accountId, byOwner);
		} catch (ApplicationException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Ignore
	@Test
	public void testObtainPlane() {
		long accountId = 4;
		String start = "2013-05-31 22:36:15";
		int limit = 2;
		boolean forward = true;
		try {
			Map<String, Object> result = ServiceUtils.planeService.obtainPlanes(accountId, start, limit, forward);
			ObjectUtils.printObject(result);
		} catch (ApplicationException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Ignore
	@Test
	public void testViewedMessage() {
		long accountId = 4;
		long planeId = 4;
		String last = "2013-06-05 11:30:33";
		boolean byOwner = false;
		try {
			ServiceUtils.planeService.viewedMessage(accountId, planeId, last, byOwner);
		} catch (ApplicationException e) {
			e.printStackTrace();
			fail();
		}
	}

}