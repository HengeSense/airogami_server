package com.airogami.presentation.plane;

import java.sql.Timestamp;
import java.util.Map;

import javax.servlet.http.HttpSession;

import com.airogami.exception.AirogamiError;
import com.airogami.exception.AirogamiException;
import com.airogami.presentation.AirogamiActionSupport;
import com.airogami.presentation.logic.ManagerUtils;
import com.airogami.presentation.logic.User;
import com.airogami.presentation.utilities.JSONUtils;

public class ObtainAllPlaneIdsAction extends AirogamiActionSupport {

	private static final long serialVersionUID = 1L;	
	private long startId;
	private int limit;

	public String receivePlaneIds() throws Exception{
		planeIds(1);
    	return SUCCESS;
	}
	
	public String obtainPlaneIds() throws Exception{ 
		planeIds(2);
    	return SUCCESS;
	}
	
	private void planeIds(int type){
		boolean succeed = false;
		try {
			HttpSession session = request.getSession(false);			
			User user = (User)session.getAttribute("user");
			Map<String, Object> result;
			if(type == 1){
			    result = ManagerUtils.planeManager.receivePlaneIds(user.getAccountId(), startId, limit);
			}
			else{
				result = ManagerUtils.planeManager.obtainPlaneIds(user.getAccountId(), startId, limit);
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

	public long getStartId() {
		return startId;
	}

	public void setStartId(long startId) {
		this.startId = startId;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

}
