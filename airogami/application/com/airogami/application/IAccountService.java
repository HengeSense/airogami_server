package com.airogami.application;

import java.sql.Timestamp;
import java.util.Map;

import com.airogami.application.exception.ApplicationException;
import com.airogami.persistence.entities.Account;
import com.airogami.persistence.entities.Profile;
import com.airogami.persistence.entities.Report;

public interface IAccountService {
	/*
	 * @param account:(Account) must be not null and have valid account.authenticate, account.profile
	 * @return account if successful
	 * @throws ApplicationException or EmailExistsException if failed 
	 */
	public Account signup(Account account) throws ApplicationException;
	
	/*
	 * @param args:(String[] must be not null, have password and (have email, phone, or screenName)
	 * @param type:(int) must be a valid type
	 * @param automatic:(boolean) whether signed in automatically
	 * @return account, accountStat, [profile], [authenticate] if successful or null if not matched
	 * @throws ApplicationException if failed 
	 */
	public Account signin(String[]args, int type, boolean automatic) throws ApplicationException;
	
	/*
	 * @param properties:((Map<String, Object>) no screenName
	 * @return profile if successful
	 * @throws ApplicationException if failed 
	 */
	public Profile editProfile(Map<String, Object> properties) throws ApplicationException;
	
	/*
	 * @param accountId:(long)
	 * @param oldPassword:(String)
	 * @param newPassword:(String)
	 * @return succeed
	 * @throws ApplicationException if failed 
	 */
	public boolean changePassword(long accountId, String oldPassword, String newPassword) throws ApplicationException;
	
	/*
	 * @param accountId:(long)
	 * @param screenName:(String)
	 * @return succeed
	 * @throws ApplicationException if failed 
	 */
	public boolean changeScreenName(long accountId, String screenName) throws ApplicationException;

	/*
	 * @param accountId:(long)
	 * @param last:(Timestamp)
	 * @return profile, null if not updated
	 * @throws ApplicationException if failed 
	 */
	public Profile obtainProfile(long accountId, Long updateCount) throws ApplicationException;

	/*
	 * @param report:(Report)
	 * @return report if successful or null if not
	 * @throws ApplicationException or EmailExistsException if failed 
	 */
	public Report reportAccount(Report report) throws ApplicationException;
	
}
