package com.airogami.presentation.account;

import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpSession;

import com.airogami.exception.AirogamiError;
import com.airogami.exception.AirogamiException;
import com.airogami.persistence.entities.Account;
import com.airogami.presentation.AirogamiActionSupport;
import com.airogami.presentation.logic.ManagerUtils;
import com.airogami.presentation.logic.User;
import com.airogami.presentation.utilities.JSONUtils;
import com.opensymphony.xwork2.ModelDriven;

public class ChangeScreenNameAction extends AirogamiActionSupport implements ModelDriven<ChangeScreenNameVO>{

	private static final long serialVersionUID = 1L;
    private ChangeScreenNameVO changeScreenNameVO;
	
	public ChangeScreenNameAction() {
		super();
		changeScreenNameVO = new ChangeScreenNameVO();
	}
	
    public String execute() throws Exception{
    	boolean succeed = false;
		try {			
			User user = (User)request.getAttribute("user");
			boolean succeeded = ManagerUtils.accountManager.changeScreenName(
					user.getAccountId(), changeScreenNameVO.getScreenName());
			Map<String, Object> result = new TreeMap<String, Object>();
		    result.put("succeed", succeeded);
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
	public ChangeScreenNameVO getModel() {
		return changeScreenNameVO;
	}

}
