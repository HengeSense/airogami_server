package com.airogami.application;

import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.airogami.application.exception.ApplicationException;
import com.airogami.common.constants.ChainMessageConstants;
import com.airogami.persitence.daos.DaoUtils;
import com.airogami.persitence.entities.Account;
import com.airogami.persitence.entities.Chain;
import com.airogami.persitence.entities.ChainMessage;
import com.airogami.persitence.entities.ChainMessageId;
import com.airogami.persitence.entities.EntityManagerHelper;
import com.airogami.persitence.entities.Message;
import com.airogami.persitence.entities.Chain;

public class ChainService implements IChainService {

	/*
	 * @see com.airogami.application.IChainService#sendChain(Chain chain, long
	 * targetId, long ownerId)
	 */
	@Override
	public Chain sendChain(Chain chain, long ownerId)
			throws ApplicationException {

		ApplicationException ae = null;
		try {
			EntityManagerHelper.beginTransaction();
			Account account = DaoUtils.accountDao
					.getReference(ownerId);
			chain.setAccount(account);
			ChainMessage chainMessage = chain.getChainMessages().iterator().next();
			chain.getChainMessages().clear();			
			DaoUtils.chainDao.save(chain);
			DaoUtils.chainDao.flush();
			ChainMessageId id = new ChainMessageId(chain.getChainId(), ownerId);			
			chainMessage.setId(id);
			chainMessage.setStatus((short)ChainMessageConstants.StatusReplied);
			chain.getChainMessages().add(chainMessage);
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
		return chain;
	}

    /*
     * @see com.airogami.application.IChainService#replyChain(long, long, java.lang.String, int)
     */     
	@Override
	public ChainMessage replyChain(long accountId, long chainId, String content, int type)
			throws ApplicationException {
		ApplicationException ae = null;
		ChainMessage chainMessage = null;
		try {
			EntityManagerHelper.beginTransaction();
			if(DaoUtils.chainMessageDao.replyChainMessage(accountId, chainId, content, type)){
				DaoUtils.chainDao.increasePassCount(chainId, 1);
				chainMessage = DaoUtils.chainMessageDao.findById(new ChainMessageId(chainId, accountId));
			}
			else{
				ae = new ApplicationException("replyChain failed");
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
		return chainMessage;
	}

	@Override
	public Chain matchChain(long chainId) throws ApplicationException {
		ApplicationException ae = null;
		Chain chain = null;
		try {
			EntityManagerHelper.beginTransaction();
			chain = DaoUtils.chainDao.findById(chainId);
			if(chain == null){
				ae = new ApplicationException("matchChain failed");
			}
			else{
				long accountId;
				String country = chain.getCountry(), province = chain.getProvince(), city = chain
						.getCity();
				if (city != null && city.length() > 0) {
					accountId = DaoUtils.accountDao.randChainAccount(
							chainId, country, province, city);
				} else if (province != null && province.length() > 0) {
					accountId = DaoUtils.accountDao.randChainAccount(
							chainId, country, province);
				} else if (country != null && country.length() > 0) {
					accountId = DaoUtils.accountDao.randChainAccount(
							chainId, country);
				} else {
					accountId = DaoUtils.accountDao.randChainAccount(chainId);
				}
				ChainMessage chainMessage = new ChainMessage();
				chainMessage.setId(new ChainMessageId(chainId, accountId));
				chainMessage.setType((short)0);
				DaoUtils.chainMessageDao.save(chainMessage);
				DaoUtils.chainDao.flush();
				DaoUtils.chainDao.increaseMatchCount(chainId, 1);
				chain.getChainMessages();				
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
		chain.setMatchCount(chain.getMatchCount() + 1);
		return chain;
	}

	@Override
	public int throwChain(long chainId, long accountId)
			throws ApplicationException {
		ApplicationException ae = null;
		int count = 0;
		try {
			EntityManagerHelper.beginTransaction();
			if (DaoUtils.chainDao.throwChain(chainId, accountId) == false) {
				ae = new ApplicationException("throwChain failed");
			}
			else{
				count = DaoUtils.chainDao.getChainMatchCount(chainId);
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
	public void deleteChain(long chainId, long accountId)
			throws ApplicationException {
		ApplicationException ae = null;
		try {
			EntityManagerHelper.beginTransaction();
			if (DaoUtils.chainDao.deleteChain(chainId, accountId) == false) {
				ae = new ApplicationException("deleteChain failed");
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
	public Map<String, Object> obtainChains(long accountId, String start, int limit,
			boolean forward) throws ApplicationException {
		if(limit > IChainService.MaxLimit)
			limit = IChainService.MaxLimit; 
		List<Chain> chains = null;
		ApplicationException ae = null;
		Map<String, Object> result = null;
		try {
			EntityManagerHelper.beginTransaction();
			chains = DaoUtils.chainDao.obtainChains(accountId, start, limit + 1, forward); 			
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
	public List<ChainMessage> obtainChainMessages(long accountId, long chainId)
			throws ApplicationException {
		ApplicationException ae = null;
		List<ChainMessage> chainMessages = null;
		try {
			EntityManagerHelper.beginTransaction();
			chainMessages = DaoUtils.chainMessageDao.obtainChainMessages(accountId, chainId);			
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
		return chainMessages;
	}

	@Override
	public void viewedChainMessage(long accountId, long chainId, String last)
			throws ApplicationException {
		Timestamp lastTimestamp = Timestamp.valueOf(last);
		long time = System.currentTimeMillis();
		if(lastTimestamp.getTime() > time){
			lastTimestamp.setTime(time);
		}
		ApplicationException ae = null;
		try {
			EntityManagerHelper.beginTransaction();
			if (DaoUtils.chainMessageDao.viewedChainMessage(accountId, chainId, lastTimestamp) == false) {
				ae = new ApplicationException("viewedChainMessage failed");
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
