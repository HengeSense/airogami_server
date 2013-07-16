package com.airogami.presentation.logic;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import com.airogami.application.ServiceUtils;
import com.airogami.application.exception.ApplicationException;
import com.airogami.common.constants.AccountConstants;
import com.airogami.common.constants.ChainConstants;
import com.airogami.common.constants.ChainMessageConstants;
import com.airogami.common.constants.PlaneConstants;
import com.airogami.exception.AirogamiException;
import com.airogami.persitence.entities.Category;
import com.airogami.persitence.entities.Chain;
import com.airogami.persitence.entities.ChainMessage;
import com.airogami.persitence.entities.Message;

public class ChainManager {
	/*
	 * @param chain:(Chain) must be not null, have (chain.chainMessage) 
	 * @param ownerId:(long) must exist
	 * @return chain, chain.chainMessage if successful
	 * @throws AirogamiException if failed 
	 */ 
	public Chain sendChain(Chain chain, long ownerId) throws AirogamiException{
		if (chain == null || chain.getChainMessages().size() == 0){
			throw new IllegalArgumentException("Illegal arguments in sendChain");
		}
        ChainMessage chainMessage = chain.getChainMessages().get(0);
        if(chainMessage.getType() == null
        		|| chainMessage.getContent() == null || chainMessage.getContent().length() == 0){
			throw new IllegalArgumentException("Illegal arguments in sendChain");
		}
        chain.setMaxMatchCount((int)ChainConstants.MaxMatchCount);
        chain.setMatchCount(0);
        chain.setMaxPassCount((int)ChainConstants.MaxPassCount);
        chain.setPassCount(0);
        chain.setStatus((short)ChainConstants.StatusUnmatched);
        if(chain.getSex() == null){
        	chain.setSex((short)AccountConstants.SexType_Unknown);
        }
        chainMessage.setStatus((short)ChainMessageConstants.StatusReplied);
		try {
			chain = ServiceUtils.chainService.sendChain(chain, ownerId);
		} catch (ApplicationException re) {
			throw new AirogamiException(
					AirogamiException.Chain_SendChain_Failure_Status,
					AirogamiException.Chain_SendChain_Failure_Message);
		}
		return chain;
	}
	
	/*
	 * @param accountId:(long) must exist
	 * @param chainId:(long) must exist
	 * @param content:(String) must not be empty
	 * @param type:(int)
	 * @return chainMessage if successful
	 * @throws AirogamiException if failed 
	 */ 
	public ChainMessage replyChain(long accountId, long chainId, String content, int type) throws AirogamiException{
		if(content == null || content.length() == 0){
			throw new IllegalArgumentException("Illegal arguments in replyChain");
		}
		ChainMessage chainMessage = null;
		try {
			chainMessage = ServiceUtils.chainService.replyChain(accountId, chainId, content, type);
		} catch (ApplicationException re) {
			throw new AirogamiException(
					AirogamiException.Chain_ReplyChain_Failure_Status,
					AirogamiException.Chain_ReplyChain_Failure_Message);
		}
		return chainMessage;
	}
	
	/*
	 * @param chainId:(long) must be not null
	 * @param accountId:(long)
	 * @return canMatchedAgain if successful
	 * @throws AirogamiException if failed 
	 */ 
	public boolean throwChain(long chainId,long accountId) throws AirogamiException{
		boolean canMatchedAgain = false;
		try {
			canMatchedAgain = ServiceUtils.chainService.throwChain(chainId, accountId);
		} catch (ApplicationException re) {
			throw new AirogamiException(
					AirogamiException.Chain_ThrowChain_Failure_Status,
					AirogamiException.Chain_ThrowChain_Failure_Message);
		}
		return canMatchedAgain;
	}
	
	/*
	 * @param chainId:(long)
	 * @param accountId:(long)
	 * @throws AirogamiException if failed 
	 */ 
	public void deleteChain(long chainId,long accountId) throws AirogamiException{
		try {
			ServiceUtils.chainService.deleteChain(chainId, accountId);
		} catch (ApplicationException re) {
			throw new AirogamiException(
					AirogamiException.Chain_DeleteChain_Failure_Status,
					AirogamiException.Chain_DeleteChain_Failure_Message);
		}
	}
	
