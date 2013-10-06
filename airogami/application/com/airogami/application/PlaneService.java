package com.airogami.application;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.persistence.EntityExistsException;

import com.airogami.application.exception.ApplicationException;
import com.airogami.common.NotifiedInfo;
import com.airogami.common.constants.MessageConstants;
import com.airogami.common.constants.PlaneConstants;
import com.airogami.persistence.daos.DaoUtils;
import com.airogami.persistence.entities.Account;
import com.airogami.persistence.entities.Category;
import com.airogami.persistence.entities.EntityManagerHelper;
import com.airogami.persistence.entities.Message;
import com.airogami.persistence.entities.NewPlane;
import com.airogami.persistence.entities.OldPlane;
import com.airogami.persistence.entities.Plane;
import com.airogami.persistence.entities.PlaneHist;
import com.airogami.persistence.entities.PlaneHistId;

public class PlaneService implements IPlaneService {
	
	public Category createCatrgory(Category category) throws ApplicationException{
		ApplicationException ae = null;
		try {
			EntityManagerHelper.beginTransaction();
			DaoUtils.categoryDao.save(category);
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
		return category;
	}

	/*
	 * @see com.airogami.application.IPlaneService#sendPlane(Plane plane, long
	 * targetId, long ownerId)
	 */
	@Override
	public Plane sendPlane(Plane plane, int ownerId)
			throws ApplicationException {

		ApplicationException ae = null;
		Message message = plane.getMessages().get(0);
		try {
			EntityManagerHelper.beginTransaction();
			Account accountByOwnerId = DaoUtils.accountDao
					.getReference(ownerId);
			plane.setAccountByOwnerId(accountByOwnerId);
			message.setAccount(accountByOwnerId);
			DaoUtils.planeDao.save(plane);
			//may be not needed
			DaoUtils.planeDao.initLastMsgIdOfTarget(plane.getPlaneId(), message.getMessageId());
			EntityManagerHelper.commit();
		} catch (Throwable t) {
			//t.printStackTrace();
			if (t.getCause() == null) {
				ae = new ApplicationException();
			} else {
				if(t.getCause().getCause() instanceof EntityExistsException){
					plane = null;
				}
				else{
					ae = new ApplicationException(t.getCause().getMessage());
				}
			}
		} finally {
			EntityManagerHelper.closeEntityManager();
		}
		if (ae != null) {
			throw ae;
		}
		plane.setLastMsgIdOfTarget(message.getMessageId());
		return plane;
	}

	/*
	 * @see com.airogami.application.IPlaneService#replyPlane(long,
	 * com.airogami.persitence.entities.Message)
	 * lock plane first
	 */
	@Override
	public Map<String, Object> replyPlane(long planeId, int accountId, Message message)
			throws ApplicationException {
		ApplicationException ae = null;
		String error = null;
		NotifiedInfo notifiedInfo = null;
		try {
			EntityManagerHelper.beginTransaction();
			if (DaoUtils.planeDao.verifyReply(planeId, accountId)) {
				Plane plane = DaoUtils.planeDao.getReference(planeId);
				message.setPlane(plane);
				Account account = DaoUtils.accountDao.getReference(accountId);
				message.setAccount(account);
				DaoUtils.messageDao.save(message);
				DaoUtils.messageDao.flush();
				DaoUtils.planeDao.updateInc(planeId);
				notifiedInfo = DaoUtils.planeDao.getNotifiedInfo(planeId, accountId);
				if(notifiedInfo != null){
					DaoUtils.accountStatDao.increaseMsgCount(notifiedInfo.getAccountId(), 1);
				}
				
			} else {
				error = "none";
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
		Map<String, Object> result = new TreeMap<String, Object>();
		if(error != null){
		    result.put("error", error);
		}
		else{
			result.put("message", message);
			if(notifiedInfo != null){
				notifiedInfo.setMessagesCount(notifiedInfo.getMessagesCount() + 1);
				result.put("notifiedInfo", notifiedInfo);
			}
		}
		return result;
	}
	
	@Override
	public List<Plane> pickupPlane(int accountId, int count) throws ApplicationException	{
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
		Integer accountId = null;
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
	public Map<String, Object> throwPlane(long planeId, int accountId)
			throws ApplicationException {
		ApplicationException ae = null;
		boolean canMatchAgain = false;
		String error = null;
		Plane plane = null;
		PlaneHistId id = new PlaneHistId(planeId, accountId);
		PlaneHist planeHist = new PlaneHist();
		planeHist.setId(id);
		try {
			EntityManagerHelper.beginTransaction();
			if (DaoUtils.planeDao.throwPlane(planeId, accountId)) {
				canMatchAgain = DaoUtils.planeDao.canPlaneMatch(planeId);
				DaoUtils.planeHistDao.save(planeHist);
			}
			else{
				plane = DaoUtils.planeDao.getPlane(planeId, accountId);
				if(plane == null){
					error = "none";
				}
				else{
					error =  "others";
				}
				
			}
			EntityManagerHelper.commit();
		} catch (Throwable t) {
			
			if(t.getCause() == null) {
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
		if(error != null){
			result.put("error", error);
			if(plane != null){
				result.put("plane", plane);
			}
		}
		else{
			result.put("canMatchAgain", canMatchAgain);
		}
		
		return result;
		
	}
	
	@Override
	public Map<String, Object> likePlane(long planeId,int accountId, boolean byOwner) throws ApplicationException{
		ApplicationException ae = null;
		Message message = new Message();
		message.setContent(MessageConstants.LikeContent);
		message.setType(MessageConstants.MessageTypeLike);
		String error = null;
		Plane plane = null;
		NotifiedInfo notifiedInfo = null;
		try {			
			EntityManagerHelper.beginTransaction();
			Integer oppositeAccountId;
			if ((oppositeAccountId = DaoUtils.planeDao.likePlane(planeId, accountId, byOwner)) != null) {
				plane = DaoUtils.planeDao.getReference(planeId);
				message.setPlane(plane);
				Account account = DaoUtils.accountDao.getReference(accountId);
				message.setAccount(account);
				DaoUtils.messageDao.save(message);
				DaoUtils.messageDao.flush();
				DaoUtils.profileDao.increaseLikesCount(oppositeAccountId, 1);	
				DaoUtils.planeDao.updateInc(planeId);
				notifiedInfo = DaoUtils.planeDao.getNotifiedInfo(planeId, accountId);
				if(notifiedInfo != null){
					DaoUtils.accountStatDao.increaseMsgCount(notifiedInfo.getAccountId(), 1);
				}
			}
			else{
				plane = DaoUtils.planeDao.getPlane(planeId, accountId);
				if(plane == null){
					error = "none";
				}
				else{
					error =  "others";
				}
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
			
		if(error != null){
			result.put("error", error);
			if(plane != null){
				result.put("plane", plane);
			}
		}
		else{
			result.put("message", message);
			if(notifiedInfo != null){
				notifiedInfo.setMessagesCount(notifiedInfo.getMessagesCount() + 1);
				result.put("notifiedInfo", notifiedInfo);
			}
		}
		
		return result;	
	}
	
	/*
	 * lock plane first
	 */
	
	@Override
	public Map<String, Object> deletePlane(long planeId, int accountId, boolean byOwner)
			throws ApplicationException {
		ApplicationException ae = null;
		boolean succeed = false;
		String error = null;
		Plane plane = null;
		try {
			EntityManagerHelper.beginTransaction();
			if(succeed = DaoUtils.planeDao.deletePlane(planeId, accountId, byOwner)){
				DaoUtils.messageDao.decreaseMessageCount(planeId, accountId);
			}
			else{
				plane = DaoUtils.planeDao.getPlane(planeId, accountId);
				if(plane == null){
					error = "none";
				}
				else{
					error =  "others";
				}
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
		Map<String, Object> result = new TreeMap<String,Object>();
		if(error != null){
			result.put("error", error);
			if(plane != null){
				result.put("plane", plane);
			}
		}
		else{
			result.put("succeed", succeed);
		}
		
		return result;
	}
	
	@Override
	public Map<String, Object> getNewPlanes(int accountId, Long start, Long end, int limit,
			boolean forward) throws ApplicationException {
		if(limit > IPlaneService.MaxPlaneLimit || limit < 1)
			limit = IPlaneService.MaxPlaneLimit;
		List<NewPlane> newPlanes = null;
		ApplicationException ae = null;
		Map<String, Object> result = null;
		try {
			EntityManagerHelper.beginTransaction();
			newPlanes = DaoUtils.planeDao.getNewPlanes(accountId, start, end, limit + 1, forward);
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
		boolean more = newPlanes.size() > limit;
		result = new TreeMap<String, Object>();
		result.put("more", more);
		result.put("newPlanes", newPlanes);
		return result;
	}
	
	@Override
	public List<Plane> getPlanes(int accountId, List<Long> planeIds) throws ApplicationException{
		ApplicationException ae = null;
		List<Plane> planes = null;
		try {
			EntityManagerHelper.beginTransaction();
			planes = DaoUtils.planeDao.getPlanes(accountId, planeIds);
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
	public Map<String, Object> getOldPlanes(int accountId, Long start, Long end, int limit,
			boolean forward) throws ApplicationException{
		if(limit > IPlaneService.MaxOldPlanesLimit || limit < 1)
			limit = IPlaneService.MaxOldPlanesLimit;
		List<OldPlane> oldPlanes = null;
		ApplicationException ae = null;
		Map<String, Object> result = null;
		try {
			EntityManagerHelper.beginTransaction();
			oldPlanes = DaoUtils.planeDao.getOldPlanes(accountId, start, end, limit + 1, forward);
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
		boolean more = oldPlanes.size() > limit;
		result = new TreeMap<String, Object>();
		result.put("more", more);
		result.put("oldPlanes", oldPlanes);
		return result;
	}

	@Override
	public Map<String, Object> obtainPlanes(int accountId, Long start, Long end, int limit,
			boolean forward) throws ApplicationException {
		if(limit > IPlaneService.MaxPlaneLimit || limit < 1)
			limit = IPlaneService.MaxPlaneLimit;
		List<Plane> planes = null;
		ApplicationException ae = null;
		Map<String, Object> result = null;
		try {
			EntityManagerHelper.beginTransaction();
			planes = DaoUtils.planeDao.obtainPlanes(accountId, start, end, limit + 1, forward);
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
	public Map<String, Object> obtainPlaneIds(int accountId, long startId, int limit) throws ApplicationException{
		if(limit > IPlaneService.MaxOldPlanesLimit || limit < 1)
			limit = IPlaneService.MaxOldPlanesLimit;
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
	public Map<String, Object> obtainPlaneIds(int accountId, Long start, Long end, int limit,
			boolean forward) throws ApplicationException{
		if(limit > IPlaneService.MaxOldPlanesLimit || limit < 1)
			limit = IPlaneService.MaxOldPlanesLimit;
		List<Long> planeIds = null;
		ApplicationException ae = null;
		Map<String, Object> result = null;
		try {
			EntityManagerHelper.beginTransaction();
			planeIds = DaoUtils.planeDao.obtainPlaneIds(accountId, start, end, limit + 1, forward);
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
	public Map<String, Object> receivePlanes(int accountId, Long start, Long end, int limit,
			boolean forward) throws ApplicationException {
		if(limit > IPlaneService.MaxPlaneLimit || limit < 1)
			limit = IPlaneService.MaxPlaneLimit;
		List<Plane> planes = null;
		ApplicationException ae = null;
		Map<String, Object> result = null;
		try {
			EntityManagerHelper.beginTransaction();
			planes = DaoUtils.planeDao.receivePlanes(accountId, start, end, limit + 1, forward);
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
	public Map<String, Object> receivePlaneIds(int accountId, long startId, int limit) throws ApplicationException{
		if(limit > IPlaneService.MaxOldPlanesLimit || limit < 1)
			limit = IPlaneService.MaxOldPlanesLimit;
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
	public Map<String, Object> receivePlaneIds(int accountId, Long start, Long end, int limit,
			boolean forward) throws ApplicationException{
		if(limit > IPlaneService.MaxOldPlanesLimit || limit < 1)
			limit = IPlaneService.MaxOldPlanesLimit;
		List<Long> planeIds = null;
		ApplicationException ae = null;
		Map<String, Object> result = null;
		try {
			EntityManagerHelper.beginTransaction();
			planeIds = DaoUtils.planeDao.receivePlaneIds(accountId, start, end, limit + 1, forward);
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
	public Map<String, Object> obtainMessages(int accountId, long planeId, Long startId,
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
	public boolean viewedMessages(int accountId, long planeId, long lastMsgId, boolean byOwner)
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
