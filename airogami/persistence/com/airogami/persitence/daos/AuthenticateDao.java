package com.airogami.persitence.daos;

import java.util.logging.Level;

import javax.persistence.Query;

import com.airogami.persitence.entities.Account;
import com.airogami.persitence.entities.AuthenticateDAO;
import com.airogami.persitence.entities.EntityManagerHelper;

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
			account = (Account) query.getSingleResult();
			EntityManagerHelper
					.log("authenticate successful", Level.INFO, null);
		} catch (RuntimeException re) {
			EntityManagerHelper.log("authenticate failed", Level.SEVERE, re);
			throw re;
		}
		return account;
	}
}
