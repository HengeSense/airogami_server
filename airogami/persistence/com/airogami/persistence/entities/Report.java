package com.airogami.persistence.entities;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import org.apache.struts2.json.annotations.JSON;

/**
 * Report entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "REPORT", catalog = "Airogami")
public class Report implements java.io.Serializable {

	// Fields
	private static final long serialVersionUID = 1L;

	private ReportId id;

	private Account accountByReportId;

	private Account accountByReportedId;

	private Timestamp updatedTime;

	private Integer reportCount = 0;

	private String reason;

	// Constructors

	/** default constructor */
	public Report() {
	}

	/** full constructor */
	public Report(ReportId id, Account accountByReportId,
			Account accountByReportedId, Timestamp updatedTime,
			Integer reportCount, String reason) {
		this.id = id;
		this.accountByReportId = accountByReportId;
		this.accountByReportedId = accountByReportedId;
		this.updatedTime = updatedTime;
		this.reportCount = reportCount;
		this.reason = reason;
	}

	// Property accessors
	@EmbeddedId
	@AttributeOverrides({
			@AttributeOverride(name = "reportId", column = @Column(name = "REPORT_ID", nullable = false)),
			@AttributeOverride(name = "reportedId", column = @Column(name = "REPORTED_ID", nullable = false)) })
	public ReportId getId() {
		return this.id;
	}

	public void setId(ReportId id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "REPORT_ID", nullable = false, insertable = false, updatable = false)
	public Account getAccountByReportId() {
		return this.accountByReportId;
	}

	public void setAccountByReportId(Account accountByReportId) {
		this.accountByReportId = accountByReportId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "REPORTED_ID", nullable = false, insertable = false, updatable = false)
	public Account getAccountByReportedId() {
		return this.accountByReportedId;
	}

	public void setAccountByReportedId(Account accountByReportedId) {
		this.accountByReportedId = accountByReportedId;
	}

	@Column(name = "UPDATED_TIME", nullable = false, insertable = false, updatable = false, length = 19)
	@JSON(format = "yyyy-MM-dd HH:mm:ss")
	public Timestamp getUpdatedTime() {
		return this.updatedTime;
	}

	public void setUpdatedTime(Timestamp updatedTime) {
		this.updatedTime = updatedTime;
	}

	@Column(name = "REPORT_COUNT", nullable = false, insertable = false, updatable = false)
	public Integer getReportCount() {
		return this.reportCount;
	}

	public void setReportCount(Integer reportCount) {
		this.reportCount = reportCount;
	}

	@Column(name = "REASON", nullable = false)
	public String getReason() {
		return this.reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	@PrePersist
	protected void onPrePersist() {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());

	}

}