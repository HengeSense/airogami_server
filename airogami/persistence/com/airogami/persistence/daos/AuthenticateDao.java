package com.airogami.persistence.daos;

import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import javax.persistence.Query;
import javax.persistence.TypedQuery;

import com.airogami.persistence.entities.Account;
import com.airogami.persistence.entities.Authenticate;
import com.airogami.persistence.entities.AuthenticateDAO;
import com.airogami.persistence.entities.EntityManagerHelper;

public class AuthenticateDao extends AuthenticateDAO {
	
	private final String emailAutoAuthJPQL = "select account from Account account join fetch account.accountStat join fetch account.agent where account.accountId = (select authenticate.accountId from Authenticate authenticate where authenticate.email = ?1 and authenticate.password = ?2)";

	private final String emailAuthJPQL = "select account from Account account join fetch account.accountStat join fetch account.agent join fetch account.hot join fetch account.profile join fetch account.authenticate where account.accountId = (select authenticate.accountId from Authenticate authenticate where authenticate.email = ?1 and authenticate.password = ?2)";
	
	//fetch objects are different between automatic and nonautomatic
	public Account authenticateWithEmail(String email, String password, boolean automatic) {
		EntityManagerHelper.log("authenticating with email", Level.INFO, null);
		Account account = null;
		try {
			String jpql;
			if(automatic){
				jpql = emailAutoAuthJPQL;
			}
			else{
				jpql = emailAuthJPQL;
			}
			TypedQuery<Account> query = EntityManagerHelper.getEntityManager().createQuery(
					jpql, Account.class);
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
	
	private final String signoutEmailAuthJPQL = "select authenticate.accountId from Authenticate authenticate where authenticate.email = ?1 and authenticate.password = ?2";

	public Integer signoutWithEmail(String email, String password) {
		EntityManagerHelper.log("signoutWithEmailing", Level.INFO, null);
		Integer accountId = null;
		try {
			TypedQuery<Integer> query = EntityManagerHelper.getEntityManager().createQuery(
					signoutEmailAuthJPQL, Integer.class);
			query.setParameter(1, email);
			query.setParameter(2, password);
			List<Integer> result = query.getResultList();
			Iterator<Integer> iter = result.iterator();
			if(iter.hasNext()){
				accountId = iter.next();
			}
			EntityManagerHelper
					.log("signoutWithEmail successful", Level.INFO, null);
		} catch (RuntimeException re) {
			EntityManagerHelper.log("signoutWithEmail failed", Level.SEVERE, re);
			throw re;
		}
		return accountId;
	}
	
	private final String screenNameAutoAuthJPQL = "select account from Account account join fetch account.accountStat join fetch account.agent where account.accountId = (select authenticate.accountId from Authenticate authenticate where authenticate.screenName = ?1 and authenticate.password = ?2)";

	private final String screenNameAuthJPQL = "select account from Account account join fetch account.accountStat join fetch account.agent join fetch account.hot join fetch account.profile  join fetch account.authenticate  where account.accountId = (select authenticate.accountId from Authenticate authenticate where authenticate.screenName = ?1 and authenticate.password = ?2)";

	//fetch objects are different between automatic and nonautomatic
	public Account authenticateWithScreenName(String screenName, String password, boolean automatic) {
		EntityManagerHelper.log("authenticating with screen name", Level.INFO, null);
		Account account = null;
		try {
			String jpql;
			if(automatic){
				jpql = screenNameAutoAuthJPQL;
			}
			else{
				jpql = screenNameAuthJPQL;
			}
			TypedQuery<Account> query = EntityManagerHelper.getEntityManager().createQuery(
					jpql, Account.class);
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
	
	private final String signoutScreenNameAuthJPQL = "select authenticate.accountId from Authenticate authenticate where authenticate.screenName = ?1 and authenticate.password = ?2";

	public Integer signoutWithScreenName(String screeName, String password) {
		EntityManagerHelper.log("signoutWithScreenNaming", Level.INFO, null);
		Integer accountId = null;
		try {
			TypedQuery<Integer> query = EntityManagerHelper.getEntityManager().createQuery(
					signoutScreenNameAuthJPQL, Integer.class);
			query.setParameter(1, screeName);
			query.setParameter(2, password);
			List<Integer> result = query.getResultList();
			Iterator<Integer> iter = result.iterator();
			if(iter.hasNext()){
				accountId = iter.next();
			}
			EntityManagerHelper
					.log("signoutWithScreenName successful", Level.INFO, null);
		} catch (RuntimeException re) {
			EntityManagerHelper.log("signoutWithScreenName failed", Level.SEVERE, re);
			throw re;
		}
		return accountId;
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
