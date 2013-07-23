package com.airogami.exception;

public interface AirogamiError {
	public static final int Others_Status = -2;
	public static final int Unknown_Status = -1;
	public static final String Unknown_Message = "unknown";
	
	public static final int OK_Status=0;
	public static final String OK_Message = "success";
	/* 
	 * account begins
	 */
	public static final int Account_Signup_Input_Status = 10001;
	public static final String Account_Signup_Input_Message = "error.account.signup.input";
	public static final int Account_Signup_Failure_Status = 10002;		
	public static final String Account_Signup_Failure_Message = "error.account.signup.failure";
	public static final int Account_Signup_Duplicate_Status = 10003;		
	public static final String Account_Signup_Duplicate_Message = "error.account.signup.duplicate";
	
	public static final int Account_No_Signin_Status = 10007;
	public static final String Account_No_Signin_Message = "error.account.notsignin";
	
	public static final int Account_Signin_Input_Status = 10100;
	public static final int Account_Signin_Failure_Status = 100101;
	public static final String Account_Signin_Input_Message = "error.account.signin.input";
	public static final String Account_Signin_Failure_Message = "error.account.signin.failure";
	
	public static final int Account_EditAccount_Input_Status = 100102;
	public static final int Account_EditAccount_Failure_Status = 100103;
	public static final String Account_EditAccount_Input_Message = "error.account.editaccount.input";
	public static final String Account_EditAccount_Failure_Message = "error.account.editaccount.failure";
	
	public static final int Account_ChangePassword_Input_Status = 100104;
	public static final int Account_ChangePassword_Failure_Status = 100105;
	public static final String Account_ChangePassword_Input_Message = "error.account.changepassword.input";
	public static final String Account_ChangePassword_Failure_Message = "error.account.changepassword.failure";

	public static final int Account_ChangeScreenName_Input_Status = 100106;
	public static final int Account_ChangeScreenName_Failure_Status = 100107;
	public static final String Account_ChangeScreenName_Input_Message = "error.account.changescreenname.input";
	public static final String Account_ChangeScreenName_Failure_Message = "error.account.changescreenname.failure";

	public static final int Account_ObtainAccount_Input_Status = 100108;
	public static final int Account_ObtainAccount_Failure_Status = 100109;
	public static final String Account_ObtainAccount_Input_Message = "error.account.obtainaccount.input";
	public static final String Account_ObtainAccount_Failure_Message = "error.account.obtainaccount.failure";

	
	/* 
	 * account ends
	 */
	
	/* 
	 * plane begins
	 */
	public static final int Plane_SendPlane_Input_Status = 20000;
	public static final String Plane_SendPlane_Input_Message = "error.plane.sendplane.input";
	public static final int Plane_SendPlane_Failure_Status = 20001;
	public static final String Plane_SendPlane_Failure_Message = "error.plane.sendplane.failure";
	
	public static final int Plane_ReplyPlane_Input_Status = 20002;
	public static final String Plane_ReplyPlane_Input_Message = "error.plane.replyplane.input";
	public static final int Plane_ReplyPlane_Failure_Status = 20003;
	public static final String Plane_ReplyPlane_Failure_Message = "error.plane.replyplane.failure";
	
	public static final int Plane_ThrowPlane_Input_Status = 20004;
	public static final String Plane_ThrowPlane_Input_Message = "error.plane.throwplane.input";
	public static final int Plane_ThrowPlane_Failure_Status = 20005;
	public static final String Plane_ThrowPlane_Failure_Message = "error.plane.throwplane.failure";
	
	public static final int Plane_DeletePlane_Input_Status = 20006;
	public static final String Plane_DeletePlane_Input_Message = "error.plane.deleteplane.input";
	public static final int Plane_DeletePlane_Failure_Status = 20007;
	public static final String Plane_DeletePlane_Failure_Message = "error.plane.deleteplane.failure";
	
	public static final int Plane_ObtainPlanes_Input_Status = 20008;
	public static final String Plane_ObtainPlanes_Input_Message = "error.plane.obtainplanes.input";
	public static final int Plane_ObtainPlanes_Failure_Status = 20009;
	public static final String Plane_ObtainPlanes_Failure_Message = "error.plane.obtainplanes.failure";
	
	public static final int Plane_ObtainMessages_Input_Status = 20010;
	public static final String Plane_ObtainMessages_Input_Message = "error.plane.obtainmessages.input";
	public static final int Plane_ObtainMessages_Failure_Status = 20011;
	public static final String Plane_ObtainMessages_Failure_Message = "error.plane.obtainmessages.failure";
	
	public static final int Plane_ViewedMessages_Input_Status = 20012;
	public static final String Plane_ViewedMessages_Input_Message = "error.plane.viewedmessages.input";
	public static final int Plane_ViewedMessages_Failure_Status = 20013;
	public static final String Plane_ViewedMessages_Failure_Message = "error.plane.viewedmessages.failure";
	
