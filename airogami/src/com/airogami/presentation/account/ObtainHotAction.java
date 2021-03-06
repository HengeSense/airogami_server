package com.airogami.presentation.account;

import java.sql.Timestamp;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpSession;

import com.airogami.exception.AirogamiError;
import com.airogami.exception.AirogamiException;
import com.airogami.persistence.entities.Account;
import com.airogami.persistence.entities.Hot;
import com.airogami.persistence.entities.Profile;
import com.airogami.presentation.AirogamiActionSupport;
import com.airogami.presentation.logic.ManagerUtils;
import com.airogami.presentation.utilities.JSONUtils;
import com.opensymphony.xwork2.ModelDriven;

public class ObtainHotAction extends AirogamiActionSupport{

	private static final long serialVersionUID = 1L;
	private Integer updateCount;
	private Integer accountId;
	
    public String execute() throws Exception{
    	boolean succeed = false;
		try {			
			//HttpSession session = request.getSession(true);
			//Account account = (Account)session.getAttribute("account");	
			Hot hot = ManagerUtils.accountManager.obtainHot(accountId, updateCount);
			Map<String, Object> result = new TreeMap<String, Object>();
		    result.put("hot", hot);
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
	
	public Integer getAccountId() {
		return accountId;
	}

	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
	}

	public Integer getUpdateCount() {
		return updateCount;
	}

	public void setUpdateCount(Integer updateCount) {
		this.updateCount = updateCount;
	}

}
