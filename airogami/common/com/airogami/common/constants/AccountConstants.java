package com.airogami.common.constants;

public interface AccountConstants {
	
	public static final int AuthenticateTypeEmail=0;
	public static final int AuthenticateTypeScreenName=1;
	public static final int AuthenticateTypeFacebook=2;
	public static final int AuthenticateTypeTwitter=3;
	public static final int AuthenticateTypePhone=4;
	
	/*The four codes specified in ISO/IEC 5218 are:
		0 = not known,
		1 = male,
		2 = female,
		9 = not applicable.*/
	
	public static final byte SexType_Unknown = 0;
	public static final byte SexType_Male = 1;
	public static final byte SexType_Female = 2;
	public static final byte SexType_NotApllicable = 9;
	
	public static final byte SendMaxCount = 6;
	public static final byte PickupMaxCount = 6;

}