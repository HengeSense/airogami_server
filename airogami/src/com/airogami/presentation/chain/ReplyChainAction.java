package com.airogami.presentation.chain;

import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.PropertyUtils;

import com.airogami.exception.AirogamiError;
import com.airogami.exception.AirogamiException;
import com.airogami.persistence.entities.Category;
import com.airogami.persistence.entities.ChainMessage;
import com.airogami.persistence.entities.Message;
import com.airogami.persistence.entities.Chain;
import com.airogami.presentation.AirogamiActionSupport;
import com.airogami.presentation.logic.ManagerUtils;
import com.airogami.presentation.logic.User;
import com.airogami.presentation.utilities.JSONUtils;
import com.opensymphony.xwork2.ModelDriven;

public class ReplyChainAction extends AirogamiActionSupport implements ModelDriven<ReplyChainVO>{

	private static final long serialVersionUID = 1L;
	private ReplyChainVO replyChainVO;
	
	public ReplyChainAction(){
		replyChainVO = new ReplyChainVO();
	}	
	
	@Override
	public String execute() throws Exception {
		boolean succeed = false;
		try {	
			HttpSession session = request.getSession(true);
			User user = (User)session.getAttribute("user");	
			ChainMessageVO chainMessageVO = replyChainVO.getChainMessageVO();
			Map<String, Object> result = ManagerUtils.chainManager.replyChain(
					user.getAccountId(),replyChainVO.getChainId(),
					chainMessageVO.getContent(), chainMessageVO.getType());;
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
	public ReplyChainVO getModel() {
		return replyChainVO;
	}
}
