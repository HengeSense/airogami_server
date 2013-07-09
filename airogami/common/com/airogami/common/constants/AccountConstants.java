package com.airogami.common.constants;

public interface AccountConstants {
	public static final int AuthenticateTypeEmail=0;
	public static final int AuthenticateTypeFacebook=1;
	public static final int AuthenticateTypeTwitter=2;
	public static final int AuthenticateTypePhone=3;
	/*The four codes specified in ISO/IEC 5218 are:
		0 = not known,
		1 = male,
		2 = female,
		9 = not applicable.*/
	public static final byte SexType_Unknown = 0;
	public static final byte SexType_Male = 1;
	public static final byte SexType_Female = 2;
	public static final byte SexType_NotApllicable = 9;
}