package com.airogami.presentation.chain;

import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.PropertyUtils;

import com.airogami.exception.AirogamiError;
import com.airogami.exception.AirogamiException;
import com.airogami.persistence.entities.ChainMessage;
import com.airogami.persistence.entities.Chain;
import com.airogami.presentation.AirogamiActionSupport;
import com.airogami.presentation.logic.ManagerUtils;
import com.airogami.presentation.logic.User;
import com.airogami.presentation.utilities.JSONUtils;
import com.opensymphony.xwork2.ModelDriven;

public class SendChainAction extends AirogamiActionSupport implements ModelDriven<SendChainVO>{

	private static final long serialVersionUID = 1L;
	private SendChainVO sendChainVO;

	public SendChainAction(){
		sendChainVO = new SendChainVO();
	}
	
	@Override
	public String execute() throws Exception {
		boolean succeed = false;
		try {			
			Chain chain = new Chain();
			PropertyUtils.copyProperties(chain, sendChainVO);
			ChainMessage chainMessage = new ChainMessage();
			PropertyUtils.copyProperties(chainMessage, sendChainVO.getChainMessageVO());
			chain.getChainMessages().add(chainMessage);
			User user = (User)request.getAttribute("user");	
			Map<String, Object> result = ManagerUtils.chainManager.sendChain(chain, user.getAccountId());
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
	public SendChainVO getModel() {
		return sendChainVO;
	}

}
