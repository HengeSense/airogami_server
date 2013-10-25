package com.airogami.presentation.chain;

import java.util.List;
import java.util.Map;
import com.airogami.exception.AirogamiException;
import com.airogami.presentation.AirogamiActionSupport;
import com.airogami.presentation.logic.ManagerUtils;
import com.airogami.presentation.logic.User;
import com.airogami.presentation.utilities.JSONUtils;

public class GetChainsAction extends AirogamiActionSupport {

	private static final long serialVersionUID = 1L;	
	private List<Long> chainIds;
	private boolean updated;
	
	public String execute() throws Exception{
		boolean succeed = false;
		try {			
			User user = (User)request.getAttribute("user");
			Map<String, Object> result = ManagerUtils.chainManager.getChains(
					user.getAccountId(), chainIds, updated);
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

	public boolean isUpdated() {
		return updated;
	}

	public void setUpdated(boolean updated) {
		this.updated = updated;
	}

}
