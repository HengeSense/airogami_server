package com.airogami.presentation.chain;

import javax.servlet.http.HttpSession;

import com.airogami.exception.AirogamiError;
import com.airogami.exception.AirogamiException;
import com.airogami.presentation.AirogamiActionSupport;
import com.airogami.presentation.logic.ManagerUtils;
import com.airogami.presentation.logic.User;
import com.airogami.presentation.utilities.JSONUtils;

public class DeleteChainAction extends AirogamiActionSupport {

	private static final long serialVersionUID = 1L;
	private Long chainId;
	
	public String execute() throws Exception{
		boolean succeed = false;
		try {
			HttpSession session = request.getSession(true);			
			User user = (User)session.getAttribute("user");	
			boolean result = ManagerUtils.chainManager.deleteChain(chainId, user.getAccountId());
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
	
	@Override
	protected int getInputStatus() {
		return AirogamiError.Chain_DeleteChain_Input_Status;
	}

	@Override
	protected String getInputMessage() {
		return AirogamiError.Chain_DeleteChain_Input_Message;
	}

	public Long getChainId() {
		return chainId;
	}

	public void setChainId(Long chainId) {
		this.chainId = chainId;
	}
	
}