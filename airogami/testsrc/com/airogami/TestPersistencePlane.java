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
import com.airogami.common.constants.AccountConstants;
import com.airogami.persistence.daos.DaoUtils;
import com.airogami.persistence.entities.Account;
import com.airogami.persistence.entities.Category;
import com.airogami.persistence.entities.Message;
import com.airogami.persistence.entities.Plane;

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
			
			for(int i = 0; i < 10; ++i){
				System.out.println(DaoUtils.accountDao.randPlaneAccount(DaoUtils.accountDao.getReference(1L), (short)AccountConstants.SexType_Unknown));
		        //System.out.println(DaoUtils.accountDao.randPlaneAccount(DaoUtils.accountDao.getReference(1L),(short)AccountConstants.SexType_Female, "China"));
				//System.out.println(DaoUtils.accountDao.randPlaneAccount(DaoUtils.accountDao.getReference(2L), (short)AccountConstants.SexType_Female, "China", "Sichuan"));
				//System.out.println(DaoUtils.accountDao.randPlaneAccount(DaoUtils.accountDao.getReference(2L), (short)AccountConstants.SexType_Female, "China", "Sichuan", "Chengdu"));

			}

		    
		} catch (RuntimeException re) {
			re.printStackTrace();
			fail();
		}
	}
	
	
	@Test
	public void testMatchPlane() {
		try {
			long planeId = 2;
			Map<String, Object> result = ServiceUtils.planeService.matchPlane(planeId);
			ObjectUtils.printObject(result);
		} catch (ApplicationException e) {
			e.printStackTrace();
			fail();
		}
	}

	@Ignore
	@Test
	public void testSendPlane() {
		Plane plane = new Plane();
		plane.setCity("chengdu");
		plane.setProvince("sichuan");
		plane.setCountry("China");
		plane.setLatitude(0.0);
		plane.setLongitude(0.0);
		plane.setCategory(new Category());
		plane.getCategory().setCategoryId((short)1);
		long ownerId = 1L;
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
	public void testObtainPlanes() {
		long accountId = 4;
		String start = "2013-05-31 22:36:15";
		Timestamp timestamp = Timestamp.valueOf(start);
		int limit = 2;
		boolean forward = true;
		try {
			Map<String, Object> result = ServiceUtils.planeService.obtainPlanes(accountId, -1, null, null, limit, forward);
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
			ServiceUtils.planeService.viewedMessages(accountId, planeId, Timestamp.valueOf(last), byOwner);
		} catch (ApplicationException e) {
			e.printStackTrace();
			fail();
		}
	}

}
