package com.airogami.presentation.logic;

import java.sql.Timestamp;
import java.util.Map;

import com.airogami.application.ServiceUtils;
import com.airogami.application.exception.ApplicationException;
import com.airogami.application.exception.EmailExistsException;
import com.airogami.common.constants.AccountConstants;
import com.airogami.exception.AirogamiException;
import com.airogami.persistence.entities.Account;
import com.airogami.persistence.entities.Authenticate;
import com.airogami.persistence.entities.Report;
import com.airogami.persistence.entities.ReportId;

public class AccountManager {

	/*
	 * @param account:(Account) must be not null and have (fullName, sex, icon, longitude, altitude, country, province, city), account.authenticate (email, password)
	 * @return accountId if successful
	 * @throws AirogamiException if failed 
	 */
	public long signup(Account account)
			throws AirogamiException {	
		Authenticate authenticate = account.getAuthenticate();
		if (account == null || authenticate == null){
			throw new IllegalArgumentException("Illegal arguments in signup");
		}
        
		if ((account.getFullName() == null || account.getFullName().length() == 0)
				|| account.getSex() == null
			    || (account.getIcon() == null || account.getIcon().length() == 0)
				|| account.getLongitude() == null
				|| account.getLatitude() == null
				|| (account.getCountry() == null || account.getCountry().length() == 0)
				|| (account.getProvince() == null || account.getProvince().length() == 0)
				|| (account.getCity() == null || account.getCity().length() == 0)
				|| (authenticate.getEmail() == null || authenticate.getEmail().length() == 0)
				|| (authenticate.getPassword() == null || authenticate.getPassword().length() == 0)) {
			throw new IllegalArgumentException("Illegal arguments in signup");
		}
		if(account.getSex() > AccountConstants.SexType_Female){
			account.setSex((short)AccountConstants.SexType_Female);
		}
		else if(account.getSex() < AccountConstants.SexType_Male){
			account.setSex((short)AccountConstants.SexType_Male);
		}
		account.setAccountId(null);	
		account.setStatus((short)0);
		//no screen name		
		authenticate.setScreenName(null);
		account.setScreenName(null);
		//
		authenticate.setAccount(null);
		try {
			return ServiceUtils.accountService.signup(account);
		} catch (ApplicationException re) {
			if(re instanceof EmailExistsException){
				throw new AirogamiException(
						AirogamiException.Account_Signup_Duplicate_Status,
						AirogamiException.Account_Signup_Duplicate_Message);
			}
			else{
				throw new AirogamiException(
					AirogamiException.Account_Signup_Failure_Status,
					AirogamiException.Account_Signup_Failure_Message);
			}
		}
	}

	/*
	 * @param args:(String[]) must have two valid elements (screenName, password)
	 * @return account, accountStat if successful or null if not matched
	 * @throws AirogamiException if failed 
	 */
public Account signinWithScreenName(String screenName, String password)
			throws AirogamiException {
		if (screenName == null || screenName.length() == 0 || password == null || password.length() == 0){
			throw new IllegalArgumentException("Illegal arguments in signinWithScreenName");
		}
		Account account = null;
		try {
			account = ServiceUtils.accountService.signin(new String[]{screenName, password}, AccountConstants.AuthenticateTypeScreenName);
		} catch (Throwable re) {
			throw new AirogamiException(
					AirogamiException.Account_Signin_Failure_Status,
					AirogamiException.Account_Signin_Failure_Message);
		}
		/*if(account == null){
			throw new AirogamiException(
					AirogamiException.Account_Signin_NotMatch_Status,
					AirogamiException.Account_Signin_NotMatch_Message);
		}*/
		return account;
	}
	
	/*
	 * @param args:(String[]) must have two valid elements (email, password)
	 * @return account, accountStat if successful or null if not matched
	 * @throws AirogamiException if failed 
	 */
	public Account signinWithEmail(String email, String password)
			throws AirogamiException {
		if (email == null || email.length() == 0 || password == null || password.length() == 0){
			throw new IllegalArgumentException("Illegal arguments in signinWithEmail");
		}
		Account account = null;
		try {
			account = ServiceUtils.accountService.signin(new String[]{email, password}, AccountConstants.AuthenticateTypeEmail);
		} catch (Throwable re) {
			//re.printStackTrace();
			throw new AirogamiException(
					AirogamiException.Account_Signin_Failure_Status,
					AirogamiException.Account_Signin_Failure_Message);
		}
		/*if(account == null){
			throw new AirogamiException(
					AirogamiException.Account_Signin_NotMatch_Status,
					AirogamiException.Account_Signin_NotMatch_Message);
		}*/
		
		return account;
	}
	
