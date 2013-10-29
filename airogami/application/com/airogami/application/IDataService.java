package com.airogami.application;

import com.airogami.application.exception.ApplicationException;

public interface IDataService {
	/*
	 * @param accountId:(int)
	 * @return msgDataInc if successful otherwise null
	 * @throws ApplicationException if failed 
	 */
	public Integer getMsgDataInc(int accountId) throws ApplicationException;
	
}
