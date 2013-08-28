package com.airogami.persistence.daos;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;

import javax.persistence.Query;

import com.airogami.persistence.entities.Account;
import com.airogami.persistence.entities.AccountDAO;
import com.airogami.persistence.entities.EntityManagerHelper;
import com.airogami.persistence.entities.Profile;

public class AccountDao extends AccountDAO {

	private Random random = new Random(System.currentTimeMillis());
	/*private final String[] randPlaneAccountJPQLs = {
			"select account.accountId from Account account where account.accountId >= (select ?1 * (1.0 + max(account.accountId) - min(account.accountId)) + min(account.accountId) - 1 from Account account where account.status = 0 and account <> ?2 and (0 = ?3 or account.sex = ?3)) and account.status = 0 and account <> ?2 and (0 = ?3 or account.sex = ?3)",
			"select account.accountId from Account account where account.accountId >= (select ?1 * (1.0 + max(account.accountId) - min(account.accountId)) + min(account.accountId) - 1 from Account account where account.status = 0 and account <> ?2 and (0 = ?4 or account.sex = ?4) and account.country = ?3) and account.status = 0 and account <> ?2 and (0 = ?4 or account.sex = ?4) and account.country = ?3",
			"select account.accountId from Account account where account.accountId >= (select ?1 * (1.0 + max(account.accountId) - min(account.accountId)) + min(account.accountId) - 1 from Account account where account.status = 0 and account <> ?2 and (0 = ?5 or account.sex = ?5) and account.country = ?3 and account.province = ?4) and account.status = 0 and account <> ?2 and (0 = ?5 or account.sex = ?5) and account.country = ?3 and account.province = ?4",
			"select account.accountId from Account account where account.accountId >= (select ?1 * (1.0 + max(account.accountId) - min(account.accountId)) + min(account.accountId) - 1 from Account account where account.status = 0 and account <> ?2 and (0 = ?6 or account.sex = ?6) and account.country = ?3 and account.province = ?4 and account.city = ?5) and account.status = 0 and account <> ?2 and (0 = ?6 or account.sex = ?6) and account.country = ?3 and account.province = ?4 and account.city = ?5" };
     */
   private final String randPlaneAccountJPQL = "select profile.accountId from Profile profile, Plane plane where profile.accountId >= (select :rand * (1.0 + max(profile.accountId) - min(profile.accountId)) + min(profile.accountId) - 1 from Profile profile, Plane plane where plane.planeId = :planeId and profile.status = 0 and profile.accountId <> plane.accountByOwnerId.accountId and (0 = plane.sex or profile.sex = plane.sex) and (plane.country is null or plane.country = '' or profile.country = plane.country) and (plane.province is null or plane.province = '' or profile.province = plane.province) and (plane.city is null or plane.city = '' or profile.city = plane.city) and (plane.birthdayLower is null or plane.birthdayLower <= profile.birthday) and (plane.birthdayUpper is null or plane.birthdayUpper >= profile.birthday) and (plane.language is null or plane.language = '' or profile.language = plane.language) and profile.accountId not in (select planeHist.id.accountId from PlaneHist planeHist where planeHist.id.planeId = :planeId) ) and plane.planeId = :planeId and profile.status = 0 and profile.accountId <> plane.accountByOwnerId.accountId and (0 = plane.sex or profile.sex = plane.sex) and (plane.country is null or plane.country = '' or profile.country = plane.country) and (plane.province is null or plane.province = '' or profile.province = plane.province) and (plane.city is null or plane.city = '' or profile.city = plane.city)  and (plane.birthdayLower is null or plane.birthdayLower <= profile.birthday) and (plane.birthdayUpper is null or plane.birthdayUpper >= profile.birthday) and (plane.language is null or plane.language = '' or profile.language = plane.language) and profile.accountId not in (select planeHist.id.accountId from PlaneHist planeHist where planeHist.id.planeId = :planeId)";

	// not equal (current) account
	public Long randPlaneAccount(long planeId) {
		EntityManagerHelper.log("randPlaneAccounting", Level.INFO, null);
		try {
			Query query = EntityManagerHelper.getEntityManager().createQuery(
					randPlaneAccountJPQL);
			query.setParameter("rand", random.nextDouble());//
			query.setParameter("planeId", planeId);
			query.setMaxResults(1);
			Long result = null;
			Iterator<Long> iter = query.getResultList().iterator();
			if (iter.hasNext()) {
				result = iter.next();
			}
			EntityManagerHelper.log("randPlaneAccount successful", Level.INFO,
					null);
			return result;
		} catch (RuntimeException re) {
			EntityManagerHelper
					.log("randPlaneAccount failed", Level.SEVERE, re);
			throw re;
		}
	}

