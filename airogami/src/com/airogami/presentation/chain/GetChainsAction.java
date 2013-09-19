package com.airogami.presentation.chain;

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

public class GetChainsAction extends AirogamiActionSupport {

	private static final long serialVersionUID = 1L;	
	private List<Long> chainIds;
	
	public String execute() throws Exception{
		boolean succeed = false;
		try {			
			User user = (User)request.getAttribute("user");
			Map<String, Object> result = new TreeMap<String, Object>();
			result.put("chains", ManagerUtils.chainManager.getChains(
					user.getAccountId(), chainIds));
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

	public List<Long> getChainIds() {
		return chainIds;
	}

	public void setChainIds(List<Long> chainIds) {
		this.chainIds = chainIds;
	}

}
