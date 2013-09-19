package com.airogami.presentation.plane;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpSession;

import com.airogami.exception.AirogamiError;
import com.airogami.exception.AirogamiException;
import com.airogami.presentation.AirogamiActionSupport;
import com.airogami.presentation.logic.ManagerUtils;
import com.airogami.presentation.logic.User;
import com.airogami.presentation.utilities.JSONUtils;

public class GetPlanesAction extends AirogamiActionSupport {

	private static final long serialVersionUID = 1L;	
	private List<Long> planeIds;
	
	public String execute() throws Exception{
		boolean succeed = false;
		try {			
			User user = (User)request.getAttribute("user");
			Map<String, Object> result = new TreeMap<String, Object>();
			result.put("planes", ManagerUtils.planeManager.getPlanes(
					user.getAccountId(), planeIds));
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
		return SUCCESS;
	}

	public List<Long> getPlaneIds() {
		return planeIds;
	}

	public void setPlaneIds(List<Long> chainIds) {
		this.planeIds = chainIds;
	}

}
