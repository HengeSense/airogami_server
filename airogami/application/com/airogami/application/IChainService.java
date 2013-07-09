package com.airogami.application;

import java.util.List;
import java.util.Map;

import com.airogami.application.exception.ApplicationException;
import com.airogami.persitence.entities.ChainMessage;
import com.airogami.persitence.entities.Message;
import com.airogami.persitence.entities.Chain;

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
	 * @return chain, chain.message, chain.category, chain.accountByOwnerId if successful
	 * @throws ApplicationException if failed 
	 */ 
	public Chain matchChain(long chainId) throws ApplicationException;

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
	 * @param chainId:(long) must be not null
	 * @param accountId:(long)
	 * @return matchCount if successful
	 * @throws ApplicationException if failed 
	 */ 
	public int throwChain(long chainId,long accountId) throws ApplicationException;
	
	/*
	 * @param chainId:(long)
	 * @param accountId:(long)
	 * @param byOwner:(boolean)
	 * @throws ApplicationException if failed 
	 */ 
	public void deleteChain(long chainId,long accountId) throws ApplicationException;
	
	/*
	 * @param accountId:(long)
	 * @param start:(String) start datetime (exclusive)
	 * @param limit:(int) max(limit) = MaxLimit
	 * @param forward:(boolean)
	 * @return more:(boolean), chains (chain.chaiMessages) if successful
	 * @throws ApplicationException if failed 
	 */ 
	public Map<String, Object> obtainChains(long accountId, String start, int limit, boolean forward) throws ApplicationException;	
	
	/*
	 * @param accountId:(long)
	 * @param chainId:(long)	 
	 * @throws ApplicationException if failed 
	 */ 
	public List<ChainMessage> obtainChainMessages(long accountId, long chainId) throws ApplicationException;
	
	/*
	 * @param accountId:(long)
	 * @param chainId:(long)	 
	 * @param last:(String)
	 * @throws ApplicationException if failed 
	 */ 
	public void viewedChainMessage(long accountId, long chainId, String last) throws ApplicationException;
	
}
