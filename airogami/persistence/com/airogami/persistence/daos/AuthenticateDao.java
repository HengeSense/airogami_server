package com.airogami.persistence.daos;

import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import javax.persistence.Query;

import com.airogami.persistence.entities.Account;
import com.airogami.persistence.entities.Authenticate;
import com.airogami.persistence.entities.AuthenticateDAO;
import com.airogami.persistence.entities.EntityManagerHelper;

public class AuthenticateDao extends AuthenticateDAO {
	private final String emailAuthJPQL = "select account from Account account join fetch account.accountStat where account.accountId = (select authenticate.accountId from Authenticate authenticate where authenticate.email = ?1 and authenticate.password = ?2)";

	public Account authenticateWithEmail(String email, String password) {
		EntityManagerHelper.log("authenticate with email", Level.INFO, null);
		Account account = null;
		try {
			Query query = EntityManagerHelper.getEntityManager().createQuery(
					emailAuthJPQL);
			query.setParameter(1, email);
			query.setParameter(2, password);
			List<Account> result = query.getResultList();
			Iterator<Account> iter = result.iterator();
			if(iter.hasNext()){
				account = iter.next();
			}
			EntityManagerHelper
					.log("authenticate successful", Level.INFO, null);
		} catch (RuntimeException re) {
			EntityManagerHelper.log("authenticate failed", Level.SEVERE, re);
			throw re;
		}
		return account;
	}
	
	private final String screenNameAuthJPQL = "select account from Account account join fetch account.accountStat where account.accountId = (select authenticate.accountId from Authenticate authenticate where authenticate.screenName = ?1 and authenticate.password = ?2)";

	public Account authenticateWithScreenName(String screenName, String password) {
		EntityManagerHelper.log("authenticate with screen name", Level.INFO, null);
		Account account = null;
		try {
			Query query = EntityManagerHelper.getEntityManager().createQuery(
					screenNameAuthJPQL);
			query.setParameter(1, screenName);
			query.setParameter(2, password);
			List<Account> result = query.getResultList();
			Iterator<Account> iter = result.iterator();
			if(iter.hasNext()){
				account = iter.next();
			}
			EntityManagerHelper
					.log("authenticate successful", Level.INFO, null);
		} catch (RuntimeException re) {
			EntityManagerHelper.log("authenticate failed", Level.SEVERE, re);
			throw re;
		}
		return account;
	}
	
	private final String createAuthenticateSQL = "insert into AUTHENTICATE (EMAIL, PASSWORD) select * from (select ?, ?) as tmp where not exists (select * from AUTHENTICATE  where EMAIL = ?)";
	
	public Authenticate createAuthenticate(Authenticate authenticate) {
		EntityManagerHelper.log("createAuthenticating", Level.INFO, null);
		try {
			String email = authenticate.getEmail();
			Query query = EntityManagerHelper.getEntityManager().createNativeQuery(createAuthenticateSQL);
			query.setParameter(1, authenticate.getEmail());
			query.setParameter(2, authenticate.getPassword());
			query.setParameter(3, email);			
			authenticate = null;
			if( query.executeUpdate() == 1)
			{
				List<Authenticate> result = this.findByEmail(email);
				if(result.size() > 0){
					authenticate = result.iterator().next();
				}
			}
			EntityManagerHelper
					.log("createAuthenticate successful", Level.INFO, null);
		} catch (RuntimeException re) {
			EntityManagerHelper.log("createAuthenticate failed", Level.SEVERE, re);
			throw re;
		}
		return authenticate;
	}
}