package com.airogami.presentation.chain;

import java.sql.Timestamp;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpSession;

import com.airogami.exception.AirogamiError;
import com.airogami.exception.AirogamiException;
import com.airogami.presentation.AirogamiActionSupport;
import com.airogami.presentation.logic.ManagerUtils;
import com.airogami.presentation.logic.User;
import com.airogami.presentation.utilities.JSONUtils;

public class ViewedChainMessagesAction extends AirogamiActionSupport{

	private static final long serialVersionUID = 1L;
	private Long chainId;
	private String last;	
	private Timestamp timestamp;
	
	public void validate(){	
		super.validate();
		if(this.hasActionErrors() == false && this.hasFieldErrors() == false){
			try{
				timestamp = Timestamp.valueOf(last);
				long time = System.currentTimeMillis();
				if(timestamp.getTime() > time){
					timestamp.setTime(time);
				}
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
			HttpSession session = request.getSession(false);
			User user = (User)session.getAttribute("user");	
			boolean succeeded = ManagerUtils.chainManager.viewedChainMessages(user.getAccountId(), chainId, timestamp);
			Map<String, Object> result = new TreeMap<String, Object>(); 
			result.put("succeed", succeeded);
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

	public void setLast(String last) {
		this.last = last;
	}

}
