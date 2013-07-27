package com.airogami.presentation.chain;

import java.sql.Timestamp;
import java.util.Map;

import javax.servlet.http.HttpSession;

import com.airogami.exception.AirogamiError;
import com.airogami.exception.AirogamiException;
import com.airogami.presentation.AirogamiActionSupport;
import com.airogami.presentation.logic.ManagerUtils;
import com.airogami.presentation.logic.User;
import com.airogami.presentation.utilities.JSONUtils;

public class ObtainChainsAction extends AirogamiActionSupport {

	private static final long serialVersionUID = 1L;	
	private int startIdx;
	private String start;
	private String end;
	private int limit;
	private boolean forward;
	private Timestamp sTimestamp;
	private Timestamp eTimestamp;
	
	public void validate(){	
		super.validate();
		if(this.hasActionErrors() == false && this.hasFieldErrors() == false){
			try{
				if(start != null && start.length() > 0)
				    sTimestamp = Timestamp.valueOf(start);
			}catch(Throwable t){
				this.addFieldError("start", "Invalid Timestamp");
				JSONUtils.putStatus(dataMap,  getInputStatus(), getInputMessage());
				dataMap.put("fieldErrors", this.getFieldErrors());
			}	
			try{
				if(end != null && end.length() > 0)
				eTimestamp = Timestamp.valueOf(end);
			}catch(Throwable t){
				this.addFieldError("end", "Invalid Timestamp");
				JSONUtils.putStatus(dataMap,  getInputStatus(), getInputMessage());
				dataMap.put("fieldErrors", this.getFieldErrors());
			}
		}		
	}

	public String receiveChains() throws Exception{
		chains(1);
    	return SUCCESS;
	}
	
	public String obtainChains() throws Exception{ 
		chains(2);
    	return SUCCESS;
	}
	
	private void chains(int type){
		boolean succeed = false;
		try {
			HttpSession session = request.getSession(true);			
			User user = (User)session.getAttribute("user");
			Map<String, Object> result;
			if(type == 1){
			    result = ManagerUtils.chainManager.receiveChains(
					user.getAccountId(), startIdx, sTimestamp, eTimestamp, limit, forward);
			}
			else{
				result = ManagerUtils.chainManager.obtainChains(
						user.getAccountId(), startIdx, sTimestamp, eTimestamp, limit, forward);
			}
            dataMap.put("result", result);
			succeed = true;
		} catch (AirogamiException e) {
			String localizedMessage = getText(e.getMessage(),e.getMessage());
			JSONUtils.putStatus(dataMap, e.getStatus(), localizedMessage);
		}
		catch(Throwable t){
		    //t.printStackTrace(System.out);
			JSONUtils.putStatus(dataMap, "Unexpected exception");
		}
		//success
		if(succeed){
			JSONUtils.putOKStatus(dataMap);
		}
	}
	
	@Override
	protected int getInputStatus() {
		if("receiveChains".equals(method)){
			return AirogamiError.Chain_ReceiveChains_Input_Status;
		}
		else{
			return AirogamiError.Chain_ObtainChains_Input_Status;
		}
	}

	@Override
	protected String getInputMessage() {
		if("receiveChains".equals(method)){
			return AirogamiError.Chain_ReceiveChains_Input_Message;
		}
		else{
			return AirogamiError.Chain_ObtainChains_Input_Message;		
     	}
	}

	public int getStartIdx() {
		return startIdx;
	}

	public void setStartIdx(int startIdx) {
		this.startIdx = startIdx;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public boolean isForward() {
		return forward;
	}

	public void setForward(boolean forward) {
		this.forward = forward;
	}

}
