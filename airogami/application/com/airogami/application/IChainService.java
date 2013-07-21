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
public final int MaxLimit = 50;
	
	/*
	 * @param chain:(Chain) must be not null, have chain.message
	 * @param ownerId:(long) must exist
	 * @return chain, chain.message, chain.category if successful
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
	 * @return chainMessage if successful
	 * @throws ApplicationException if failed 
	 */ 
	public ChainMessage replyChain(long accountId, long chainId, String content, int type) throws ApplicationException;
	
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
	 * @return canMatchedAgain if successful
	 * @throws ApplicationException if failed 
	 */ 
	public boolean throwChain(long chainId,long accountId) throws ApplicationException;
	
	/*
	 * @param chainId:(long)
	 * @param accountId:(long)
	 * @throws ApplicationException if failed 
	 */ 
	public void deleteChain(long chainId,long accountId) throws ApplicationException;
	
	/* 
	 * get replied chains
	 * @param accountId:(long)
	 * @param startIdx:(int) (inclusive)
	 * @param start:(Timestamp) start datetime (inclusive)
	 * @param end:(Timestamp) end datetime (inclusive)
	 * @param limit:(int) max(limit) = MaxLimit
	 * @param forward:(boolean)
	 * @return more:(boolean), chains (chain.chaiMessages) if successful
	 * @throws ApplicationException if failed 
	 */ 
	public Map<String, Object> obtainChains(long accountId, int startIdx, Timestamp start, Timestamp end, int limit, boolean forward) throws ApplicationException;	
	
	/*
	 * get newly received chains
	 * @param accountId:(long)
	 * @param startIdx:(int) (inclusive)
	 * @param start:(Timestamp) start datetime (inclusive)
	 * @param end:(Timestamp) end datetime (inclusive)
	 * @param limit:(int) max(limit) = MaxLimit
	 * @param forward:(boolean)
	 * @return more:(boolean), chains (chain.chaiMessages) if successful
	 * @throws ApplicationException if failed 
	 */ 
	public Map<String, Object> receiveChains(long accountId, int startIdx, Timestamp start, Timestamp end, int limit, boolean forward) throws ApplicationException;	
	
	
	/*
	 * @param accountId:(long)
	 * @param chainId:(long) 
	 * @return chainMessages
	 * @throws ApplicationException if failed 
	 */ 
	public List<ChainMessage> obtainChainMessages(long accountId, long chainId) throws ApplicationException;
	
	/*
	 * @param accountId:(long)
	 * @param chainId:(long)	 
	 * @param last:(Timestamp) must be not-null
	 * @return succeed
	 * @throws ApplicationException if failed 
	 */ 
	public boolean viewedChainMessages(long accountId, long chainId, Timestamp last) throws ApplicationException;
	
}
