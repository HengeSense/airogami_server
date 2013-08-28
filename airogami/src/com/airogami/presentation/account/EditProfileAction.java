package com.airogami.presentation.account;

import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpSession;

import com.airogami.exception.AirogamiError;
import com.airogami.exception.AirogamiException;
import com.airogami.presentation.AirogamiActionSupport;
import com.airogami.presentation.logic.ManagerUtils;
import com.airogami.presentation.logic.User;
import com.airogami.presentation.utilities.AirogamiUtils;
import com.airogami.presentation.utilities.JSONUtils;
import com.opensymphony.xwork2.ModelDriven;

public class EditProfileAction extends AirogamiActionSupport implements ModelDriven<EditProfileVO>{
	
	private static final long serialVersionUID = 1L;
	private EditProfileVO editProfileVO;
	
	public EditProfileAction() {
		super();
		editProfileVO = new EditProfileVO();
	}
	
    public String execute() throws Exception{
    	boolean succeed = false;
		try {			
			Map<String, Object> properties = AirogamiUtils.describe(editProfileVO);
			if(properties.size() > 0){
				HttpSession session = request.getSession(true);
				User user = (User)session.getAttribute("user");	
				properties.put("accountId", user.getAccountId());
				ManagerUtils.accountManager.editProfile(properties);			
			}
			//System.out.println("EditProfile property size: " + properties.size());
			if(request.getParameter("tokens") != null){
				HttpSession session = request.getSession(true);
				User user = (User)session.getAttribute("user");	
				Map<String, Object> result = new TreeMap<String, Object>();
				result.put("tokens", ManagerUtils.dataManager.accountIcon(user.getAccountId())); 
				dataMap.put("result", result);
			}
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
	public EditProfileVO getModel() {
		return editProfileVO;
	}

}
