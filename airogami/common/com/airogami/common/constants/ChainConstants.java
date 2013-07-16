package com.airogami.common.constants;

public interface ChainConstants {

	public final byte StatusUnmatched = 0;//not found next member
	public final byte StatusMatched = 1;// found next member
	public final byte MaxMatchCount = 20;
	public final byte MaxPassCount = 20;
}
