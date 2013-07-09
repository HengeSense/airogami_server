package com.airogami.application;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.airogami.application.exception.ApplicationException;
import com.airogami.persitence.daos.DaoUtils;
import com.airogami.persitence.entities.Account;
import com.airogami.persitence.entities.EntityManagerHelper;
import com.airogami.persitence.entities.Message;
import com.airogami.persitence.entities.Plane;

public class PlaneService implements IPlaneService {

	/*
	 * @see com.airogami.application.IPlaneService#sendPlane(Plane plane, long
	 * targetId, long ownerId)
	 */
	@Override
	public Plane sendPlane(Plane plane, long ownerId)
			throws ApplicationException {

		ApplicationException ae = null;
		try {
			Message message = plane.getMessages().get(0);
			EntityManagerHelper.beginTransaction();
			Account accountByOwnerId = DaoUtils.accountDao
					.getReference(ownerId);
			plane.setAccountByOwnerId(accountByOwnerId);
			message.setAccount(accountByOwnerId);
			DaoUtils.planeDao.save(plane);
			EntityManagerHelper.commit();
		} catch (Throwable t) {
			t.printStackTrace();
			if (t.getCause() == null) {
				ae = new ApplicationException();
			} else {
				ae = new ApplicationException(t.getCause().getMessage());
			}
		} finally {
			EntityManagerHelper.closeEntityManager();
		}
		if (ae != null) {
			throw ae;
		}
		return plane;
	}

	/*
	 * @see com.airogami.application.IPlaneService#replyPlane(long,
	 * com.airogami.persitence.entities.Message)
	 */
	@Override
	public Message replyPlane(long planeId, long ownerId, Message message)
			throws ApplicationException {
		ApplicationException ae = null;
		try {
			EntityManagerHelper.beginTransaction();
			if (DaoUtils.planeDao.verifyReply(planeId, ownerId)) {
				Plane plane = DaoUtils.planeDao.getReference(planeId);
				message.setPlane(plane);
				Account account = DaoUtils.accountDao.getReference(ownerId);
				message.setAccount(account);
				DaoUtils.messageDao.save(message);
			} else {
				ae = new ApplicationException("verifyReply failed");
			}
			EntityManagerHelper.commit();
		} catch (Throwable t) {
			
			if (t.getCause() == null) {
				ae = new ApplicationException();
			} else {
				ae = new ApplicationException(t.getCause().getMessage());
			}
		} finally {
			EntityManagerHelper.closeEntityManager();
		}
		if (ae != null) {
			throw ae;
		}
		return message;
	}

	@Override
	public Plane matchPlane(long planeId) throws ApplicationException {
		ApplicationException ae = null;
		Plane plane = null;
		try {
			EntityManagerHelper.beginTransaction();
			plane = DaoUtils.planeDao.findById(planeId);
			if(plane == null){
				ae = new ApplicationException("matchPlane failed");
			}
			else{
				long accountId;
				String country = plane.getCountry(), province = plane.getProvince(), city = plane
						.getCity();
				if (city != null && city.length() > 0) {
					accountId = DaoUtils.accountDao.randPlaneAccount(
							plane.getAccountByOwnerId(), country, province, city);
				} else if (province != null && province.length() > 0) {
					accountId = DaoUtils.accountDao.randPlaneAccount(
							plane.getAccountByOwnerId(), country, province);
				} else if (country != null && country.length() > 0) {
					accountId = DaoUtils.accountDao.randPlaneAccount(
							plane.getAccountByOwnerId(), country);
				} else {
					accountId = DaoUtils.accountDao.randPlaneAccount(plane
							.getAccountByOwnerId());
				}
				Account accountByTargetId = DaoUtils.accountDao
						.getReference(accountId);
				plane.setAccountByTargetId(accountByTargetId);
				DaoUtils.planeDao.flush();
				DaoUtils.planeDao.increaseMatchCount(planeId, 1);
			}			
			EntityManagerHelper.commit();
		} catch (Throwable t) {
			
			if (t.getCause() == null) {
				ae = new ApplicationException();
			} else {
				ae = new ApplicationException(t.getCause().getMessage());
			}
		} finally {
			EntityManagerHelper.closeEntityManager();
		}
		if (ae != null) {
			throw ae;
		}
		plane.setMatchCount(plane.getMatchCount() + 1);
		return plane;
	}

