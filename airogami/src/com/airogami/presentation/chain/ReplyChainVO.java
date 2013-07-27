package com.airogami.presentation.chain;

public class ReplyChainVO {

	private Long chainId;
	private ChainMessageVO chainMessageVO = new ChainMessageVO();
	
	public Long getChainId() {
		return chainId;
	}
	public void setChainId(Long chainId) {
		this.chainId = chainId;
	}
	public ChainMessageVO getChainMessageVO() {
		return chainMessageVO;
	}
	public void setChainMessageVO(ChainMessageVO chainMessageVO) {
		this.chainMessageVO = chainMessageVO;
	}	
}
