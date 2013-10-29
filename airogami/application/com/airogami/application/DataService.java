package com.airogami.application;

import java.util.Map;
import java.util.TreeMap;

import com.airogami.application.exception.ApplicationException;
import com.airogami.persistence.daos.DaoUtils;
import com.airogami.persistence.entities.EntityManagerHelper;

public class DataService implements IDataService {

	@Override
	public Integer getMsgDataInc(int accountId) throws ApplicationException {
		ApplicationException ae = null;
		Integer msgDataInc = null;
		try {
			EntityManagerHelper.beginTransaction();
			msgDataInc = DaoUtils.accountSysDao.getMsgDataInc(accountId);
			EntityManagerHelper.commit();
		} catch (Throwable t) {
			
			if (t.getCause() == null) {
				ae = new ApplicationException();
			} else {
				ae = new ApplicationException(t.getCause().getMessage());
			}
		} finally {
			EntityManagerHelper.closeEntityManager();
		}
		if (ae != null) {
			throw ae;
		}
		return msgDataInc;
	}

}
