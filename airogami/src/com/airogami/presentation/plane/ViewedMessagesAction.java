package com.airogami.presentation.plane;

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

public class ViewedMessagesAction extends AirogamiActionSupport{

	private static final long serialVersionUID = 1L;
	private Long planeId;
	private Long lastMsgId;	
	private Boolean byOwner;
	
	@Override
	public String execute() throws Exception {
		boolean succeed = false;
		try {
			HttpSession session = request.getSession(false);
			User user = (User)session.getAttribute("user");	
			boolean succeeded = ManagerUtils.planeManager.viewedMessages(user.getAccountId(), planeId, lastMsgId, byOwner);
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

	public Long getPlaneId() {
		return planeId;
	}

	public void setPlaneId(Long planeId) {
		this.planeId = planeId;
	}

	public Boolean getByOwner() {
		return byOwner;
	}

	public void setByOwner(Boolean byOwner) {
		this.byOwner = byOwner;
	}

	public Long getLastMsgId() {
		return lastMsgId;
	}

	public void setLastMsgId(Long lastMsgId) {
		this.lastMsgId = lastMsgId;
	}

}
