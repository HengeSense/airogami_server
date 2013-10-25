package com.airogami.application;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.persistence.EntityExistsException;

import com.airogami.application.exception.ApplicationException;
import com.airogami.common.constants.MessageConstants;
import com.airogami.common.notification.MessageNotifiedInfo;
import com.airogami.common.notification.NotifiedInfo;
import com.airogami.common.notification.SilentNotifiedInfo;
import com.airogami.persistence.classes.AccountStatLeft;
import com.airogami.persistence.classes.NeoPlane;
import com.airogami.persistence.classes.OldPlane;
import com.airogami.persistence.daos.DaoUtils;
import com.airogami.persistence.entities.Account;
import com.airogami.persistence.entities.Category;
import com.airogami.persistence.entities.EntityManagerHelper;
import com.airogami.persistence.entities.Message;
import com.airogami.persistence.entities.Plane;
import com.airogami.persistence.entities.PlaneHist;
import com.airogami.persistence.entities.PlaneHistId;

public class PlaneService implements IPlaneService {
	
	public Category createCategory(Category category) throws ApplicationException{
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
	public Map<String, Object> sendPlane(Plane plane, int ownerId)
			throws ApplicationException {
		ApplicationException ae = null;
		String error = null;
		AccountStatLeft accountStatLeft = null;
		Message message = plane.getMessages().get(0);
		try {
			EntityManagerHelper.beginTransaction();
			if(DaoUtils.accountStatDao.verifySend(ownerId))
			{
				Account accountByOwnerId = DaoUtils.accountDao
						.getReference(ownerId);
				plane.setAccountByOwnerId(accountByOwnerId);
				message.setAccount(accountByOwnerId);
				DaoUtils.planeDao.save(plane);
				DaoUtils.planeDao.initLastMsgIdOfT(plane.getPlaneId(), message.getMessageId());
			}
			else{
				error = "limit";
			}
			accountStatLeft = DaoUtils.accountStatDao.getSendAndPickupLeftCounts(ownerId);
			EntityManagerHelper.commit();
		} catch (Throwable t) {
			//t.printStackTrace();
			if (t.getCause() == null) {
				ae = new ApplicationException();
			} else {
				if(t.getCause().getCause() instanceof EntityExistsException){
					error = "none";
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
		
		Map<String, Object> result = new TreeMap<String, Object>();
		if(error != null){
		    result.put("error", error);
		}
		else{
			//plane.setLastMsgIdOfTarget(message.getMessageId());
			result.put("plane", plane);
		}
		
		result.put("accountStatLeft", accountStatLeft);
		
		return result;
	}

	/*
	 * @see com.airogami.application.IPlaneService#replyPlane(long,
	 * com.airogami.persitence.entities.Message)
	 * lock plane first
	 */
	@Override
	public Map<String, Object> replyPlane(long planeId, int accountId, boolean byOwner, Message message)
			throws ApplicationException {
		ApplicationException ae = null;
		String error = null;
		MessageNotifiedInfo notifiedInfo = null;
		try {
			EntityManagerHelper.beginTransaction();
			if (DaoUtils.messageDao.verifyReply(planeId, accountId, byOwner)) {
				Plane plane = DaoUtils.planeDao.getReference(planeId);
				message.setPlane(plane);
				Account account = DaoUtils.accountDao.getReference(accountId);
				message.setAccount(account);
				DaoUtils.messageDao.save(message);
				DaoUtils.messageDao.flush();
				notifiedInfo = DaoUtils.messageDao.getNotifiedInfo(planeId, accountId);
				if(notifiedInfo != null){
					DaoUtils.planeDao.updateInc(planeId, notifiedInfo.getAccountId(), !byOwner);
					DaoUtils.messageDao.updateNeoMsgId(planeId, message.getMessageId(), !byOwner);
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
				//should process different types
				notifiedInfo.setContent(message.getContent());
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
				DaoUtils.hotDao.increaseLikesCount(oppositeAccountId, 1);
				DaoUtils.accountDao.increaseUpdateCount(oppositeAccountId, 1);
				notifiedInfo = DaoUtils.messageDao.getNotifiedInfo(planeId, accountId);
				if(notifiedInfo != null){
					DaoUtils.planeDao.updateInc(planeId, notifiedInfo.getAccountId(), !byOwner);
					DaoUtils.messageDao.updateNeoMsgId(planeId, message.getMessageId(), !byOwner);
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
	
	@Override
	public Map<String, Object> clearPlane(long planeId, int accountId)
			throws ApplicationException {
		ApplicationException ae = null;
		String error = null;
		SilentNotifiedInfo notifiedInfo = null;
		Object[] clearResult = null;
		Plane plane = null;
		try {
			EntityManagerHelper.beginTransaction();
			clearResult = DaoUtils.messageDao.clearPlane(planeId, accountId);
			if (clearResult != null) {
				
			} else {
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
		    if(plane != null){
				result.put("plane", plane);
			}
		}
		else{
			if((Integer)clearResult[1] == accountId){
				accountId = (Integer)clearResult[2];
			}
			else{
				accountId = (Integer)clearResult[1];
			}
			notifiedInfo = new SilentNotifiedInfo(accountId);
            result.put("clearMsgId", (Long)clearResult[0]);
			result.put("notifiedInfo", notifiedInfo);
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
		SilentNotifiedInfo notifiedInfo = null;
		try {
			EntityManagerHelper.beginTransaction();
			if(succeed = DaoUtils.planeDao.deletePlane(planeId, accountId, byOwner)){
				DaoUtils.messageDao.decreaseMessageCount(planeId);
				notifiedInfo = DaoUtils.messageDao.getSNotifiedInfo(planeId, byOwner);
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
			if(notifiedInfo != null){
				result.put("notifiedInfo", notifiedInfo);
			}
			result.put("succeed", succeed);
		}
		
		return result;
	}
	
	@Override
	public Map<String, Object> getNeoPlanes(int accountId, Long start, Long end, int limit,
			boolean forward) throws ApplicationException {
		if(limit > IPlaneService.MaxPlaneLimit || limit < 1)
			limit = IPlaneService.MaxPlaneLimit;
		List<NeoPlane> neoPlanes = null;
		ApplicationException ae = null;
		Map<String, Object> result = null;
		try {
			EntityManagerHelper.beginTransaction();
			neoPlanes = DaoUtils.planeDao.getNeoPlanes(accountId, start, end, limit + 1, forward);
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
		boolean more = neoPlanes.size() > limit;
		result = new TreeMap<String, Object>();
		result.put("more", more);
		result.put("neoPlanes", neoPlanes);
		return result;
	}
	
	@Override
	public Map<String, Object> getPlanes(int accountId, List<Long> planeIds, boolean updated) throws ApplicationException{
		ApplicationException ae = null;
		Object planes = null;
		try {
			EntityManagerHelper.beginTransaction();
			planes = DaoUtils.planeDao.getPlanes(accountId, planeIds, updated);
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
		result.put("planes", planes);
		return result;
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
	public Map<String, Object> viewedMessages(int accountId, long planeId, long lastMsgId, boolean byOwner)
			throws ApplicationException {
		ApplicationException ae = null;
		Object results[] = null;
		try {
			EntityManagerHelper.beginTransaction();
			results = DaoUtils.messageDao.viewedMessage(accountId, planeId, lastMsgId, byOwner);
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
		result.put("succeed", results[0]);
		if(results[1] != null){
			result.put("lastMsgId", results[1]);
		}
		if(results[2] != null){
			accountId = (Integer)results[2];
			SilentNotifiedInfo notifiedInfo = new SilentNotifiedInfo(accountId);
			result.put("notifiedInfo", notifiedInfo);
		}
		return result;
	}

}
