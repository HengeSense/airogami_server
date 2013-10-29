package com.airogami.presentation.data;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.airogami.exception.AirogamiException;
import com.airogami.presentation.AirogamiActionSupport;
import com.airogami.presentation.logic.ManagerUtils;
import com.airogami.presentation.logic.User;
import com.airogami.presentation.utilities.JSONUtils;

public class AccountIconTokensAction extends AirogamiActionSupport {

	private static final long serialVersionUID = 1L;	
	
	public String execute() throws Exception{
		boolean succeed = false;
		try {			
			User user = (User)request.getAttribute("user");
			String tokens = ManagerUtils.dataManager.accountIcon(user.getAccountId());
			Map<String, Object> result = new TreeMap<String, Object>();
			result.put("tokens", tokens);
            dataMap.put("result", result);
			succeed = true;
		}
		catch(Throwable t){
			JSONUtils.putStatus(dataMap, "Unexpected exception");
		}
		//success
		if(succeed){
			JSONUtils.putOKStatus(dataMap);
		}
		return SUCCESS;
	}

}