	@Override
	public int throwPlane(long planeId, long accountId)
			throws ApplicationException {
		ApplicationException ae = null;
		int count = 0;
		try {
			EntityManagerHelper.beginTransaction();
			if (DaoUtils.planeDao.throwPlane(planeId, accountId) == false) {
				ae = new ApplicationException("throwPlane failed");
			}
			else{
				count = DaoUtils.planeDao.getPlaneMatchCount(planeId);
			}
			EntityManagerHelper.commit();
		} catch (Throwable t) {
			
			if (t.getCause() == null) {
				ae = new ApplicationException();
			} else {
				ae = new ApplicationException(t.getCause().getMessage());
			}
		} finally {
			EntityManagerHelper.closeEntityManager();
		}
		if (ae != null) {
			throw ae;
		}
		return count;
		
	}

	@Override
	public void deletePlane(long planeId, long accountId, boolean byOwner)
			throws ApplicationException {
		ApplicationException ae = null;
		try {
			EntityManagerHelper.beginTransaction();
			if (DaoUtils.planeDao.deletePlane(planeId, accountId, byOwner) == false) {
				ae = new ApplicationException("deletePlane failed");
			}
			EntityManagerHelper.commit();
		} catch (Throwable t) {
			
			if (t.getCause() == null) {
				ae = new ApplicationException();
			} else {
				ae = new ApplicationException(t.getCause().getMessage());
			}
		} finally {
			EntityManagerHelper.closeEntityManager();
		}
		if (ae != null) {
			throw ae;
		}
		
	}

	@Override
	public Map<String, Object> obtainPlanes(long accountId, String start, int limit,
			boolean forward) throws ApplicationException {
		if(limit > IPlaneService.MaxPlaneLimit)
			limit = IPlaneService.MaxPlaneLimit;
		List<Plane> planes = null;
		ApplicationException ae = null;
		Map<String, Object> result = null;
		try {
			EntityManagerHelper.beginTransaction();
			planes = DaoUtils.planeDao.obtainPlanes(accountId, start, limit + 1, forward);
			EntityManagerHelper.commit();
		} catch (Throwable t) {
			
			if (t.getCause() == null) {
				ae = new ApplicationException();
			} else {
				ae = new ApplicationException(t.getCause().getMessage());
			}
		} finally {
			EntityManagerHelper.closeEntityManager();
		}
		if (ae != null) {
			throw ae;
		}
		boolean more = planes.size() > limit;
		result = new TreeMap<String, Object>();
		result.put("more", more);
		result.put("planes", planes);
		return result;
	}

	@Override
	public Map<String, Object> obtainMessages(long accountId, long planeId,
			String start, int limit, boolean forward)
			throws ApplicationException {
		if(limit > IPlaneService.MaxMessageLimit)
			limit = IPlaneService.MaxMessageLimit;
		List<Message> messages = null;
		ApplicationException ae = null;
		Map<String, Object> result = null;
		try {
			EntityManagerHelper.beginTransaction();
			messages = DaoUtils.messageDao.obtainMessages(accountId, planeId, start, limit + 1, forward);
			EntityManagerHelper.commit();
		} catch (Throwable t) {
			
			if (t.getCause() == null) {
				ae = new ApplicationException();
			} else {
				ae = new ApplicationException(t.getCause().getMessage());
			}
		} finally {
			EntityManagerHelper.closeEntityManager();
		}
		if (ae != null) {
			throw ae;
		}
		boolean more = messages.size() > limit;
		result = new TreeMap<String, Object>();
		result.put("more", more);
		result.put("messages", messages);
		return result;
	}

	@Override
	public void viewedMessage(long accountId, long planeId, String last, boolean byOwner)
			throws ApplicationException {
		Timestamp lastTimestamp = Timestamp.valueOf(last);
		long time = System.currentTimeMillis();
		if(lastTimestamp.getTime() > time){
			lastTimestamp.setTime(time);
		}
		ApplicationException ae = null;
		try {
			EntityManagerHelper.beginTransaction();
			if (DaoUtils.messageDao.viewedMessage(accountId, planeId, lastTimestamp, byOwner) == false) {
				ae = new ApplicationException("viewedMessage failed");
			}
			EntityManagerHelper.commit();
		} catch (Throwable t) {
			
			if (t.getCause() == null) {
				ae = new ApplicationException();
			} else {
				ae = new ApplicationException(t.getCause().getMessage());
			}
		} finally {
			EntityManagerHelper.closeEntityManager();
		}
		if (ae != null) {
			throw ae;
		}
		
	}

}
