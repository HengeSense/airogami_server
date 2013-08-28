package com.airogami.presentation.plane;

import java.sql.Timestamp;
import java.util.Map;

import javax.servlet.http.HttpSession;

import com.airogami.ObjectUtils;
import com.airogami.exception.AirogamiError;
import com.airogami.exception.AirogamiException;
import com.airogami.presentation.AirogamiActionSupport;
import com.airogami.presentation.logic.ManagerUtils;
import com.airogami.presentation.logic.User;
import com.airogami.presentation.utilities.JSONUtils;

public class ObtainPlanesAction extends AirogamiActionSupport {

	private static final long serialVersionUID = 1L;	
	private Long start;
	private Long end;
	private int limit;
	private boolean forward = true;

	public String receivePlanes() throws Exception{
		planes(1);
    	return SUCCESS;
	}
	
	public String obtainPlanes() throws Exception{ 
		planes(2);
    	return SUCCESS;
	}
	
	private void planes(int type){
		boolean succeed = false;
		try {
			HttpSession session = request.getSession(true);			
			User user = (User)session.getAttribute("user");
			Map<String, Object> result;
			if(type == 1){
			    result = ManagerUtils.planeManager.receivePlanes(
					user.getAccountId(), start, end, limit, forward);
			}
			else{
				result = ManagerUtils.planeManager.obtainPlanes(
						user.getAccountId(), start, end, limit, forward);
			}
            dataMap.put("result", result);
			succeed = true;
		} catch (AirogamiException e) {
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

	public Long getStart() {
		return start;
	}

	public void setStart(Long start) {
		this.start = start;
	}

	public Long getEnd() {
		return end;
	}

	public void setEnd(Long end) {
		this.end = end;
	}

}