	// consider whether the account is blocked
	/*private final String[] randChainAccountJPQLs = {
			"select account.accountId from Account account, Chain chain where chain.chainId = ?1 and (chain.sex = 0 or chain.sex = account.sex) and account.accountId >= (select ?2 * (1.0 + max(account.accountId) - min(account.accountId)) + min(account.accountId) - 1 from Account account, Chain chain where account.status = 0 and chain.chainId = ?1 and (chain.sex = 0 or chain.sex = account.sex) and  not exists (select chainMessage from ChainMessage chainMessage where chainMessage.chain = chain and chainMessage.account = account)) and account.status = 0 and  not exists (select chainMessage from ChainMessage chainMessage where chainMessage.chain = chain and chainMessage.account = account)",
			"select account.accountId from Account account, Chain chain where chain.chainId = ?1 and (chain.sex = 0 or chain.sex = account.sex) and account.accountId >= (select ?2 * (1.0 + max(account.accountId) - min(account.accountId)) + min(account.accountId) - 1 from Account account, Chain chain where account.status = 0 and chain.chainId = ?1 and (chain.sex = 0 or chain.sex = account.sex) and  not exists (select chainMessage from ChainMessage chainMessage where chainMessage.chain = chain and chainMessage.account = account) and account.country = ?3) and account.status = 0 and  not exists (select chainMessage from ChainMessage chainMessage where chainMessage.chain = chain and chainMessage.account = account) and account.country = ?3",
			"select account.accountId from Account account, Chain chain where chain.chainId = ?1 and (chain.sex = 0 or chain.sex = account.sex) and account.accountId >= (select ?2 * (1.0 + max(account.accountId) - min(account.accountId)) + min(account.accountId) - 1 from Account account, Chain chain where account.status = 0 and chain.chainId = ?1 and (chain.sex = 0 or chain.sex = account.sex) and  not exists (select chainMessage from ChainMessage chainMessage where chainMessage.chain = chain and chainMessage.account = account) and account.country = ?3 and account.province = ?4) and account.status = 0 and  not exists (select chainMessage from ChainMessage chainMessage where chainMessage.chain = chain and chainMessage.account = account) and account.country = ?3  and account.province = ?4",
			"select account.accountId from Account account, Chain chain where chain.chainId = ?1 and (chain.sex = 0 or chain.sex = account.sex) and account.accountId >= (select ?2 * (1.0 + max(account.accountId) - min(account.accountId)) + min(account.accountId) - 1 from Account account, Chain chain where account.status = 0 and chain.chainId = ?1 and (chain.sex = 0 or chain.sex = account.sex) and  not exists (select chainMessage from ChainMessage chainMessage where chainMessage.chain = chain and chainMessage.account = account) and account.country = ?3 and account.province = ?4 and account.city = ?5) and account.status = 0 and  not exists (select chainMessage from ChainMessage chainMessage where chainMessage.chain = chain and chainMessage.account = account) and account.country = ?3 and account.province = ?4 and account.city = ?5" };
     */
	//private final String randChainAccountJPQL = "select profile.accountId from Profile profile, Chain chain where chain.chainId = ?1 and (chain.sex = 0 or chain.sex = profile.sex) and profile.accountId >= (select ?2 * (1.0 + max(profile.accountId) - min(profile.accountId)) + min(profile.accountId) - 1 from Profile profile, Chain chain where profile.status = 0 and chain.chainId = ?1 and (chain.sex = 0 or chain.sex = profile.sex) and not exists (select chainMessage from ChainMessage chainMessage where chainMessage.chain = chain and chainMessage.account.accountId = profile.accountId) and (chain.country is null or chain.country = '' or profile.country = chain.country) and (chain.province is null or chain.province = '' or profile.province = chain.province) and (chain.city is null or chain.city = '' or profile.city = chain.city) and (chain.birthdayLower is null or chain.birthdayLower <= profile.birthday) and (chain.birthdayUpper is null or chain.birthdayUpper >= profile.birthday) and (chain.language is null or chain.language = '' or profile.language = chain.language) ) and profile.status = 0 and not exists (select chainMessage from ChainMessage chainMessage where chainMessage.chain = chain and chainMessage.account.accountId = profile.accountId) and (chain.country is null or chain.country = '' or profile.country = chain.country) and (chain.province is null or chain.province = '' or profile.province = chain.province) and (chain.city is null or chain.city = '' or profile.city = chain.city) and (chain.birthdayLower is null or chain.birthdayLower <= profile.birthday) and (chain.birthdayUpper is null or chain.birthdayUpper >= profile.birthday) and (chain.language is null or chain.language = '' or profile.language = chain.language)";
	
