package com.airogami.application;

import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.persistence.EntityExistsException;

import com.airogami.application.exception.ApplicationException;
import com.airogami.common.ChainMessageNotifiedInfos;
import com.airogami.common.NotifiedInfo;
import com.airogami.common.constants.ChainConstants;
import com.airogami.common.constants.ChainMessageConstants;
import com.airogami.common.constants.MessageConstants;
import com.airogami.persistence.classes.AccountStatLeft;
import com.airogami.persistence.classes.NewChain;
import com.airogami.persistence.classes.OldChain;
import com.airogami.persistence.daos.DaoUtils;
import com.airogami.persistence.entities.Account;
import com.airogami.persistence.entities.Chain;
import com.airogami.persistence.entities.ChainHist;
import com.airogami.persistence.entities.ChainHistId;
import com.airogami.persistence.entities.ChainMessage;
import com.airogami.persistence.entities.ChainMessageId;
import com.airogami.persistence.entities.EntityManagerHelper;
import com.airogami.persistence.entities.Message;
import com.airogami.persistence.entities.Plane;

public class ChainService implements IChainService {

	/*
	 * @see com.airogami.application.IChainService#sendChain(Chain chain, long
	 * targetId, long ownerId)
	 */
	@Override
	public Map<String, Object> sendChain(Chain chain, int ownerId)
			throws ApplicationException {
		String error = null;
		AccountStatLeft accountStatLeft = null;
		ApplicationException ae = null;
		try {
			EntityManagerHelper.beginTransaction();
			if(DaoUtils.accountStatDao.verifySend(ownerId))
			{
				Account account = DaoUtils.accountDao
						.getReference(ownerId);
				chain.setAccount(account);
				ChainMessage chainMessage = chain.getChainMessages().iterator().next();
				chain.getChainMessages().clear();
				DaoUtils.chainDao.save(chain);
				DaoUtils.chainDao.flush();
				ChainMessageId id = new ChainMessageId(chain.getChainId(), ownerId);			
				chainMessage.setId(id);
				chain.getChainMessages().add(chainMessage);
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
				//ownerId not exist
				if(t.getCause() instanceof EntityExistsException){
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
			result.put("chain", chain);
		}
		result.put("accountStatLeft", accountStatLeft);
		return result; 
	}

    /*
     * @see com.airogami.application.IChainService#replyChain(long, long, java.lang.String, int)
     */     
	@Override
	public Map<String, Object> replyChain(int accountId, long chainId, String content, int type)
			throws ApplicationException {
		ApplicationException ae = null;
		ChainMessage chainMessage = null;
		String error = null;
		List<NotifiedInfo> notifiedInfos = null;
		String name = null;
		ChainMessageId id = new ChainMessageId(chainId, accountId);
		try {
			EntityManagerHelper.beginTransaction();
			if(DaoUtils.chainMessageDao.replyChainMessage(accountId, chainId, content, type)){
				//DaoUtils.chainDao.increasePassCount(chainId, 1);
				chainMessage = DaoUtils.chainMessageDao.findById(id);
				DaoUtils.chainDao.updateInc(chainId);
				DaoUtils.chainDao.increaseChainMessageCount(chainId, accountId);
				notifiedInfos = DaoUtils.chainDao.getNotifiedInfos(chainId, accountId);
				name = DaoUtils.profileDao.getFullName(accountId);
			}
			else{
				chainMessage = DaoUtils.chainDao.getChainMessage(accountId, chainId);
				if(chainMessage == null){
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
			if(chainMessage != null){
				result.put("chainMessage", chainMessage);
			}
		}
		else{
			NotifiedInfo notifiedInfo = new ChainMessageNotifiedInfos(notifiedInfos, name, chainMessage.getContent());
			result.put("chainMessage", chainMessage);
			result.put("notifiedInfo", notifiedInfo);
		}
		return result;
	}
	
	@Override
	public List<Chain> pickupChain(int accountId, int count) throws ApplicationException{
		ApplicationException ae = null;
		List<Chain> chains = null;
		try {
			EntityManagerHelper.beginTransaction();
			chains = DaoUtils.chainDao.pickup(accountId, count);			
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
		return chains;
	}

	@Override
	public Map<String, Object> matchChain(long chainId) throws ApplicationException {
		ApplicationException ae = null;
		Boolean rematch = true;
		Boolean succeed = false;
		Integer accountId = null;
		try {
			EntityManagerHelper.beginTransaction();
			accountId = DaoUtils.accountDao.randChainAccount(chainId);
			if(accountId == null || DaoUtils.chainDao.match(chainId, accountId)){
				rematch = DaoUtils.chainDao.increaseMatchCount(chainId, 1);
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
		result.put("rematch", rematch);
		result.put("succeed", succeed);
		if(succeed){
			result.put("accountId", accountId);
		}		
		
		return result;
	}

	@Override
	public Map<String, Object> throwChain(long chainId, int accountId)
			throws ApplicationException {
		ApplicationException ae = null;
		boolean canMatchedAgain = false;
		String error = null;
		ChainMessage chainMessage = null;
		ChainHistId id = new ChainHistId(accountId, chainId);
		ChainHist chainHist = new ChainHist();
		chainHist.setId(id);
		try {
			EntityManagerHelper.beginTransaction();
			if (DaoUtils.chainDao.throwChain(chainId, accountId)) {
				canMatchedAgain = DaoUtils.chainDao.canChainMatch(chainId);
				DaoUtils.chainHistDao.save(chainHist);
			}
			else{
				chainMessage = DaoUtils.chainDao.getChainMessage(accountId, chainId);
				if(chainMessage == null){
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
			if(chainMessage != null){
				result.put("chainMessage", chainMessage);
			}
		}
		else{
			result.put("canMatchAgain", canMatchedAgain);
		}
		return result;
	}

	@Override
	public Map<String, Object> deleteChain(long chainId, int accountId)
			throws ApplicationException {
		ApplicationException ae = null;
		boolean succeed = false;
		String error = null;
		ChainMessage chainMessage = null;
		try {
			EntityManagerHelper.beginTransaction();
			if(succeed = DaoUtils.chainDao.deleteChain(chainId, accountId)){
				DaoUtils.chainMessageDao.decreaseChainMessageCount(chainId, accountId);
			}
			else{
				chainMessage = DaoUtils.chainDao.getChainMessage(accountId, chainId);
				if(chainMessage == null){
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
			if(chainMessage != null){
				result.put("chainMessage", chainMessage);
			}
		}
		else{
			result.put("succeed", succeed);
		}
		return result;
	}

	@Override
	public Map<String, Object> obtainChains(int accountId, Long start, Long end, int limit,
			boolean forward) throws ApplicationException {
		if(limit > IChainService.MaxChainLimit || limit < 1)
			limit = IChainService.MaxChainLimit; 
		List<Chain> chains = null;
		ApplicationException ae = null;
		Map<String, Object> result = null;
		try {
			EntityManagerHelper.beginTransaction();
			chains = DaoUtils.chainDao.obtainChains(accountId, start, end, limit + 1, forward); 			
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
		boolean more = chains.size() > limit;
		result = new TreeMap<String, Object>();
		result.put("more", more);
		result.put("chains", chains);
		return result;
	}
	
	@Override
	public Map<String, Object> getNewChains(int accountId, Long start, Long end, int limit,
			boolean forward) throws ApplicationException {
		if(limit > IChainService.MaxChainLimit || limit < 1)
			limit = IChainService.MaxChainLimit; 
		List<NewChain> newChains = null;
		ApplicationException ae = null;
		Map<String, Object> result = null;
		try {
			EntityManagerHelper.beginTransaction();
			newChains = DaoUtils.chainDao.getNewChains(accountId, start, end, limit + 1, forward); 			
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
		boolean more = newChains.size() > limit;
		result = new TreeMap<String, Object>();
		result.put("more", more);
		result.put("newChains", newChains);
		return result;
	}
	
	@Override
	public List<Chain> getChains(int accountId, List<Long> chainIds) throws ApplicationException {
		List<Chain> chains = null;
		ApplicationException ae = null;
		try {
			EntityManagerHelper.beginTransaction();
			chains = DaoUtils.chainDao.getChains(accountId, chainIds); 			
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
		return chains;
	}
	
	@Override
	public Map<String, Object> getOldChains(int accountId, Long start, Long end, int limit,
			boolean forward) throws ApplicationException {
		if(limit > IChainService.MaxOldChainsLimit || limit < 1)
			limit = IChainService.MaxOldChainsLimit; 
		List<OldChain> oldChains = null;
		ApplicationException ae = null;
		Map<String, Object> result = null;
		try {
			EntityManagerHelper.beginTransaction();
			oldChains = DaoUtils.chainDao.getOldChains(accountId, start, end, limit + 1, forward); 			
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
		boolean more = oldChains.size() > limit;
		result = new TreeMap<String, Object>();
		result.put("more", more);
		result.put("oldChains", oldChains);
		return result;
	}
	
	
	@Override
	public Map<String, Object> obtainChainIds(int accountId, Long start, Long end, int limit,
			boolean forward) throws ApplicationException {
		if(limit > IChainService.MaxOldChainsLimit || limit < 1)
			limit = IChainService.MaxOldChainsLimit; 
		List<Long> chainIds = null;
		ApplicationException ae = null;
		Map<String, Object> result = null;
		try {
			EntityManagerHelper.beginTransaction();
			chainIds = DaoUtils.chainDao.obtainChainIds(accountId, start, end, limit + 1, forward); 			
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
		boolean more = chainIds.size() > limit;
		result = new TreeMap<String, Object>();
		result.put("more", more);
		result.put("chainIds", chainIds);
		return result;
	}
	
	@Override
	public Map<String, Object> obtainChainIds(int accountId, long startId, int limit) throws ApplicationException {
		if(limit > IChainService.MaxOldChainsLimit || limit < 1)
			limit = IChainService.MaxOldChainsLimit; 
		List<Long> chainIds = null;
		ApplicationException ae = null;
		Map<String, Object> result = null;
		try {
			EntityManagerHelper.beginTransaction();
			chainIds = DaoUtils.chainDao.obtainChainIds(accountId, startId, limit + 1); 			
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
		boolean more = chainIds.size() > limit;
		result = new TreeMap<String, Object>();
		result.put("more", more);
		result.put("chainIds", chainIds);
		return result;
	}
	
	@Override
	public Map<String, Object> receiveChains(int accountId, Long start, Long end, int limit,
			boolean forward) throws ApplicationException {
		if(limit > IChainService.MaxChainLimit || limit < 1)
			limit = IChainService.MaxChainLimit; 
		List<Chain> chains = null;
		ApplicationException ae = null;
		Map<String, Object> result = null;
		try {
			EntityManagerHelper.beginTransaction();
			chains = DaoUtils.chainDao.receiveChains(accountId, start, end, limit + 1, forward); 			
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
		boolean more = chains.size() > limit;
		result = new TreeMap<String, Object>();
		result.put("more", more);
		result.put("chains", chains);
		return result;
	}
	
	@Override
	public Map<String, Object> receiveChainIds(int accountId, Long start, Long end, int limit,
			boolean forward) throws ApplicationException {
		if(limit > IChainService.MaxOldChainsLimit || limit < 1)
			limit = IChainService.MaxOldChainsLimit; 
		List<Long> chainIds = null;
		ApplicationException ae = null;
		Map<String, Object> result = null;
		try {
			EntityManagerHelper.beginTransaction();
			chainIds = DaoUtils.chainDao.receiveChainIds(accountId, start, end, limit + 1, forward); 			
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
		boolean more = chainIds.size() > limit;
		result = new TreeMap<String, Object>();
		result.put("more", more);
		result.put("chainIds", chainIds);
		return result;
	}
	
	@Override
	public Map<String, Object> receiveChainIds(int accountId, long startId, int limit) throws ApplicationException {
		if(limit > IChainService.MaxOldChainsLimit || limit < 1)
			limit = IChainService.MaxOldChainsLimit; 
		List<Long> chainIds = null;
		ApplicationException ae = null;
		Map<String, Object> result = null;
		try {
			EntityManagerHelper.beginTransaction();
			chainIds = DaoUtils.chainDao.receiveChainIds(accountId, startId, limit + 1); 			
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
		boolean more = chainIds.size() > limit;
		result = new TreeMap<String, Object>();
		result.put("more", more);
		result.put("chainIds", chainIds);
		return result;
	}

	@Override
	public Map<String, Object> obtainChainMessages(int accountId, long chainId, Timestamp last, int limit)
			throws ApplicationException {
		if(limit > IChainService.MaxChainMessageLimit || limit < 1)
			limit = IChainService.MaxChainMessageLimit; 
		ApplicationException ae = null;
		List<ChainMessage> chainMessages = null;
		try {
			EntityManagerHelper.beginTransaction();
			chainMessages = DaoUtils.chainMessageDao.obtainChainMessages(accountId, chainId, last, limit + 1);
			//test
			for(int i = 0; i < chainMessages.size(); ++i){
				ChainMessage chainMessage = chainMessages.get(i);
				if(chainMessage.getAccount() == null){
					System.err.println("Wrong ObtainChainMessages");
					break;
				}
			}
			//test
			
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
		boolean more = chainMessages.size() > limit;
		Map<String, Object> result = new TreeMap<String, Object>();
		result.put("more", more);
		result.put("chainMessages", chainMessages);
		return result;
	}

	@Override
	public Map<String, Object> viewedChainMessages(int accountId, long chainId, Timestamp last)
			throws ApplicationException {
		boolean succeed = false;
		ApplicationException ae = null;
		Object[] results = null;
		try {
			EntityManagerHelper.beginTransaction();
			results = DaoUtils.chainMessageDao.viewedChainMessage(accountId, chainId, last);
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
			result.put("lastViewedTime", ServiceUtils.format((Timestamp)results[1]));
		}
		return result;
	}
}