	public static final int Plane_CreateCategory_Input_Status = 20014;
	public static final String Plane_CreateCategory_Input_Message = "error.plane.createcategory.input";
	public static final int Plane_CreateCategory_Failure_Status = 20015;
	public static final String Plane_CreateCategory_Failure_Message = "error.plane.createcategory.failure";
	
	public static final int Plane_PickupPlane_Input_Status = 20016;
	public static final String Plane_PickupPlane_Input_Message = "error.plane.pickup.input";
	public static final int Plane_PickupPlane_Failure_Status = 20017;
	public static final String Plane_PickupPlane_Failure_Message = "error.plane.pickup.failure";
	
	public static final int Plane_LikePlane_Input_Status = 20018;
	public static final String Plane_LikePlane_Input_Message = "error.plane.likeplane.input";
	public static final int Plane_LikePlane_Failure_Status = 20019;
	public static final String Plane_LikePlane_Failure_Message = "error.plane.likeplane.failure";
	
	public static final int Plane_ReceivePlanes_Input_Status = 20020;
	public static final String Plane_ReceivePlanes_Input_Message = "error.plane.receiveplanes.input";
	public static final int Plane_ReceivePlanes_Failure_Status = 20021;
	public static final String Plane_ReceivePlanes_Failure_Message = "error.plane.receiveplanes.failure";

	
	//public static final int Card_EditCard_Modified_Status = 20010;
	//public static final String Card_EditCard_Modified_Message = "error.card.modified.failure";

	/* 
	 * plane ends
	 */
	
	/* 
	 * chain begins
	 */
	public static final int Chain_SendChain_Input_Status = 30000;
	public static final String Chain_SendChain_Input_Message = "error.chain.sendchain.input";
	public static final int Chain_SendChain_Failure_Status = 30001;
	public static final String Chain_SendChain_Failure_Message = "error.chain.sendchain.failure";
	
	public static final int Chain_ReplyChain_Input_Status = 30002;
	public static final String Chain_ReplyChain_Input_Message = "error.chain.replychain.input";
	public static final int Chain_ReplyChain_Failure_Status = 30003;
	public static final String Chain_ReplyChain_Failure_Message = "error.chain.replychain.failure";
	
	public static final int Chain_ThrowChain_Input_Status = 30004;
	public static final String Chain_ThrowChain_Input_Message = "error.chain.throwchain.input";
	public static final int Chain_ThrowChain_Failure_Status = 30005;
	public static final String Chain_ThrowChain_Failure_Message = "error.chain.throwchain.failure";
	
	public static final int Chain_DeleteChain_Input_Status = 30006;
	public static final String Chain_DeleteChain_Input_Message = "error.chain.deletechain.input";
	public static final int Chain_DeleteChain_Failure_Status = 30007;
	public static final String Chain_DeleteChain_Failure_Message = "error.chain.deletechain.failure";	
	
	public static final int Chain_ObtainChains_Input_Status = 30008;
	public static final String Chain_ObtainChains_Input_Message = "error.chain.obtainchains.input";
	public static final int Chain_ObtainChains_Failure_Status = 30009;
	public static final String Chain_ObtainChains_Failure_Message = "error.chain.obtainchains.failure";	
	
	public static final int Chain_ObtainChainMessages_Input_Status = 30010;
	public static final String Chain_ObtainChainMessages_Input_Message = "error.chain.obtainchainmessages.input";
	public static final int Chain_ObtainChainMessages_Failure_Status = 30011;
	public static final String Chain_ObtainChainMessages_Failure_Message = "error.chain.obtainchainmessages.failure";
	
	public static final int Chain_ViewedChainMessages_Input_Status = 30012;
	public static final String Chain_ViewedChainMessages_Input_Message = "error.chain.viewedchainmessages.input";
	public static final int Chain_ViewedChainMessages_Failure_Status = 30013;
	public static final String Chain_ViewedChainMessages_Failure_Message = "error.chain.viewedchainmessages.failure"; 
	
    public static final int Chain_ReceiveChains_Input_Status = 30014;
	public static final String Chain_ReceiveChains_Input_Message = "error.chain.receivechains.input";
	public static final int Chain_ReceiveChains_Failure_Status = 30015;
	public static final String Chain_ReceiveChains_Failure_Message = "error.chain.receivechains.failure";	

		
	/*  
	 * chain ends
	 */
	
	/* 
	 * image begins
	 */
	public static final int Image_Upload_Failure_Status = 40000;
	public static final String Image_Upload_Failure_Message = "error.image.upload.failure";
	/* 
	 * image ends
	 */
	
	/* 
	 * recommend begins
	 */
	public static final int Recommend_RecommendCard_Failure_Status = 50000;
	public static final String Recommend_RecommendCard_Failure_Message = "error.recommend.recommendcard.failure";
	public static final int Recommend_GetNewsCount_Failure_Status = 50001;
	public static final String Recommend_GetNewsCount_Failure_Message = "error.recommend.getnews.failure";
	public static final int Recommend_GetNews_Failure_Status = 50002;
	public static final String Recommend_GetNews_Failure_Message = "error.recommend.getnews.failure";
	/* 
	 * recommend begins
	 */
}
