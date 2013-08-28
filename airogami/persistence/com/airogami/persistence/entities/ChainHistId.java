package com.airogami.persistence.entities;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import org.apache.struts2.json.annotations.JSON;

/**
 * ChainHistId entity. @author MyEclipse Persistence Tools
 */
@Embeddable
public class ChainHistId implements java.io.Serializable {

	// Fields
	private static final long serialVersionUID = 1L;

	private Long accountId;

	private Long chainId;

	// Constructors

	/** default constructor */
	public ChainHistId() {
	}

	/** full constructor */
	public ChainHistId(Long accountId, Long chainId) {
		this.accountId = accountId;
		this.chainId = chainId;
	}

	// Property accessors

	@Column(name = "ACCOUNT_ID", nullable = false)
	public Long getAccountId() {
		return this.accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	@Column(name = "CHAIN_ID", nullable = false)
	public Long getChainId() {
		return this.chainId;
	}

	public void setChainId(Long chainId) {
		this.chainId = chainId;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof ChainHistId))
			return false;
		ChainHistId castOther = (ChainHistId) other;

		return ((this.getAccountId() == castOther.getAccountId()) || (this
				.getAccountId() != null && castOther.getAccountId() != null && this
				.getAccountId().equals(castOther.getAccountId())))
				&& ((this.getChainId() == castOther.getChainId()) || (this
						.getChainId() != null && castOther.getChainId() != null && this
						.getChainId().equals(castOther.getChainId())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getAccountId() == null ? 0 : this.getAccountId().hashCode());
		result = 37 * result
				+ (getChainId() == null ? 0 : this.getChainId().hashCode());
		return result;
	}

}