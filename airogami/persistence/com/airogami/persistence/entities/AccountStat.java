package com.airogami.persistence.entities;

import com.airogami.common.constants.AccountConstants;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import org.apache.struts2.json.annotations.JSON;

/**
 * AccountStat entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "ACCOUNT_STAT", catalog = "Airogami")
public class AccountStat implements java.io.Serializable {

	// Fields
	private static final long serialVersionUID = 1L;

	private Integer accountId;

	private Account account;

	private Integer signinCount = 0;

	private Short status = 0;

	private Integer msgCount = 0;

	private Integer chainMsgCount = 0;

	private Short devType;

	private String devToken;

	private Short pickupLeftCount = AccountConstants.PickupMaxCount;

	private Short sendLeftCount = AccountConstants.SendMaxCount;

	private Short pickupCount = AccountConstants.PickupMaxCount;

	private Short sendCount = AccountConstants.SendMaxCount;

	// Constructors

	/** default constructor */
	public AccountStat() {
	}

	/** minimal constructor */
	public AccountStat(Integer accountId, Account account, Integer signinCount,
			Short status, Integer msgCount, Integer chainMsgCount,
			Short pickupLeftCount, Short sendLeftCount, Short pickupCount,
			Short sendCount) {
		this.accountId = accountId;
		this.account = account;
		this.signinCount = signinCount;
		this.status = status;
		this.msgCount = msgCount;
		this.chainMsgCount = chainMsgCount;
		this.pickupLeftCount = pickupLeftCount;
		this.sendLeftCount = sendLeftCount;
		this.pickupCount = pickupCount;
		this.sendCount = sendCount;
	}

	/** full constructor */
	public AccountStat(Integer accountId, Account account, Integer signinCount,
			Short status, Integer msgCount, Integer chainMsgCount,
			Short devType, String devToken, Short pickupLeftCount,
			Short sendLeftCount, Short pickupCount, Short sendCount) {
		this.accountId = accountId;
		this.account = account;
		this.signinCount = signinCount;
		this.status = status;
		this.msgCount = msgCount;
		this.chainMsgCount = chainMsgCount;
		this.devType = devType;
		this.devToken = devToken;
		this.pickupLeftCount = pickupLeftCount;
		this.sendLeftCount = sendLeftCount;
		this.pickupCount = pickupCount;
		this.sendCount = sendCount;
	}

	// Property accessors
	@Id
	@Column(name = "ACCOUNT_ID", unique = true, nullable = false)
	public Integer getAccountId() {
		return this.accountId;
	}

	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
	}

	@OneToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn
	public Account getAccount() {
		return this.account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	@Column(name = "SIGNIN_COUNT", nullable = false, insertable = false, updatable = false)
	public Integer getSigninCount() {
		return this.signinCount;
	}

	public void setSigninCount(Integer signinCount) {
		this.signinCount = signinCount;
	}

	@Column(name = "STATUS", nullable = false)
	public Short getStatus() {
		return this.status;
	}

	public void setStatus(Short status) {
		this.status = status;
	}

	@Column(name = "MSG_COUNT", nullable = false, insertable = false, updatable = false)
	public Integer getMsgCount() {
		return this.msgCount;
	}

	public void setMsgCount(Integer msgCount) {
		this.msgCount = msgCount;
	}

	@Column(name = "CHAIN_MSG_COUNT", nullable = false, insertable = false, updatable = false)
	public Integer getChainMsgCount() {
		return this.chainMsgCount;
	}

	public void setChainMsgCount(Integer chainMsgCount) {
		this.chainMsgCount = chainMsgCount;
	}

	@Column(name = "DEV_TYPE")
	public Short getDevType() {
		return this.devType;
	}

	public void setDevType(Short devType) {
		this.devType = devType;
	}

	@Column(name = "DEV_TOKEN", length = 4096)
	public String getDevToken() {
		return this.devToken;
	}

	public void setDevToken(String devToken) {
		this.devToken = devToken;
	}

	@Column(name = "PICKUP_LEFT_COUNT", nullable = false, updatable = false)
	public Short getPickupLeftCount() {
		return this.pickupLeftCount;
	}

	public void setPickupLeftCount(Short pickupLeftCount) {
		this.pickupLeftCount = pickupLeftCount;
	}

	@Column(name = "SEND_LEFT_COUNT", nullable = false, updatable = false)
	public Short getSendLeftCount() {
		return this.sendLeftCount;
	}

	public void setSendLeftCount(Short sendLeftCount) {
		this.sendLeftCount = sendLeftCount;
	}

	@Column(name = "PICKUP_COUNT", nullable = false, updatable = false)
	public Short getPickupCount() {
		return this.pickupCount;
	}

	public void setPickupCount(Short pickupCount) {
		this.pickupCount = pickupCount;
	}

	@Column(name = "SEND_COUNT", nullable = false, updatable = false)
	public Short getSendCount() {
		return this.sendCount;
	}

	public void setSendCount(Short sendCount) {
		this.sendCount = sendCount;
	}

	/**/

}