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

public class ObtainMessagesAction extends AirogamiActionSupport{

	private static final long serialVersionUID = 1L;
	private Long planeId;
	private Long startId;
	private int limit;	
	
	@Override
	public String execute() throws Exception {
		boolean succeed = false;
		try {
			HttpSession session = request.getSession(true);
			User user = (User)session.getAttribute("user");	
			Map<String, Object> result = ManagerUtils.planeManager.obtainMessages(
					user.getAccountId(), planeId, startId, limit);
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

	@Override
	protected int getInputStatus() {		
		return AirogamiError.Plane_ObtainMessages_Input_Status;
	}

	@Override
	protected String getInputMessage() {
		return AirogamiError.Plane_ObtainMessages_Input_Message;
	}

	public Long getPlaneId() {
		return planeId;
	}

	public void setPlaneId(Long planeId) {
		this.planeId = planeId;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public Long getStartId() {
		return startId;
	}

	public void setStartId(Long startId) {
		this.startId = startId;
	}

}
