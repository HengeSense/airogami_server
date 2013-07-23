package com.airogami.presentation.plane;

import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.PropertyUtils;

import com.airogami.exception.AirogamiError;
import com.airogami.exception.AirogamiException;
import com.airogami.persistence.entities.Category;
import com.airogami.persistence.entities.Message;
import com.airogami.persistence.entities.Plane;
import com.airogami.presentation.AirogamiActionSupport;
import com.airogami.presentation.logic.ManagerUtils;
import com.airogami.presentation.logic.User;
import com.airogami.presentation.utilities.JSONUtils;
import com.opensymphony.xwork2.ModelDriven;

public class ReplyPlaneAction extends AirogamiActionSupport implements ModelDriven<ReplyPlaneVO>{

	private static final long serialVersionUID = 1L;
	private ReplyPlaneVO replyPlaneVO;
	
	public ReplyPlaneAction(){
		replyPlaneVO = new ReplyPlaneVO();
	}	
	
	@Override
	public String execute() throws Exception {
		boolean succeed = false;
		try {			

			HttpSession session = request.getSession(true);
			User user = (User)session.getAttribute("user");	
			Message message = new Message();
			PropertyUtils.copyProperties(message, replyPlaneVO.getMessageVO());
			message = ManagerUtils.planeManager.replyPlane(replyPlaneVO.getPlaneId(), user.getAccountId(), message);
			dataMap.put("result", message);
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
		return AirogamiError.Plane_ReplyPlane_Input_Status;
	}

	@Override
	protected String getInputMessage() {
		return AirogamiError.Plane_ReplyPlane_Input_Message;
	}

	@Override
	public ReplyPlaneVO getModel() {
		return replyPlaneVO;
	}
}
