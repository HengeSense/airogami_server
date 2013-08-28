package com.airogami.presentation.plane;

import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpSession;

import com.airogami.exception.AirogamiError;
import com.airogami.exception.AirogamiException;
import com.airogami.presentation.AirogamiActionSupport;
import com.airogami.presentation.logic.ManagerUtils;
import com.airogami.presentation.logic.User;
import com.airogami.presentation.utilities.JSONUtils;

public class PickupThrowPlaneAction extends AirogamiActionSupport {

	private static final long serialVersionUID = 1L;
	private Long planeId;
	
	public String pickup() throws Exception{
		plane(1);
		return SUCCESS;
	}
	
	public String throwPlane() throws Exception{
		plane(2);
		return SUCCESS;
	}
	
	public void plane(int type){
		boolean succeed = false;
		try {
			HttpSession session = request.getSession(true);			
			User user = (User)session.getAttribute("user");	
			Map<String, Object> result;
			if(type == 1){
				result = ManagerUtils.planeManager.pickup(user.getAccountId());
			}
			else{
				result = ManagerUtils.planeManager.throwPlane(planeId, user.getAccountId());
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
	
	public Long getPlaneId() {
		return planeId;
	}

	public void setPlaneId(Long planeId) {
		this.planeId = planeId;
	}

}
