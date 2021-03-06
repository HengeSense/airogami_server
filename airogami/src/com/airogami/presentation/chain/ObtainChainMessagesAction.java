package com.airogami.presentation.chain;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import com.airogami.exception.AirogamiError;
import com.airogami.exception.AirogamiException;
import com.airogami.persistence.entities.ChainMessage;
import com.airogami.presentation.AirogamiActionSupport;
import com.airogami.presentation.logic.ManagerUtils;
import com.airogami.presentation.logic.User;
import com.airogami.presentation.utilities.JSONUtils;

public class ObtainChainMessagesAction extends AirogamiActionSupport{

	private static final long serialVersionUID = 1L;
	private Long chainId;
	private String last;
	private int limit;	
	private Timestamp timestamp;
	
	public void validate(){	
		super.validate();
		if(this.hasActionErrors() == false && this.hasFieldErrors() == false){
			try{
				if(last != null && last.length() > 0)
				    timestamp = Timestamp.valueOf(last);
			}catch(Throwable t){
				this.addFieldError("last", "Invalid Timestamp");
				JSONUtils.putInputStatus(dataMap);
				dataMap.put("fieldErrors", this.getFieldErrors());
			}	
		}		
	}
	
	@Override
	public String execute() throws Exception {
		boolean succeed = false;
		try {
			User user = (User)request.getAttribute("user");	
			Map<String, Object> result = ManagerUtils.chainManager.obtainChainMessages(
					user.getAccountId(), chainId, timestamp, limit);
			dataMap.put("result", result);
			succeed = true;
		} catch (AirogamiException e) {
			//e.printStackTrace();
			String localizedMessage = getText(e.getMessage(),e.getMessage());
			JSONUtils.putStatus(dataMap, e.getStatus(), localizedMessage);
		}
		catch(Throwable t){
			t.printStackTrace(System.out);
			JSONUtils.putStatus(dataMap, "Unexpected exception");
		}
		//success
		if(succeed){
			JSONUtils.putOKStatus(dataMap);
		}
		return SUCCESS;
	}

	public Long getChainId() {
		return chainId;
	}

	public void setChainId(Long chainId) {
		this.chainId = chainId;
	}

	public String getLast() {
		return last;
	}

	public void setLast(String start) {
		this.last = start;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

}
