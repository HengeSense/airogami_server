package com.airogami.presentation.plane;

import java.util.Map;
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
	public void validate(){	
		super.validate();
		if(this.hasActionErrors() == false && this.hasFieldErrors() == false){
			try{
				String link = replyPlaneVO.getMessageVO().getLink();
				if(link!=null){
					Integer.parseInt(link);
				}
			}catch(Throwable t){
				this.addFieldError("messageVO.link", "Invalid messageVO.link");
				JSONUtils.putInputStatus(dataMap);
				dataMap.put("fieldErrors", this.getFieldErrors());
			}	
		}		
	}
	
	@Override
	public String execute() throws Exception {
		boolean succeed = false;
		try {			
			User user = (User)request.getAttribute("user");	
			Message message = new Message();
			PropertyUtils.copyProperties(message, replyPlaneVO.getMessageVO());
			Map<String, Object> result = ManagerUtils.planeManager.replyPlane(replyPlaneVO.getPlaneId(), user.getAccountId(), replyPlaneVO.getByOwner(), message);
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