	private final String randChainAccountJPQL = "select profile.accountId from Profile profile, Chain chain where chain.chainId = ?1 and (chain.sex = 0 or chain.sex = profile.sex) and profile.accountId >= (select ?2 * (1.0 + max(profile.accountId) - min(profile.accountId)) + min(profile.accountId) - 1 from Profile profile, Chain chain where profile.status = 0 and chain.chainId = ?1 and (chain.sex = 0 or chain.sex = profile.sex) and profile.accountId not in (select chainMessage.id.accountId from ChainMessage chainMessage where chainMessage.id.chainId = ?1) and profile.accountId not in (select chainHist.id.accountId from ChainHist chainHist where chainHist.id.chainId = ?1) and (chain.country is null or chain.country = '' or profile.country = chain.country) and (chain.province is null or chain.province = '' or profile.province = chain.province) and (chain.city is null or chain.city = '' or profile.city = chain.city) and (chain.birthdayLower is null or chain.birthdayLower <= profile.birthday) and (chain.birthdayUpper is null or chain.birthdayUpper >= profile.birthday) and (chain.language is null or chain.language = '' or profile.language = chain.language) ) and profile.status = 0 and profile.accountId not in (select chainMessage.id.accountId from ChainMessage chainMessage where chainMessage.id.chainId = ?1) and profile.accountId not in (select chainHist.id.accountId from ChainHist chainHist where chainHist.id.chainId = ?1) and (chain.country is null or chain.country = '' or profile.country = chain.country) and (chain.province is null or chain.province = '' or profile.province = chain.province) and (chain.city is null or chain.city = '' or profile.city = chain.city) and (chain.birthdayLower is null or chain.birthdayLower <= profile.birthday) and (chain.birthdayUpper is null or chain.birthdayUpper >= profile.birthday) and (chain.language is null or chain.language = '' or profile.language = chain.language)";
	
	public Long randChainAccount(long chainId) {
		EntityManagerHelper.log("randChainAccounting", Level.INFO, null);
		try {
			Query query = EntityManagerHelper.getEntityManager().createQuery(
					randChainAccountJPQL);
			query.setParameter(1, chainId);
			query.setParameter(2, random.nextDouble());
			query.setMaxResults(1);
			Long result = null;
			Iterator<Long> iter = query.getResultList().iterator();
			if (iter.hasNext()) {
				result = iter.next();
			}
			EntityManagerHelper.log("randChainAccount successful", Level.INFO,
					null);
			return result;
		} catch (RuntimeException re) {
			EntityManagerHelper
					.log("randChainAccount failed", Level.SEVERE, re);
			throw re;
		}
	}

	private final String changePasswordJPQL = "update Authenticate authenticate set authenticate.password = ?3 where authenticate.accountId = ?1 and authenticate.password = ?2";

	public boolean changePassword(long accountId, String oldPassword,
			String newPassword) {
		EntityManagerHelper.log("changePasswording", Level.INFO, null);
		try {
			Query query = EntityManagerHelper.getEntityManager().createQuery(
					changePasswordJPQL);
			query.setParameter(1, accountId);
			query.setParameter(2, oldPassword);
			query.setParameter(3, newPassword);
			int count = query.executeUpdate();
			EntityManagerHelper.log("changePassword successful", Level.INFO,
					null);
			return count == 1;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("changePassword failed", Level.SEVERE, re);
			throw re;
		}
	}

	private final String changeScreenNameJPQL = "update Authenticate authenticate set authenticate.screenName = ?2 where authenticate.accountId = ?1";

	private final String changeScreenNameSetJPQL = "update Profile profile set profile.screenName = ?2, profile.updateCount = profile.updateCount + 1 where profile.accountId = ?1";

	public boolean changeScreenName(long accountId, String screenName) {
		EntityManagerHelper.log("changeScreenNameing", Level.INFO, null);
		try {
			Query query = EntityManagerHelper.getEntityManager().createQuery(
					changeScreenNameJPQL);
			query.setParameter(1, accountId);
			query.setParameter(2, screenName);
			int count = query.executeUpdate();
			if (count == 1) {
				query = EntityManagerHelper.getEntityManager().createQuery(
						changeScreenNameSetJPQL);
				query.setParameter(1, accountId);
				query.setParameter(2, screenName);
				query.executeUpdate();
				this.increaseUpdateCount(accountId, 1);
			}
			EntityManagerHelper.log("changeScreenName successful", Level.INFO,
					null);
			return count == 1;
		} catch (RuntimeException re) {
			EntityManagerHelper
					.log("changeScreenName failed", Level.SEVERE, re);
			throw re;
		}
	}

}
