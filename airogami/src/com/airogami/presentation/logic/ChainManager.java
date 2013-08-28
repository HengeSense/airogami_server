package com.airogami.presentation.logic;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.airogami.application.ServiceUtils;
import com.airogami.application.exception.ApplicationException;
import com.airogami.common.constants.AccountConstants;
import com.airogami.common.constants.ChainConstants;
import com.airogami.common.constants.ChainMessageConstants;
import com.airogami.common.constants.PlaneConstants;
import com.airogami.exception.AirogamiException;
import com.airogami.persistence.entities.Category;
import com.airogami.persistence.entities.Chain;
import com.airogami.persistence.entities.ChainMessage;
import com.airogami.persistence.entities.Message;

public class ChainManager {
	/*
	 * @param chain:(Chain) must be not null, have (chain.chainMessage) 
	 * @param ownerId:(long) must exist
	 * @return chain, chain.chainMessage if successful
	 * @throws AirogamiException if failed 
	 */ 
	public Map<String, Object> sendChain(Chain chain, long ownerId) throws AirogamiException{
		if (chain == null || chain.getChainMessages().size() == 0){
			throw new IllegalArgumentException("Illegal arguments in sendChain");
		}
        ChainMessage chainMessage = chain.getChainMessages().get(0);
        if(chainMessage.getType() == null
        		|| chainMessage.getContent() == null || chainMessage.getContent().length() == 0){
			throw new IllegalArgumentException("Illegal arguments in sendChain");
		}
        chain.setMaxMatchCount(ChainConstants.MaxMatchCount);
        chain.setMatchCount((short)0);
        chain.setMaxPassCount(ChainConstants.MaxPassCount);
        chain.setPassCount((short)0);
        chain.setStatus((short)ChainConstants.StatusUnmatched);
        if(chain.getSex() == null){
        	chain.setSex((short)AccountConstants.SexType_Unknown);
        }
        chainMessage.setStatus((short)ChainMessageConstants.StatusReplied);
		try {
			chain = ServiceUtils.chainService.sendChain(chain, ownerId);
		} catch (ApplicationException re) {
			throw new AirogamiException(
					AirogamiException.Application_Exception_Status,
					AirogamiException.Application_Exception_Message);
		}
		Map<String, Object> result = new TreeMap<String, Object>();
		if(chain != null){
			ServiceUtils.airogamiService.appendChain(chain.getChainId());
			result.put("chain", chain);
		}
		else{
			result.put("error", "none");
		}
		
		return result;
	}
	
	/*
	 * @param accountId:(long) must exist
	 * @param chainId:(long) must exist
	 * @param content:(String) must not be empty
	 * @param type:(int)
	 * @return chainMessage if successful otherwise error or chainMessage
	 * @throws AirogamiException if failed 
	 */ 
	public Map<String, Object> replyChain(long accountId, long chainId, String content, int type) throws AirogamiException{
		if(content == null || content.length() == 0){
			throw new IllegalArgumentException("Illegal arguments in replyChain");
		}
		ChainMessage chainMessage = null;
		Map<String, Object> result;
		try {
			result = ServiceUtils.chainService.replyChain(accountId, chainId, content, type);
		} catch (ApplicationException re) {
			throw new AirogamiException(
					AirogamiException.Application_Exception_Status,
					AirogamiException.Application_Exception_Message);
		}
		if(result.get("error") == null){
			ServiceUtils.airogamiService.appendChain(chainId);
		}
		
		return result;
	}
	
	/*
	 * @param chainId:(long) must be not null
	 * @param accountId:(long)
	 * @return canMatchedAgain if successful
	 * @throws AirogamiException if failed 
	 */ 
	public Map<String, Object> throwChain(long chainId,long accountId) throws AirogamiException{
		Map<String, Object> result;
		try {
			result = ServiceUtils.chainService.throwChain(chainId, accountId);
		} catch (ApplicationException re) {
			throw new AirogamiException(
					AirogamiException.Application_Exception_Status,
					AirogamiException.Application_Exception_Message);
		}
		Boolean canMatchedAgain = (Boolean)result.get("canMatchedAgain");
		if(canMatchedAgain != null && canMatchedAgain){
			ServiceUtils.airogamiService.appendChain(chainId);
		}
		return result;
	}
	
	/*
	 * @param chainId:(long)
	 * @param accountId:(long)
	 * @throws AirogamiException if failed 
	 */ 
	public Map<String, Object> deleteChain(long chainId,long accountId) throws AirogamiException{
		try {
			return ServiceUtils.chainService.deleteChain(chainId, accountId);
		} catch (ApplicationException re) {
			throw new AirogamiException(
					AirogamiException.Application_Exception_Status,
					AirogamiException.Application_Exception_Message);
		}
	}
	
	/*
	 * @param accountId:(long)
	 * @param start:(Long) (exclusive)
	 * @param end:(Long) (exclusive)
	 * @param limit:(int) max(limit) = MaxLimit
	 * @param forward:(boolean)
	 * @return more:(boolean), chains (chain.chaiMessages) if successful
	 * @throws AirogamiException if failed 
	 */ 
	public Map<String, Object> obtainChains(long accountId, Long start, Long end, int limit, boolean forward) throws AirogamiException{
		Map<String, Object> result;
		try {
			result = ServiceUtils.chainService.obtainChains(accountId, start, end, limit, forward);
		} catch (ApplicationException re) {
			throw new AirogamiException(
					AirogamiException.Application_Exception_Status,
					AirogamiException.Application_Exception_Message);
		}
		return result;
	}
	
