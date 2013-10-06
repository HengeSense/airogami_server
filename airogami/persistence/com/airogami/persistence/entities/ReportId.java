package com.airogami.persistence.entities;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import org.apache.struts2.json.annotations.JSON;

/**
 * ReportId entity. @author MyEclipse Persistence Tools
 */
@Embeddable
public class ReportId implements java.io.Serializable {

	// Fields
	private static final long serialVersionUID = 1L;

	private Integer reportId;

	private Integer reportedId;

	// Constructors

	/** default constructor */
	public ReportId() {
	}

	/** full constructor */
	public ReportId(Integer reportId, Integer reportedId) {
		this.reportId = reportId;
		this.reportedId = reportedId;
	}

	// Property accessors

	@Column(name = "REPORT_ID", nullable = false)
	public Integer getReportId() {
		return this.reportId;
	}

	public void setReportId(Integer reportId) {
		this.reportId = reportId;
	}

	@Column(name = "REPORTED_ID", nullable = false)
	public Integer getReportedId() {
		return this.reportedId;
	}

	public void setReportedId(Integer reportedId) {
		this.reportedId = reportedId;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof ReportId))
			return false;
		ReportId castOther = (ReportId) other;

		return ((this.getReportId() == castOther.getReportId()) || (this
				.getReportId() != null && castOther.getReportId() != null && this
				.getReportId().equals(castOther.getReportId())))
				&& ((this.getReportedId() == castOther.getReportedId()) || (this
						.getReportedId() != null
						&& castOther.getReportedId() != null && this
						.getReportedId().equals(castOther.getReportedId())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getReportId() == null ? 0 : this.getReportId().hashCode());
		result = 37
				* result
				+ (getReportedId() == null ? 0 : this.getReportedId()
						.hashCode());
		return result;
	}

	/**/

}