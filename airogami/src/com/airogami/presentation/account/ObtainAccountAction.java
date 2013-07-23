package com.airogami.presentation.account;

import java.sql.Timestamp;

import javax.servlet.http.HttpSession;

import com.airogami.exception.AirogamiError;
import com.airogami.exception.AirogamiException;
import com.airogami.persistence.entities.Account;
import com.airogami.presentation.AirogamiActionSupport;
import com.airogami.presentation.logic.ManagerUtils;
import com.airogami.presentation.utilities.JSONUtils;
import com.opensymphony.xwork2.ModelDriven;

public class ObtainAccountAction extends AirogamiActionSupport{

	private static final long serialVersionUID = 1L;
	private String last;
	private Long accountId;
	private Timestamp timestamp;
	
	public void validate(){	
		super.validate();
		if(this.hasActionErrors() == false && this.hasFieldErrors() == false){
			try{
			    timestamp = Timestamp.valueOf(last);
			}catch(Throwable t){
				this.addFieldError("last", "Invalid Timestamp");
				JSONUtils.putStatus(dataMap,  getInputStatus(), getInputMessage());
				dataMap.put("fieldErrors", this.getFieldErrors());
			}			
		}		
	}
	
    public String execute() throws Exception{
    	boolean succeed = false;
		try {			
			//HttpSession session = request.getSession(true);
			//Account account = (Account)session.getAttribute("account");	
			Account result = ManagerUtils.accountManager.obtainAccount(accountId, timestamp);
			dataMap.put("account", result);
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
   		return AirogamiError.Account_ObtainAccount_Input_Status;
   	}

   	@Override
   	protected String getInputMessage() {
   		return AirogamiError.Account_ObtainAccount_Input_Message;
   	}
	
	public String getLast() {
		return last;
	}

	public void setLast(String last) {
		this.last = last;
	}

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

}
