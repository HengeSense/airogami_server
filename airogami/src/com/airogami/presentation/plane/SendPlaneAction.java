package com.airogami.presentation.plane;

import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.PropertyUtils;

import com.airogami.exception.AirogamiError;
import com.airogami.exception.AirogamiException;
import com.airogami.persistence.entities.Account;
import com.airogami.persistence.entities.Category;
import com.airogami.persistence.entities.Message;
import com.airogami.persistence.entities.Plane;
import com.airogami.presentation.AirogamiActionSupport;
import com.airogami.presentation.logic.ManagerUtils;
import com.airogami.presentation.logic.User;
import com.airogami.presentation.utilities.JSONUtils;
import com.opensymphony.xwork2.ModelDriven;

public class SendPlaneAction extends AirogamiActionSupport implements ModelDriven<SendPlaneVO>{

	private static final long serialVersionUID = 1L;
	private SendPlaneVO sendPlaneVO;

	public SendPlaneAction(){
		sendPlaneVO = new SendPlaneVO();
	}
	
	@Override
	public String execute() throws Exception {
		boolean succeed = false;
		try {			
			Plane plane = new Plane();
			PropertyUtils.copyProperties(plane, sendPlaneVO);
			Message message = new Message();
			PropertyUtils.copyProperties(message, sendPlaneVO.getMessageVO());
			Category category = new Category();
			PropertyUtils.copyProperties(category, sendPlaneVO.getCategoryVO());
			plane.setCategory(category);
			plane.getMessages().add(message);
			User user = (User)request.getAttribute("user");	
			Map<String, Object> result = ManagerUtils.planeManager.sendPlane(plane, user.getAccountId());
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
		return SUCCESS;
	}

	@Override
	public SendPlaneVO getModel() {
		return sendPlaneVO;
	}

}
