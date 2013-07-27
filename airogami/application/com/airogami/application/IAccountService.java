package com.airogami.application;

import java.sql.Timestamp;
import java.util.Map;

import com.airogami.application.exception.ApplicationException;
import com.airogami.persistence.entities.Account;
import com.airogami.persistence.entities.Report;

public interface IAccountService {
	/*
	 * @param account:(Account) must be not null and have valid account.authenticate
	 * @return accountId if successful
	 * @throws ApplicationException or EmailExistsException if failed 
	 */
	public long signup(Account account) throws ApplicationException;
	
	/*
	 * @param args:(String[] must be not null, have password and (have email, phone, or screenName)
	 * @param type:(int) must be a valid type
	 * @return account, accountStat if successful or null if not matched
	 * @throws ApplicationException if failed 
	 */
	public Account signin(String[]args, int type) throws ApplicationException;
	
	/*
	 * @param properties:((Map<String, Object>) no screenName
	 * @return account if successful
	 * @throws ApplicationException if failed 
	 */
	public Account editAccount(Map<String, Object> properties) throws ApplicationException;
	
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
	 * @return account, null if not updated
	 * @throws ApplicationException if failed 
	 */
	public Account obtainAccount(long accountId, Long updateCount) throws ApplicationException;

	/*
	 * @param report:(Report)
	 * @return report if successful or null if not
	 * @throws ApplicationException or EmailExistsException if failed 
	 */
	public Report reportAccount(Report report) throws ApplicationException;
	
}
