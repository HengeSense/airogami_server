package com.airogami.presentation.account;

import java.sql.Timestamp;

import javax.servlet.http.HttpSession;

import com.airogami.exception.AirogamiError;
import com.airogami.exception.AirogamiException;
import com.airogami.persistence.entities.Account;
import com.airogami.persistence.entities.Report;
import com.airogami.presentation.AirogamiActionSupport;
import com.airogami.presentation.logic.ManagerUtils;
import com.airogami.presentation.logic.User;
import com.airogami.presentation.utilities.JSONUtils;
import com.opensymphony.xwork2.ModelDriven;

public class ReportAccountAction extends AirogamiActionSupport{

	private static final long serialVersionUID = 1L;
	private Long reportedId;
	private String reason;
	
    public String execute() throws Exception{
    	boolean succeed = false;
		try {			
			HttpSession session = request.getSession(true);
			User user = (User)session.getAttribute("user");	
			Report report = ManagerUtils.accountManager.reportAccount(user.getAccountId(), reportedId, reason);
			dataMap.put("report", report);
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
   		return AirogamiError.Account_ReportAccount_Input_Status;
   	}

   	@Override
   	protected String getInputMessage() {
   		return AirogamiError.Account_ReportAccount_Input_Message;
   	}

	public Long getReportedId() {
		return reportedId;
	}

	public void setReportedId(Long accountId) {
		this.reportedId = accountId;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

}
