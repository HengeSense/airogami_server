package com.airogami.application;

import java.sql.Timestamp;
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
				//ae = new ApplicationException("verifyReply failed");
				message = null;
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
		Long accountId = null;
		Boolean rematch = true;
		Boolean succeed = false;
		try {
			EntityManagerHelper.beginTransaction();
			accountId = DaoUtils.accountDao.randPlaneAccount(planeId);
			//plane may not exist
			if(accountId == null || DaoUtils.planeDao.match(planeId, accountId)){	
				//check whether planeId exists
				rematch = DaoUtils.planeDao.increaseMatchCount(planeId, 1);
				if(accountId != null){//match succeed
					rematch = false;
					succeed = true;
				}
			}
			else{
				//already matched or exceed maximum	 
				rematch = false;
			}
			
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
		Map<String, Object> result = new TreeMap<String,Object>();
		result.put("rematch", rematch);
		result.put("succeed", succeed);
		if(succeed){
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
			if ((oppositeAccountId = DaoUtils.planeDao.likePlane(planeId, accountId, byOwner)) != null) {
				Plane plane = DaoUtils.planeDao.getReference(planeId);
				message.setPlane(plane);
				Account account = DaoUtils.accountDao.getReference(accountId);
				message.setAccount(account);
				DaoUtils.messageDao.save(message);
				DaoUtils.messageDao.flush();
				DaoUtils.profileDao.increaseLikesCount(oppositeAccountId, 1);		
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
		if(oppositeAccountId != null){
			result.put("message", message);
		}		
		result.put("oppositeAccountId", oppositeAccountId);
		return result;	
	}

	@Override
	public boolean deletePlane(long planeId, long accountId, boolean byOwner)
			throws ApplicationException {
		ApplicationException ae = null;
		boolean result = false;
		try {
			EntityManagerHelper.beginTransaction();
			result = DaoUtils.planeDao.deletePlane(planeId, accountId, byOwner);
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
		return result;
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
	public Map<String, Object> obtainPlaneIds(long accountId, long startId, int limit) throws ApplicationException{
		if(limit > IPlaneService.MaxPlaneIdLimit || limit < 1)
			limit = IPlaneService.MaxPlaneIdLimit;
		List<Long> planeIds = null;
		ApplicationException ae = null;
		Map<String, Object> result = null;
		try {
			EntityManagerHelper.beginTransaction();
			planeIds = DaoUtils.planeDao.obtainPlaneIds(accountId, startId, limit + 1);
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
		boolean more = planeIds.size() > limit;
		result = new TreeMap<String, Object>();
		result.put("more", more);
		result.put("planeIds", planeIds);
		return result;
	}
	
	@Override
	public Map<String, Object> obtainPlaneIds(long accountId, int startIdx, Timestamp start,Timestamp end, int limit,
			boolean forward) throws ApplicationException{
		if(limit > IPlaneService.MaxPlaneIdLimit || limit < 1)
			limit = IPlaneService.MaxPlaneIdLimit;
		List<Long> planeIds = null;
		ApplicationException ae = null;
		Map<String, Object> result = null;
		try {
			EntityManagerHelper.beginTransaction();
			planeIds = DaoUtils.planeDao.obtainPlaneIds(accountId, startIdx, start, end, limit + 1, forward);
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
		boolean more = planeIds.size() > limit;
		result = new TreeMap<String, Object>();
		result.put("more", more);
		result.put("planeIds", planeIds);
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
	public Map<String, Object> receivePlaneIds(long accountId, long startId, int limit) throws ApplicationException{
		if(limit > IPlaneService.MaxPlaneIdLimit || limit < 1)
			limit = IPlaneService.MaxPlaneIdLimit;
		List<Long> planeIds = null;
		ApplicationException ae = null;
		Map<String, Object> result = null;
		try {
			EntityManagerHelper.beginTransaction();
			planeIds = DaoUtils.planeDao.receivePlaneIds(accountId, startId, limit + 1);
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
		boolean more = planeIds.size() > limit;
		result = new TreeMap<String, Object>();
		result.put("more", more);
		result.put("planeIds", planeIds);
		return result;
	}	
	
	@Override
	public Map<String, Object> receivePlaneIds(long accountId, int startIdx, Timestamp start,Timestamp end, int limit,
			boolean forward) throws ApplicationException{
		if(limit > IPlaneService.MaxPlaneIdLimit || limit < 1)
			limit = IPlaneService.MaxPlaneIdLimit;
		List<Long> planeIds = null;
		ApplicationException ae = null;
		Map<String, Object> result = null;
		try {
			EntityManagerHelper.beginTransaction();
			planeIds = DaoUtils.planeDao.receivePlaneIds(accountId, startIdx, start, end, limit + 1, forward);
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
		boolean more = planeIds.size() > limit;
		result = new TreeMap<String, Object>();
		result.put("more", more);
		result.put("planeIds", planeIds);
		return result;
	}	

	@Override
	public Map<String, Object> obtainMessages(long accountId, long planeId, Long startId,
			int limit, boolean forward)
			throws ApplicationException {
		if(limit > IPlaneService.MaxMessageLimit || limit < 1)
			limit = IPlaneService.MaxMessageLimit;
		List<Message> messages = null;
		ApplicationException ae = null;
		Map<String, Object> result = null;
		try {
			EntityManagerHelper.beginTransaction();
			messages = DaoUtils.messageDao.obtainMessages(accountId, planeId, startId, limit + 1, forward);
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
	public boolean viewedMessages(long accountId, long planeId, long lastMsgId, boolean byOwner)
			throws ApplicationException {
		ApplicationException ae = null;
		boolean succeed = false;
		try {
			EntityManagerHelper.beginTransaction();
			succeed = DaoUtils.messageDao.viewedMessage(accountId, planeId, lastMsgId, byOwner);
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
