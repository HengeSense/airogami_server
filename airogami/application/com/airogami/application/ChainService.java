package com.airogami.application;

import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.airogami.application.exception.ApplicationException;
import com.airogami.common.constants.ChainConstants;
import com.airogami.common.constants.ChainMessageConstants;
import com.airogami.common.constants.MessageConstants;
import com.airogami.persitence.daos.DaoUtils;
import com.airogami.persitence.entities.Account;
import com.airogami.persitence.entities.Chain;
import com.airogami.persitence.entities.ChainMessage;
import com.airogami.persitence.entities.ChainMessageId;
import com.airogami.persitence.entities.EntityManagerHelper;
import com.airogami.persitence.entities.Message;
import com.airogami.persitence.entities.Chain;
import com.airogami.persitence.entities.Plane;

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
	public List<Chain> pickupChain(long accountId, int count) throws ApplicationException{
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
		Chain chain = null;
		Long accountId = null;
		try {
			EntityManagerHelper.beginTransaction();
			chain = DaoUtils.chainDao.findById(chainId);
			if(chain != null){ 
				if(chain.getStatus() == ChainConstants.StatusUnmatched 
						&& chain.getMatchCount() < chain.getMaxMatchCount()
						&& chain.getPassCount() < chain.getMaxPassCount()){					
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
					if(accountId == null || DaoUtils.chainDao.match(chainId, accountId)){
						DaoUtils.chainDao.increaseMatchCount(chainId, 1);
					}
					else{
						//already matched or exceed maximum
						chain = null;
					}	
				}
				else
				{
					////already matched
					chain = null;
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
		if(chain != null){
			chain.setMatchCount(chain.getMatchCount() + 1);
			result.put("chain", chain);
		}
		if(accountId != null){
			result.put("accountId", accountId);
		}		
		
		return result;
	}

	@Override
	public boolean throwChain(long chainId, long accountId)
			throws ApplicationException {
		ApplicationException ae = null;
		boolean canMatchedAgain = false;
		try {
			EntityManagerHelper.beginTransaction();
			if (DaoUtils.chainDao.throwChain(chainId, accountId) == false) {
				ae = new ApplicationException("throwChain failed");
			}
			else{
				canMatchedAgain = DaoUtils.chainDao.canChainMatch(chainId);
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
		return canMatchedAgain;
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
	public Map<String, Object> obtainChains(long accountId, int startIdx, Timestamp start, Timestamp end, int limit,
			boolean forward) throws ApplicationException {
		if(limit > IChainService.MaxLimit || limit < 1)
			limit = IChainService.MaxLimit; 
		List<Chain> chains = null;
		ApplicationException ae = null;
		Map<String, Object> result = null;
		try {
			EntityManagerHelper.beginTransaction();
			chains = DaoUtils.chainDao.obtainChains(accountId, startIdx, start, end, limit + 1, forward); 			
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
	public Map<String, Object> receiveChains(long accountId, int startIdx, Timestamp start, Timestamp end, int limit,
			boolean forward) throws ApplicationException {
		if(limit > IChainService.MaxLimit || limit < 1)
			limit = IChainService.MaxLimit; 
		List<Chain> chains = null;
		ApplicationException ae = null;
		Map<String, Object> result = null;
		try {
			EntityManagerHelper.beginTransaction();
			chains = DaoUtils.chainDao.receiveChains(accountId, startIdx, start, end, limit + 1, forward); 			
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
	public boolean viewedChainMessages(long accountId, long chainId, Timestamp last)
			throws ApplicationException {
		boolean succeed = false;
		ApplicationException ae = null;
		try {
			EntityManagerHelper.beginTransaction();
			succeed = DaoUtils.chainMessageDao.viewedChainMessage(accountId, chainId, last);
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
