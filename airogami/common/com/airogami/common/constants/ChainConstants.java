package com.airogami.common.constants;

public interface ChainConstants {

	public final byte StatusUnmatched = 0;//not found next member
	public final byte StatusMatched = 1;// found next member
	public final short MaxMatchCount = 20;
	public final short MaxPassCount = 20;
}
