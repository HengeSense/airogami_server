package com.airogami.persistence.entities;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * ChainMessageId entity. @author MyEclipse Persistence Tools
 */
@Embeddable
public class ChainMessageId implements java.io.Serializable {

	// Fields
	private static final long serialVersionUID = 1L;

	private Long chainId;

	private Long accountId;

	// Constructors

	/** default constructor */
	public ChainMessageId() {
	}

	/** full constructor */
	public ChainMessageId(Long chainId, Long accountId) {
		this.chainId = chainId;
		this.accountId = accountId;
	}

	// Property accessors

	@Column(name = "CHAIN_ID", nullable = false)
	public Long getChainId() {
		return this.chainId;
	}

	public void setChainId(Long chainId) {
		this.chainId = chainId;
	}

	@Column(name = "ACCOUNT_ID", nullable = false)
	public Long getAccountId() {
		return this.accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof ChainMessageId))
			return false;
		ChainMessageId castOther = (ChainMessageId) other;

		return ((this.getChainId() == castOther.getChainId()) || (this
				.getChainId() != null && castOther.getChainId() != null && this
				.getChainId().equals(castOther.getChainId())))
				&& ((this.getAccountId() == castOther.getAccountId()) || (this
						.getAccountId() != null
						&& castOther.getAccountId() != null && this
						.getAccountId().equals(castOther.getAccountId())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getChainId() == null ? 0 : this.getChainId().hashCode());
		result = 37 * result
				+ (getAccountId() == null ? 0 : this.getAccountId().hashCode());
		return result;
	}

}