package com.airogami.persistence.daos;

import java.util.List;
import java.util.logging.Level;

import javax.persistence.EntityExistsException;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import com.airogami.application.exception.ApplicationException;
import com.airogami.persistence.entities.Authenticate;
import com.airogami.persistence.entities.EntityManagerHelper;
import com.airogami.persistence.entities.Report;
import com.airogami.persistence.entities.ReportDAO;

public class ReportDao extends ReportDAO {
	 
	// return null if failed
	public Report createReport(Report report) {
		EntityManagerHelper.log("createReporting", Level.INFO, null);		
		try {
			// try to save
			try {
				EntityManagerHelper.beginTransaction();
				this.save(report);		
				EntityManagerHelper.getEntityManager().flush();
				EntityManagerHelper.commit();
			} catch (PersistenceException pe) {
				if(pe.getCause() instanceof EntityExistsException){
					//System.out.println("catched: " + pe.getCause().getMessage());
				}
				else{
					throw pe;
				}
			} finally {
				EntityManagerHelper.closeEntityManager();
			}
			
			// try to find			
			try {
				String reason = report.getReason();
				EntityManagerHelper.beginTransaction();				
				report = this.findById(report.getId());
				if(report != null){
					if(report.getReportCount() != 0){
						report.setReason(reason);
					}
					this.increaseReportCount(report.getId(), 1);
				}								
				EntityManagerHelper.commit();
			} finally {
				EntityManagerHelper.closeEntityManager();
			} 			
			if(report != null){
				report.setReportCount(report.getReportCount() + 1);
			}
			EntityManagerHelper
					.log("createReport successful", Level.INFO, null);
		} catch (RuntimeException re) {
			EntityManagerHelper.log("createReport failed", Level.SEVERE, re);
			throw re;
		}
		return report;
	}
}
