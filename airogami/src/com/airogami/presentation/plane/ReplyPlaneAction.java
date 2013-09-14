package com.airogami.presentation.plane;

import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.PropertyUtils;

import com.airogami.exception.AirogamiException;
import com.airogami.persistence.entities.Message;
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

			HttpSession session = request.getSession(false);
			User user = (User)session.getAttribute("user");	
			Message message = new Message();
			PropertyUtils.copyProperties(message, replyPlaneVO.getMessageVO());
			Map<String, Object> result = ManagerUtils.planeManager.replyPlane(replyPlaneVO.getPlaneId(), user.getAccountId(), message);
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
	public ReplyPlaneVO getModel() {
		return replyPlaneVO;
	}
}
