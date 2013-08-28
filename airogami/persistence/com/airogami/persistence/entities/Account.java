package com.airogami.persistence.entities;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import org.apache.struts2.json.annotations.JSON;

/**
 * Account entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "ACCOUNT", catalog = "Airogami")
public class Account implements java.io.Serializable {

	// Fields
	private static final long serialVersionUID = 1L;

	private Long accountId;

	private Authenticate authenticate;

	private Long updateCount = 0L;

	private List<Report> reportsForReportedId = new ArrayList<Report>(0);

	private List<PlaneHist> planeHists = new ArrayList<PlaneHist>(0);

	private List<Plane> planesForOwnerId = new ArrayList<Plane>(0);

	private List<Message> messages = new ArrayList<Message>(0);

	private Profile profile;

	private List<Report> reportsForReportId = new ArrayList<Report>(0);

	private List<Plane> planesForTargetId = new ArrayList<Plane>(0);

	private List<Chain> chains = new ArrayList<Chain>(0);

	private List<ChainHist> chainHists = new ArrayList<ChainHist>(0);

	private AccountStat accountStat;

	private List<ChainMessage> chainMessages = new ArrayList<ChainMessage>(0);

	// Constructors

	/** default constructor */
	public Account() {
	}

	/** minimal constructor */
	public Account(Long accountId, Authenticate authenticate, Long updateCount) {
		this.accountId = accountId;
		this.authenticate = authenticate;
		this.updateCount = updateCount;
	}

	/** full constructor */
	public Account(Long accountId, Authenticate authenticate, Long updateCount,
			List<Report> reportsForReportedId, List<PlaneHist> planeHists,
			List<Plane> planesForOwnerId, List<Message> messages,
			Profile profile, List<Report> reportsForReportId,
			List<Plane> planesForTargetId, List<Chain> chains,
			List<ChainHist> chainHists, AccountStat accountStat,
			List<ChainMessage> chainMessages) {
		this.accountId = accountId;
		this.authenticate = authenticate;
		this.updateCount = updateCount;
		this.reportsForReportedId = reportsForReportedId;
		this.planeHists = planeHists;
		this.planesForOwnerId = planesForOwnerId;
		this.messages = messages;
		this.profile = profile;
		this.reportsForReportId = reportsForReportId;
		this.planesForTargetId = planesForTargetId;
		this.chains = chains;
		this.chainHists = chainHists;
		this.accountStat = accountStat;
		this.chainMessages = chainMessages;
	}

	// Property accessors
	@Id
	@Column(name = "ACCOUNT_ID", unique = true, nullable = false)
	public Long getAccountId() {
		return this.accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	@OneToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn
	public Authenticate getAuthenticate() {
		return this.authenticate;
	}

	public void setAuthenticate(Authenticate authenticate) {
		this.authenticate = authenticate;
	}

	@Column(name = "UPDATE_COUNT", nullable = false, insertable = false, updatable = false)
	public Long getUpdateCount() {
		return this.updateCount;
	}

	public void setUpdateCount(Long updateCount) {
		this.updateCount = updateCount;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "accountByReportedId")
	public List<Report> getReportsForReportedId() {
		return this.reportsForReportedId;
	}

	public void setReportsForReportedId(List<Report> reportsForReportedId) {
		this.reportsForReportedId = reportsForReportedId;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "account")
	public List<PlaneHist> getPlaneHists() {
		return this.planeHists;
	}

	public void setPlaneHists(List<PlaneHist> planeHists) {
		this.planeHists = planeHists;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "accountByOwnerId")
	public List<Plane> getPlanesForOwnerId() {
		return this.planesForOwnerId;
	}

	public void setPlanesForOwnerId(List<Plane> planesForOwnerId) {
		this.planesForOwnerId = planesForOwnerId;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "account")
	public List<Message> getMessages() {
		return this.messages;
	}

	public void setMessages(List<Message> messages) {
		this.messages = messages;
	}

	@OneToOne(fetch = FetchType.LAZY, mappedBy = "account")
	public Profile getProfile() {
		return this.profile;
	}

	public void setProfile(Profile profile) {
		this.profile = profile;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "accountByReportId")
	public List<Report> getReportsForReportId() {
		return this.reportsForReportId;
	}

	public void setReportsForReportId(List<Report> reportsForReportId) {
		this.reportsForReportId = reportsForReportId;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "accountByTargetId")
	public List<Plane> getPlanesForTargetId() {
		return this.planesForTargetId;
	}

	public void setPlanesForTargetId(List<Plane> planesForTargetId) {
		this.planesForTargetId = planesForTargetId;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "account")
	public List<Chain> getChains() {
		return this.chains;
	}

	public void setChains(List<Chain> chains) {
		this.chains = chains;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "account")
	public List<ChainHist> getChainHists() {
		return this.chainHists;
	}

	public void setChainHists(List<ChainHist> chainHists) {
		this.chainHists = chainHists;
	}

	@OneToOne(fetch = FetchType.LAZY, mappedBy = "account")
	public AccountStat getAccountStat() {
		return this.accountStat;
	}

	public void setAccountStat(AccountStat accountStat) {
		this.accountStat = accountStat;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "account")
	@OrderBy("createdTime desc")
	public List<ChainMessage> getChainMessages() {
		return this.chainMessages;
	}

	public void setChainMessages(List<ChainMessage> chainMessages) {
		this.chainMessages = chainMessages;
	}

}