	/*
	 * @param accountId:(long)
	 * @param startIdx:(int) (inclusive)
	 * @param start:(String) start datetime (inclusive)
	 * @param end:(String) end datetime (inclusive)
	 * @param limit:(int) max(limit) = MaxLimit
	 * @param forward:(boolean)
	 * @return more:(boolean), chains (chain.chaiMessages) if successful
	 * @throws AirogamiException if failed 
	 */ 
	public Map<String, Object> obtainChains(long accountId, int startIdx, String start, String end, int limit, boolean forward) throws AirogamiException{
		Timestamp sTimestamp = null;
		Timestamp eTimestamp = null;
		if(start != null && start.length() > 0){
			//may throw IllegalArgumentException
			sTimestamp = Timestamp.valueOf(start);			
		}
		if(end != null && end.length() > 0){
			//may throw IllegalArgumentException
			eTimestamp = Timestamp.valueOf(end);			
		}
		if(startIdx < 0){
			startIdx = 0;
		}
		Map<String, Object> result;
		try {
			result = ServiceUtils.chainService.obtainChains(accountId, startIdx, sTimestamp, eTimestamp, limit, forward);
		} catch (ApplicationException re) {
			throw new AirogamiException(
					AirogamiException.Chain_ObtainChains_Failure_Status,
					AirogamiException.Chain_ObtainChains_Failure_Message);
		}
		return result;
	}
	
	/*
	 * @param accountId:(long)
	 * @param startIdx:(int) (inclusive)
	 * @param start:(String) start datetime (inclusive)
	 * @param end:(String) end datetime (inclusive)
	 * @param limit:(int) max(limit) = MaxLimit
	 * @param forward:(boolean)
	 * @return more:(boolean), chains (chain.chaiMessages) if successful
	 * @throws AirogamiException if failed 
	 */ 
	public Map<String, Object> receiveChains(long accountId, int startIdx, String start, String end, int limit, boolean forward) throws AirogamiException{
		Timestamp sTimestamp = null;
		Timestamp eTimestamp = null;
		if(start != null && start.length() > 0){
			//may throw IllegalArgumentException
			sTimestamp = Timestamp.valueOf(start);			
		}
		if(end != null && end.length() > 0){
			//may throw IllegalArgumentException
			eTimestamp = Timestamp.valueOf(end);			
		}
		if(startIdx < 0){
			startIdx = 0;
		}
		Map<String, Object> result;
		try {
			result = ServiceUtils.chainService.receiveChains(accountId, startIdx, sTimestamp, eTimestamp, limit, forward);
		} catch (ApplicationException re) {
			throw new AirogamiException(
					AirogamiException.Chain_ReceiveChains_Failure_Status,
					AirogamiException.Chain_ReceiveChains_Failure_Message);
		}
		return result;
	}
	
	/*
	 * @param accountId:(long)
	 * @param chainId:(long) 
	 * @return chainMessages (asc)
	 * @throws AirogamiException if failed 
	 */ 
	public List<ChainMessage> obtainChainMessages(long accountId, long chainId) throws AirogamiException{
		try {
			    return ServiceUtils.chainService.obtainChainMessages(accountId, chainId);
			} catch (ApplicationException re) {
			throw new AirogamiException(
					AirogamiException.Chain_ObtainChainMessages_Failure_Status,
					AirogamiException.Chain_ObtainChainMessages_Failure_Message);
		}
		
	}
	
	/*
	 * @param accountId:(long)
	 * @param chainId:(long)	 
	 * @param last:(String) must not be empty
	 * @return succeed
	 * @throws AirogamiException if failed 
	 */ 
	public boolean viewedChainMessages(long accountId, long chainId, String last) throws AirogamiException{
		if(last == null || last.length() == 0){
			throw new IllegalArgumentException("Illegal arguments in viewedChainMessages");
		}
		Timestamp lastTimestamp = Timestamp.valueOf(last);
		long time = System.currentTimeMillis();
		if(lastTimestamp.getTime() > time){
			lastTimestamp.setTime(time);
		}
		try {
		    return ServiceUtils.chainService.viewedChainMessages(accountId, chainId, lastTimestamp);
		} catch (ApplicationException re) {
		throw new AirogamiException(
				AirogamiException.Chain_ViewedChainMessages_Failure_Status,
				AirogamiException.Chain_ViewedChainMessages_Failure_Message);
	}
	}
	

}