	/*
	 * @param accountId:(long)
	 * @param start:(Long) (exclusive)
	 * @param end:(Long) (exclusive)
	 * @param limit:(int) max(limit) = MaxLimit
	 * @param forward:(boolean)
	 * @return more:(boolean), chainIds if successful
	 * @throws AirogamiException if failed 
	 */ 
	public Map<String, Object> obtainChainIds(long accountId, Long start, Long end, int limit, boolean forward) throws AirogamiException{
		Map<String, Object> result;
		try {
			result = ServiceUtils.chainService.obtainChainIds(accountId, start, end, limit, forward);
		} catch (ApplicationException re) {
			throw new AirogamiException(
					AirogamiException.Application_Exception_Status,
					AirogamiException.Application_Exception_Message);
		}
		return result;
	}
	
	/* 
	 * obtain replied chainIds for synchronization
	 * @param accountId:(long)
	 * @param startIdx:(long) start chainId (exclusive)
	 * @param limit:(int) max(limit) = MaxChainIdLimit
	 * @return more:(boolean), chainIds if successful
	 * @throws AirogamiException if failed 
	 */ 
	public Map<String, Object> obtainChainIds(long accountId, long startId, int limit) throws AirogamiException{
		Map<String, Object> result;
		try {
			result = ServiceUtils.chainService.obtainChainIds(accountId, startId, limit);
		} catch (ApplicationException re) {
			throw new AirogamiException(
					AirogamiException.Application_Exception_Status,
					AirogamiException.Application_Exception_Message);
		}
		return result;
	}
	
	
	/*
	 * @param accountId:(long)
	 * @param start:(Long) (exclusive)
	 * @param end:(Long) (exclusive)
	 * @param limit:(int) max(limit) = MaxLimit
	 * @param forward:(boolean)
	 * @return more:(boolean), chains (chain.chaiMessages) if successful
	 * @throws AirogamiException if failed 
	 */ 
	public Map<String, Object> receiveChains(long accountId, Long start, Long end, int limit, boolean forward) throws AirogamiException{
		Map<String, Object> result;
		try {
			result = ServiceUtils.chainService.receiveChains(accountId, start, end, limit, forward);
		} catch (ApplicationException re) {
			throw new AirogamiException(
					AirogamiException.Application_Exception_Status,
					AirogamiException.Application_Exception_Message);
		}
		return result;
	}
	
	/*
	 * @param accountId:(long)
	 * @param start:(Long) (exclusive)
	 * @param end:(Long) (exclusive)
	 * @param limit:(int) max(limit) = MaxLimit
	 * @param forward:(boolean)
	 * @return more:(boolean), chainIds if successful
	 * @throws AirogamiException if failed 
	 */ 
	public Map<String, Object> receiveChainIds(long accountId, Long start, Long end, int limit, boolean forward) throws AirogamiException{
		Map<String, Object> result;
		try {
			result = ServiceUtils.chainService.receiveChainIds(accountId, start, end, limit, forward);
		} catch (ApplicationException re) {
			throw new AirogamiException(
					AirogamiException.Application_Exception_Status,
					AirogamiException.Application_Exception_Message);
		}
		return result;
	}
	
	/* 
	 * get newly received chainIds for synchronization
	 * @param accountId:(long)
	 * @param startIdx:(long) start chainId (exclusive)
	 * @param limit:(int) max(limit) = MaxChainIdLimit
	 * @return more:(boolean), chainIds if successful
	 * @throws AirogamiException if failed 
	 */ 
	public Map<String, Object> receiveChainIds(long accountId, long startId, int limit) throws AirogamiException{
		Map<String, Object> result;
		try {
			result = ServiceUtils.chainService.receiveChainIds(accountId, startId, limit);
		} catch (ApplicationException re) {
			throw new AirogamiException(
					AirogamiException.Application_Exception_Status,
					AirogamiException.Application_Exception_Message);
		}
		return result;
	}
	
	/*
	 * @param accountId:(long)
	 * @param chainId:(long) 
	 * @param last:(Timestamp) null for (updatedTime > lastViewedTime)
	 * @param limit:(int) max(limit) = MaxChainMessageLimit
	 * @return chainMessages (asc), more
	 * @throws AirogamiException if failed 
	 */ 
	public Map<String, Object> obtainChainMessages(long accountId, long chainId, Timestamp last, int limit) throws AirogamiException{
		try {
			    return ServiceUtils.chainService.obtainChainMessages(accountId, chainId, last, limit);
			} catch (ApplicationException re) {
			throw new AirogamiException(
					AirogamiException.Application_Exception_Status,
					AirogamiException.Application_Exception_Message);
		}
		
	}
	
	/*
	 * @param accountId:(long)
	 * @param chainId:(long)	 
	 * @param last:(Timestamp) must not be empty
	 * @return succeed
	 * @throws AirogamiException if failed 
	 */ 
	public boolean viewedChainMessages(long accountId, long chainId, Timestamp last) throws AirogamiException{
		if(last == null){
			throw new IllegalArgumentException("Illegal arguments in viewedChainMessages");
		}
		try {
		    return ServiceUtils.chainService.viewedChainMessages(accountId, chainId, last);
		} catch (ApplicationException re) {
		throw new AirogamiException(
				AirogamiException.Application_Exception_Status,
				AirogamiException.Application_Exception_Message);
	}
	}
	

}
