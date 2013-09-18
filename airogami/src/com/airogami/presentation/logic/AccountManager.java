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
import com.airogami.persistence.entities.Profile;
import com.airogami.persistence.entities.Report;
import com.airogami.persistence.entities.ReportId;

public class AccountManager {

	/*
	 * @param account:(Account) must be not null and have (fullName, sex, icon,
	 * longitude, altitude, country, province, city), account.authenticate
	 * (email, password)
	 * 
	 * @return accountId if successful
	 * 
	 * @throws AirogamiException if failed
	 */
	public Account signup(Account account) throws AirogamiException {
		if (account == null) {
			throw new IllegalArgumentException("Illegal arguments in signup");
		}
		Authenticate authenticate = account.getAuthenticate();
		Profile profile = account.getProfile();
		if (profile == null || authenticate == null) {
			throw new IllegalArgumentException("Illegal arguments in signup");
		}

		if ((profile.getFullName() == null || profile.getFullName().length() == 0)
				|| profile.getSex() == null
				|| profile.getLongitude() == null
				|| profile.getLatitude() == null
				|| (profile.getCountry() == null || profile.getCountry()
						.length() == 0)
				|| (profile.getProvince() == null || profile.getProvince()
						.length() == 0)
				|| (profile.getCity() == null || profile.getCity().length() == 0)
				|| (authenticate.getEmail() == null || authenticate.getEmail()
						.length() == 0)
				|| (authenticate.getPassword() == null || authenticate
						.getPassword().length() == 0)) {
			throw new IllegalArgumentException("Illegal arguments in signup");
		}
		//
		if (profile.getSex() > AccountConstants.SexType_Female) {
			profile.setSex((short) AccountConstants.SexType_Female);
		} else if (profile.getSex() < AccountConstants.SexType_Male) {
			profile.setSex((short) AccountConstants.SexType_Male);
		}
		profile.setAccountId(null);
		profile.setStatus((short) 0);
		// no screen name
		authenticate.setScreenName(null);
		profile.setScreenName(null);
		//
		authenticate.setAccount(null);
		try {
			account = ServiceUtils.accountService.signup(account);
		} catch (ApplicationException re) {
			// re.printStackTrace();
			if (re instanceof EmailExistsException) {
				account = null;
			} else {
				throw new AirogamiException(
						AirogamiException.Application_Exception_Status,
						AirogamiException.Application_Exception_Message);
			}
		}
		return account;
	}

	/*
	 * @param args:(String[]) must have two valid elements (screenName,
	 * password)
	 * 
	 * @param automatic:(boolean) whether signed in automatically
	 * 
	 * @return account, accountStat if successful or null if not matched
	 * 
	 * @throws AirogamiException if failed
	 */
	public Account signinWithScreenName(String screenName, String password,
			boolean automatic) throws AirogamiException {
		if (screenName == null || screenName.length() == 0 || password == null
				|| password.length() == 0) {
			throw new IllegalArgumentException(
					"Illegal arguments in signinWithScreenName");
		}
		Account account = null;
		try {
			account = ServiceUtils.accountService.signin(new String[] {
					screenName, password },
					AccountConstants.AuthenticateTypeScreenName, automatic);
		} catch (Throwable re) {
			throw new AirogamiException(
					AirogamiException.Application_Exception_Status,
					AirogamiException.Application_Exception_Message);
		}
		/*
		 * if(account == null){ throw new AirogamiException(
		 * AirogamiException.Account_Signin_NotMatch_Status,
		 * AirogamiException.Account_Signin_NotMatch_Message); }
		 */
		return account;
	}

	/*
	 * @param args:(String[]) must have two valid elements (email, password)
	 * 
	 * @param automatic:(boolean) whether signed in automatically
	 * 
	 * @return account, accountStat if successful or null if not matched
	 * 
	 * @throws AirogamiException if failed
	 */
	public Account signinWithEmail(String email, String password,
			boolean automatic) throws AirogamiException {
		if (email == null || email.length() == 0 || password == null
				|| password.length() == 0) {
			throw new IllegalArgumentException(
					"Illegal arguments in signinWithEmail");
		}
		Account account = null;
		try {
			account = ServiceUtils.accountService.signin(new String[] { email,
					password }, AccountConstants.AuthenticateTypeEmail, automatic);
		} catch (Throwable re) {
			re.printStackTrace();
			throw new AirogamiException(
					AirogamiException.Application_Exception_Status,
					AirogamiException.Application_Exception_Message);
		}
		/*
		 * if(account == null){ throw new AirogamiException(
		 * AirogamiException.Account_Signin_NotMatch_Status,
		 * AirogamiException.Account_Signin_NotMatch_Message); }
		 */

		return account;
	}
	
