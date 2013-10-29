package com.airogami.presentation.data;

import com.airogami.exception.AirogamiException;
import com.airogami.presentation.AirogamiActionSupport;
import com.airogami.presentation.logic.ManagerUtils;
import com.airogami.presentation.logic.User;
import com.airogami.presentation.utilities.JSONUtils;

public class ChainDataTokenAction extends AirogamiActionSupport {

	private static final long serialVersionUID = 1L;	
	private long chainId;
	private short type = -1;
	
	public String execute() throws Exception{
		boolean succeed = false;
		try {			
			User user = (User)request.getAttribute("user");
			Object result = ManagerUtils.dataManager.chainData(user.getAccountId(), chainId, type);
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

	public short getType() {
		return type;
	}

	public void setType(short type) {
		this.type = type;
	}

	public long getChainId() {
		return chainId;
	}

	public void setChainId(long chainId) {
		this.chainId = chainId;
	}

}
