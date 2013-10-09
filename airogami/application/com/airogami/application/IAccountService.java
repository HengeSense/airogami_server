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
	 * @param args:(String[] must be not null, have password and (have email, phone, or screenName)
	 * @param type:(int) must be a valid type
	 * @return accountId if successful or null if not matched
	 * @throws ApplicationException if failed 
	 */
	public Integer signout(String[]args, int type) throws ApplicationException;
	
	/*
	 * @param properties:((Map<String, Object>) no screenName
	 * @return profile if successful
	 * @throws ApplicationException if failed 
	 */
	public Profile editProfile(Map<String, Object> properties) throws ApplicationException;
	
	/*
	 * @param accountId:(int)
	 * @param oldPassword:(String)
	 * @param newPassword:(String)
	 * @return succeed
	 * @throws ApplicationException if failed 
	 */
	public boolean changePassword(int accountId, String oldPassword, String newPassword) throws ApplicationException;
	
	/*
	 * @param accountId:(int)
	 * @param screenName:(String)
	 * @return succeed
	 * @throws ApplicationException if failed 
	 */
	public boolean changeScreenName(int accountId, String screenName) throws ApplicationException;

	/*
	 * @param accountId:(int)
	 * @param updateCount:(Integer)
	 * @return profile, null if not updated
	 * @throws ApplicationException if failed 
	 */
	public Profile obtainProfile(int accountId, Integer updateCount) throws ApplicationException;

	/*
	 * @param report:(Report)
	 * @return report if successful or null if not
	 * @throws ApplicationException or EmailExistsException if failed 
	 */
	public Report reportAccount(Report report) throws ApplicationException;
	
	
	/*
	 * @return count if successful
	 * @throws ApplicationException if failed 
	 */
	public int updateAccountStat() throws ApplicationException;
	
	/*
	 * pickup planes or chains
	 * 
	 * @param accountId:(int)
	 * 
	 * @return planes, chains, accountStatLeft if successful otherwise error, accountStatLeft
	 * 
	 * @throws ApplicationException if failed
	 */
	public Map<String, Object> pickup(int accountId) throws ApplicationException;
	
	
	
}