	/*
	 * not check some data types
	 * @param properties:((Map<String, Object>) no screenName, no status
	 * @return account if successful
	 * @throws ApplicationException if failed 
	 */
	public Account editAccount(Map<String, Object> properties) throws AirogamiException{
		Long accountId = (Long)properties.get("accountId");
		if (accountId == null){
			throw new IllegalArgumentException("Illegal arguments in editAccount");
		}
		Account account = null;
		Short sex = (Short)properties.get("sex");
		if(sex != null){
			if(sex > AccountConstants.SexType_Female){
				properties.put("sex", AccountConstants.SexType_Female);
			}
			else if(sex < AccountConstants.SexType_Male){
				properties.put("sex", AccountConstants.SexType_Male);
			}
		}
		properties.remove("screenName");
		properties.remove("status");
		try {
			account = ServiceUtils.accountService.editAccount(properties);
		} catch (Throwable re) {
			throw new AirogamiException(
					AirogamiException.Account_EditAccount_Failure_Status,
					AirogamiException.Account_EditAccount_Failure_Message);
		}
		return account;
	}
	
	/*
	 * @param accountId:(long)
	 * @param oldPassword:(String) must not be empty
	 * @param newPassword:(String) must not be empty
	 * @return succeed
	 * @throws ApplicationException if failed 
	 */
	public boolean changePassword(long accountId, String oldPassword, String newPassword) throws AirogamiException{
		if (oldPassword == null || oldPassword.length() == 0
				|| newPassword == null || newPassword.length() == 0){
			throw new IllegalArgumentException("Illegal arguments in changePassword");
		}
		try {
			return ServiceUtils.accountService.changePassword(accountId, oldPassword, newPassword);
		} catch (Throwable re) {
			throw new AirogamiException(
					AirogamiException.Account_ChangePassword_Failure_Status,
					AirogamiException.Account_ChangePassword_Failure_Message);
		}
	}
	
	/*
	 * @param accountId:(long)
	 * @param screenName:(String)
	 * @return succeed
	 * @throws ApplicationException if failed 
	 */
	public boolean changeScreenName(long accountId, String screenName) throws AirogamiException{
		if (screenName == null || screenName.length() == 0){
			throw new IllegalArgumentException("Illegal arguments in changeScreenName");
		}
		try {
			return ServiceUtils.accountService.changeScreenName(accountId, screenName);
		} catch (Throwable re) {
			throw new AirogamiException(
					AirogamiException.Account_ChangeScreenName_Failure_Status,
					AirogamiException.Account_ChangeScreenName_Failure_Message);
		}
	}
	
	/*
	 * @param accountId:(long)
	 * @param last:(String) must not be empty
	 * @return account, null if not updated
	 * @throws AirogamiException if failed 
	 */
	public Account obtainAccount(long accountId, Long updateCount) throws AirogamiException{
		if (accountId < 1){
			throw new IllegalArgumentException("Illegal arguments in obtainAccount");
		}

		try {
			return ServiceUtils.accountService.obtainAccount(accountId, updateCount);
		} catch (Throwable re) {
			throw new AirogamiException(
					AirogamiException.Account_ObtainAccount_Failure_Status,
					AirogamiException.Account_ObtainAccount_Failure_Message);
		}
	}
	
	/*
	 * @param reportId:(long)
	 * @param reportedId:(long)
	 * @param reason:(String)
	 * @return report if successful or null if not
	 * @throws AirogamiException or EmailExistsException if failed 
	 */
	public Report reportAccount(long reportId, long reportedId, String reason) throws AirogamiException{
		if (reason == null || reason.length() == 0){
			throw new IllegalArgumentException("Illegal arguments in reportAccount");
		}
		ReportId id = new ReportId(reportId, reportedId);
		Report report = new Report();
		report.setId(id);
		report.setReason(reason);
		try {
			return ServiceUtils.accountService.reportAccount(report);
		} catch (Throwable re) {
			throw new AirogamiException(
					AirogamiException.Account_ReportAccount_Failure_Status,
					AirogamiException.Account_ReportAccount_Failure_Message);
		}
	}


}
