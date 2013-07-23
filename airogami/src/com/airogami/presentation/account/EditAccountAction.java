package com.airogami.presentation.account;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;

import com.airogami.ObjectUtils;
import com.airogami.exception.AirogamiError;
import com.airogami.exception.AirogamiException;
import com.airogami.persistence.entities.Account;
import com.airogami.presentation.AirogamiActionSupport;
import com.airogami.presentation.logic.ManagerUtils;
import com.airogami.presentation.logic.User;
import com.airogami.presentation.utilities.AirogamiUtils;
import com.airogami.presentation.utilities.JSONUtils;
import com.opensymphony.xwork2.ModelDriven;

public class EditAccountAction extends AirogamiActionSupport implements ModelDriven<EditAccountVO>{
	
	private static final long serialVersionUID = 1L;
	private EditAccountVO editAccountVO;
	
	public EditAccountAction() {
		super();
		editAccountVO = new EditAccountVO();
	}
	
    public String execute() throws Exception{
    	boolean succeed = false;
		try {			
			Map<String, Object> properties = AirogamiUtils.describe(editAccountVO);
			if(properties.size() > 0){
				HttpSession session = request.getSession(true);
				User user = (User)session.getAttribute("user");	
				properties.put("accountId", user.getAccountId());
				//temporary
				session.setAttribute("account", ManagerUtils.accountManager.editAccount(properties));				
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
   	protected int getInputStatus() {
   		return AirogamiError.Account_EditAccount_Input_Status;
   	}

   	@Override
   	protected String getInputMessage() {
   		return AirogamiError.Account_EditAccount_Input_Message;
   	}
	
	@Override
	public EditAccountVO getModel() {
		return editAccountVO;
	}

}