	/*
	 * @param args:(String[]) must have two valid elements (screenName,
	 * password)
	 * 
	 * @return accountId if successful or null if not matched
	 * 
	 * @throws AirogamiException if failed
	 */
	public Long signout(String account, String password, short type) throws AirogamiException {
		if (account == null || account.length() == 0 || password == null
				|| password.length() == 0) {
			throw new IllegalArgumentException(
					"Illegal arguments in signout");
		}
		try {
			return ServiceUtils.accountService.signout(new String[] {
					account, password},
					type);
		} catch (Throwable re) {
			throw new AirogamiException(
					AirogamiException.Application_Exception_Status,
					AirogamiException.Application_Exception_Message);
		}
	}

	/*
	 * not check some data types
	 * 
	 * @param properties:((Map<String, Object>) no screenName, no status
	 * 
	 * @return profile if successful
	 * 
	 * @throws ApplicationException if failed
	 */
	public Profile editProfile(Map<String, Object> properties)
			throws AirogamiException {
		Long accountId = (Long) properties.get("accountId");
		if (accountId == null) {
			throw new IllegalArgumentException(
					"Illegal arguments in editAccount");
		}
		Profile profile = null;
		Short sex = (Short) properties.get("sex");
		if (sex != null) {
			if (sex > AccountConstants.SexType_Female) {
				properties.put("sex", AccountConstants.SexType_Female);
			} else if (sex < AccountConstants.SexType_Male) {
				properties.put("sex", AccountConstants.SexType_Male);
			}
		}
		properties.remove("screenName");
		properties.remove("status");
		try {
			profile = ServiceUtils.accountService.editProfile(properties);
		} catch (Throwable re) {
			throw new AirogamiException(
					AirogamiException.Application_Exception_Status,
					AirogamiException.Application_Exception_Message);
		}
		return profile;
	}

	/*
	 * @param accountId:(long)
	 * 
	 * @param oldPassword:(String) must not be empty
	 * 
	 * @param newPassword:(String) must not be empty
	 * 
	 * @return succeed
	 * 
	 * @throws ApplicationException if failed
	 */
	public boolean changePassword(long accountId, String oldPassword,
			String newPassword) throws AirogamiException {
		if (oldPassword == null || oldPassword.length() == 0
				|| newPassword == null || newPassword.length() == 0) {
			throw new IllegalArgumentException(
					"Illegal arguments in changePassword");
		}
		try {
			return ServiceUtils.accountService.changePassword(accountId,
					oldPassword, newPassword);
		} catch (Throwable re) {
			throw new AirogamiException(
					AirogamiException.Application_Exception_Status,
					AirogamiException.Application_Exception_Message);
		}
	}

	/*
	 * @param accountId:(long)
	 * 
	 * @param screenName:(String)
	 * 
	 * @return succeed
	 * 
	 * @throws ApplicationException if failed
	 */
	public boolean changeScreenName(long accountId, String screenName)
			throws AirogamiException {
		if (screenName == null || screenName.length() == 0) {
			throw new IllegalArgumentException(
					"Illegal arguments in changeScreenName");
		}
		try {
			return ServiceUtils.accountService.changeScreenName(accountId,
					screenName);
		} catch (Throwable re) {
			throw new AirogamiException(
					AirogamiException.Application_Exception_Status,
					AirogamiException.Application_Exception_Message);
		}
	}

	/*
	 * @param accountId:(long)
	 * 
	 * @param last:(String) must not be empty
	 * 
	 * @return profile, null if not updated
	 * 
	 * @throws AirogamiException if failed
	 */
	public Profile obtainProfile(long accountId, Long updateCount)
			throws AirogamiException {
		if (accountId < 1) {
			throw new IllegalArgumentException(
					"Illegal arguments in obtainAccount");
		}

		try {
			return ServiceUtils.accountService.obtainProfile(accountId,
					updateCount);
		} catch (Throwable re) {
			throw new AirogamiException(
					AirogamiException.Application_Exception_Status,
					AirogamiException.Application_Exception_Message);
		}
	}

	/*
	 * @param reportId:(long)
	 * 
	 * @param reportedId:(long)
	 * 
	 * @param reason:(String)
	 * 
	 * @return report if successful or null if not
	 * 
	 * @throws AirogamiException or EmailExistsException if failed
	 */
	public Report reportAccount(long reportId, long reportedId, String reason)
			throws AirogamiException {
		if (reason == null || reason.length() == 0) {
			throw new IllegalArgumentException(
					"Illegal arguments in reportAccount");
		}
		ReportId id = new ReportId(reportId, reportedId);
		Report report = new Report();
		report.setId(id);
		report.setReason(reason);
		try {
			return ServiceUtils.accountService.reportAccount(report);
		} catch (Throwable re) {
			throw new AirogamiException(
					AirogamiException.Application_Exception_Status,
					AirogamiException.Application_Exception_Message);
		}
	}

}
