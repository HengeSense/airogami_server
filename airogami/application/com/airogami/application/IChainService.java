package com.airogami.application;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import com.airogami.application.exception.ApplicationException;
import com.airogami.persistence.entities.Chain;
import com.airogami.persistence.entities.ChainMessage;
import com.airogami.persistence.entities.Message;
import com.airogami.persistence.entities.Plane;

public interface IChainService {
public final int MaxChainLimit = 50;
public final int MaxChainMessageLimit = 20;
public final int MaxOldChainsLimit = 1000;
	
	/*
	 * @param chain:(Chain) must be not null, have chain.message
	 * @param ownerId:(long) must exist
	 * @return chain, chain.message, chain.category if successful otherwise null if ownerId not exist
	 * @throws ApplicationException if failed 
	 */ 
	public Chain sendChain(Chain chain, long ownerId) throws ApplicationException;
	
	/*
	 * @param chainId:(long) must exist
	 * @return chain, (matched) accountId if successful and chain = null if (not exist or already matched or exceed maxMatchCount)
	 * @throws ApplicationException if failed 
	 */ 
	public Map<String, Object> matchChain(long chainId) throws ApplicationException;

	/*
	 * @param accountId:(long) must exist
	 * @param chainId:(long) must exist
	 * @param content:(String) must not be empty
	 * @param type:(int)
	 * @return chainMessage if successful otherwise error or chainMessage
	 * @throws ApplicationException if failed 
	 */ 
	public Map<String, Object> replyChain(long accountId, long chainId, String content, int type) throws ApplicationException;
	
	/*
	 * @param accountId:(long)
	 * @param count:(int) should > 0
	 * @return chains, chains.chainMessages, chains.chainMessages.account if successful
	 * @throws ApplicationException if failed 
	 */
	public List<Chain> pickupChain(long accountId, int count) throws ApplicationException;
	
	/*
	 * @param chainId:(long) must be not null
	 * @param accountId:(long)
	 * @return canMatchedAgain if successful otherwise error or chainMessage
	 * @throws ApplicationException if failed 
	 */ 
	public Map<String, Object> throwChain(long chainId,long accountId) throws ApplicationException;
	
	/*
	 * @param chainId:(long)
	 * @param accountId:(long)
	 * @return succeed if successful otherwise error or chainMessage
	 * @throws ApplicationException if failed 
	 */ 
	public Map<String, Object> deleteChain(long chainId,long accountId) throws ApplicationException;
	
	/* 
	 * obtain replied chains
	 * @param accountId:(long)
	 * @param start:(Long) (exclusive)
	 * @param end:(Long) (exclusive)
	 * @param limit:(int) max(limit) = MaxChainLimit
	 * @param forward:(boolean)
	 * @return more:(boolean), chains (chain.chaiMessages) if successful
	 * @throws ApplicationException if failed 
	 */ 
	public Map<String, Object> obtainChains(long accountId, Long start, Long end, int limit, boolean forward) throws ApplicationException;	

	/* 
	 * obtain replied and received chains
	 * @param accountId:(long)
	 * @param start:(Long) (exclusive)
	 * @param end:(Long) (exclusive)
	 * @param limit:(int) max(limit) = MaxChainLimit
	 * @param forward:(boolean)
	 * @return more:(boolean), newChains if successful
	 * @throws ApplicationException if failed 
	 */ 
	public Map<String, Object> getNewChains(long accountId, Long start, Long end, int limit, boolean forward) throws ApplicationException;	

	/* 
	 * get all undeleted chainIds
	 * @param accountId:(long)
	 * @param start:(Long) (exclusive)
	 * @param end:(Long) (exclusive)
	 * @param limit:(int) max(limit) = MaxChainIdLimit
	 * @param forward:(boolean)
	 * @return more:(boolean), oldChains if successful
	 * @throws ApplicationException if failed 
	 */ 
	public Map<String, Object> getOldChains(long accountId, Long start, Long end, int limit, boolean forward) throws ApplicationException;	

	/* 
	 * @param accountId:(long)
	 * @param chainIds:(List<Long>))
	 * @return chains if successful
	 * @throws ApplicationException if failed 
	 */ 
	public List<Chain> getChains(long accountId, List<Long> chainIds) throws ApplicationException;	

	
	/* 
	 * obtain replied chains
	 * @param accountId:(long)
	 * @param start:(Long) (exclusive)
	 * @param end:(Long) (exclusive)
	 * @param limit:(int) max(limit) = MaxChainLimit
	 * @param forward:(boolean)
	 * @return more:(boolean), chainIds if successful
	 * @throws ApplicationException if failed 
	 */ 
	public Map<String, Object> obtainChainIds(long accountId, Long start, Long end, int limit, boolean forward) throws ApplicationException;	
	
	
	/* 
	 * obtain replied chainIds for synchronization
	 * @param accountId:(long)
	 * @param startIdx:(long) start chainId (exclusive)
	 * @param limit:(int) max(limit) = MaxChainIdLimit
	 * @return more:(boolean), chainIds if successful
	 * @throws ApplicationException if failed 
	 */ 
	public Map<String, Object> obtainChainIds(long accountId, long startId, int limit) throws ApplicationException;	
	
	
	/*
	 * get newly received chains
	 * @param accountId:(long)
	 * @param start:(Long) (exclusive)
	 * @param end:(Long) (exclusive)
	 * @param limit:(int) max(limit) = MaxChainLimit
	 * @param forward:(boolean)
	 * @return more:(boolean), chains (chain.chaiMessages, chain.account) if successful
	 * @throws ApplicationException if failed 
	 */ 
	public Map<String, Object> receiveChains(long accountId, Long start, Long end, int limit, boolean forward) throws ApplicationException;	
	
	/*
	 * get newly received chains
	 * @param accountId:(long)
	 * @param start:(Long) (exclusive)
	 * @param end:(Long) (exclusive)
	 * @param limit:(int) max(limit) = MaxChainLimit
	 * @param forward:(boolean)
	 * @return more:(boolean), chainIds if successful
	 * @throws ApplicationException if failed 
	 */ 
	public Map<String, Object> receiveChainIds(long accountId, Long start, Long end, int limit, boolean forward) throws ApplicationException;	
	
	/* 
	 * get newly received chainIds for synchronization
	 * @param accountId:(long)
	 * @param startIdx:(long) start chainId (exclusive)
	 * @param limit:(int) max(limit) = MaxChainIdLimit
	 * @return more:(boolean), chainIds if successful
	 * @throws ApplicationException if failed 
	 */ 
	public Map<String, Object> receiveChainIds(long accountId, long startId, int limit) throws ApplicationException;	

	
	/*
	 * @param accountId:(long)
	 * @param chainId:(long) 
	 * @param last:(Timestamp) null for (updateTime > lastViewedTime)
	 * @param limit:(int) max(limit) = MaxChainMessageLimit
	 * @return chainMessages, more
	 * @throws ApplicationException if failed 
	 */ 
	public Map<String, Object> obtainChainMessages(long accountId, long chainId, Timestamp last, int limit) throws ApplicationException;
	
	/*
	 * @param accountId:(long)
	 * @param chainId:(long)	 
	 * @param last:(Timestamp) must be not-null
	 * @return succeed
	 * @throws ApplicationException if failed 
	 */ 
	public boolean viewedChainMessages(long accountId, long chainId, Timestamp last) throws ApplicationException;
	
}
