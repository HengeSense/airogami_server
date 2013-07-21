package com.airogami.application;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.airogami.application.exception.ApplicationException;
import com.airogami.common.constants.MessageConstants;
import com.airogami.common.constants.PlaneConstants;
import com.airogami.persistence.daos.DaoUtils;
import com.airogami.persistence.entities.Account;
import com.airogami.persistence.entities.Category;
import com.airogami.persistence.entities.EntityManagerHelper;
import com.airogami.persistence.entities.Message;
import com.airogami.persistence.entities.Plane;

public class PlaneService implements IPlaneService {
	
	public Category createCatrgory(Category category) throws ApplicationException{
		ApplicationException ae = null;
		try {
			EntityManagerHelper.beginTransaction();
			DaoUtils.categoryDao.save(category);
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
		return category;
	}

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
	public List<Plane> pickupPlane(long accountId, int count) throws ApplicationException	{
		ApplicationException ae = null;
		List<Plane> planes = null;
		try {
			EntityManagerHelper.beginTransaction();
			planes = DaoUtils.planeDao.pickup(accountId, count);
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
		return planes;
	}

	@Override
	public Map<String, Object> matchPlane(long planeId) throws ApplicationException {
		ApplicationException ae = null;
		Plane plane = null;
		Long accountId = null;
		try {
			EntityManagerHelper.beginTransaction();
			plane = DaoUtils.planeDao.findById(planeId);
			// may pickuped before this match
			if(plane != null){
				if(plane.getAccountByTargetId() == null && plane.getStatus() == PlaneConstants.StatusNew 
					&& plane.getMatchCount() < plane.getMaxMatchCount()){
					
					String country = plane.getCountry(), province = plane.getProvince(), city = plane
							.getCity();
					Short sex = plane.getSex();
					if (city != null && city.length() > 0) {
						accountId = DaoUtils.accountDao.randPlaneAccount(
								plane.getAccountByOwnerId(), sex, country, province, city);
					} else if (province != null && province.length() > 0) {
						accountId = DaoUtils.accountDao.randPlaneAccount(
								plane.getAccountByOwnerId(), sex, country, province);
					} else if (country != null && country.length() > 0) {
						accountId = DaoUtils.accountDao.randPlaneAccount(
								plane.getAccountByOwnerId(), sex, country);
					} else {
						accountId = DaoUtils.accountDao.randPlaneAccount(plane
								.getAccountByOwnerId(), sex);
					}

					if(accountId == null || DaoUtils.planeDao.match(planeId, accountId)){						
						DaoUtils.planeDao.increaseMatchCount(planeId, 1);
						if(accountId != null){//match succeed
							plane = null;
						}
					}
					else{
						//already matched
						plane = null;
					}
				}
				else{
					//already matched
					plane = null;
				}		
			}
			
			EntityManagerHelper.commit();
		} catch (Throwable t) {
			//t.printStackTrace();
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
		Map<String, Object> result = new TreeMap<String,Object>();
		if(plane != null){
		    plane.setMatchCount(plane.getMatchCount() + 1);
		    result.put("plane", plane);
		}
		if(accountId != null){
			result.put("accountId", accountId);
		}		
		
		return result;
	}

	@Override
	public boolean throwPlane(long planeId, long accountId)
			throws ApplicationException {
		ApplicationException ae = null;
		boolean canMatchAgain = false;
		try {
			EntityManagerHelper.beginTransaction();
			if (DaoUtils.planeDao.throwPlane(planeId, accountId)) {
				canMatchAgain = DaoUtils.planeDao.canPlaneMatch(planeId);
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
		return canMatchAgain;
		
	}
	
	@Override
	public Map<String, Object> likePlane(long planeId,long accountId, boolean byOwner) throws ApplicationException{
		ApplicationException ae = null;
		Message message = new Message();
		message.setContent(MessageConstants.LikeContent);
		message.setType(MessageConstants.MessageTypeLike);
		Long oppositeAccountId = null;
		try {			
			EntityManagerHelper.beginTransaction();
			if ((oppositeAccountId = DaoUtils.planeDao.likePlane(planeId, accountId, byOwner)) == null) {
				ae = new ApplicationException("likePlane failed");
			}
			else{
				Plane plane = DaoUtils.planeDao.findById(planeId);
				message.setPlane(plane);
				Account account = DaoUtils.accountDao.getReference(accountId);
				message.setAccount(account);
				DaoUtils.messageDao.save(message);
				DaoUtils.messageDao.flush();
				DaoUtils.accountStatDao.increaseLikesCount(oppositeAccountId, 1);		
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
		Map<String, Object> result = new TreeMap<String, Object>();
		result.put("message", message);
		result.put("oppositeAccountId", oppositeAccountId);
		return result;	
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
	public Map<String, Object> obtainPlanes(long accountId, int startIdx, Timestamp start,Timestamp end, int limit,
			boolean forward) throws ApplicationException {
		if(limit > IPlaneService.MaxPlaneLimit || limit < 1)
			limit = IPlaneService.MaxPlaneLimit;
		List<Plane> planes = null;
		ApplicationException ae = null;
		Map<String, Object> result = null;
		try {
			EntityManagerHelper.beginTransaction();
			planes = DaoUtils.planeDao.obtainPlanes(accountId, startIdx, start, end, limit + 1, forward);
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
	public Map<String, Object> receivePlanes(long accountId, int startIdx, Timestamp start,Timestamp end, int limit,
			boolean forward) throws ApplicationException {
		if(limit > IPlaneService.MaxPlaneLimit || limit < 1)
			limit = IPlaneService.MaxPlaneLimit;
		List<Plane> planes = null;
		ApplicationException ae = null;
		Map<String, Object> result = null;
		try {
			EntityManagerHelper.beginTransaction();
			planes = DaoUtils.planeDao.receivePlanes(accountId, startIdx, start, end, limit + 1, forward);
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
	public Map<String, Object> obtainMessages(long accountId, long planeId, int startIdx,
			Timestamp start, int limit, boolean forward)
			throws ApplicationException {
		if(limit > IPlaneService.MaxMessageLimit || limit < 1)
			limit = IPlaneService.MaxMessageLimit;
		List<Message> messages = null;
		ApplicationException ae = null;
		Map<String, Object> result = null;
		try {
			EntityManagerHelper.beginTransaction();
			messages = DaoUtils.messageDao.obtainMessages(accountId, planeId, startIdx, start, limit + 1, forward);
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
	public boolean viewedMessages(long accountId, long planeId, Timestamp last, boolean byOwner)
			throws ApplicationException {
		ApplicationException ae = null;
		boolean succeed = false;
		try {
			EntityManagerHelper.beginTransaction();
			succeed = DaoUtils.messageDao.viewedMessage(accountId, planeId, last, byOwner);
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
		return succeed;
	}

}
