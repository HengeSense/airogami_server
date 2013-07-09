package com.airogami.exception;

public interface AirogamiError {
	public static final int OK_Status=0;
	public static final String OK_Message = "success";
	/* 
	 * account begins
	 */
	public static final int Account_Signup_Input_Status = 10001;
	public static final String Account_Signup_Input_Message = "error.account.signup.input";
	public static final int Account_Signup_Failure_Status = 10002;		
	public static final String Account_Signup_Failure_Message = "error.account.signup.failure";
	
	public static final int Account_No_Signin_Status = 10007;
	public static final String Account_No_Signin_Message = "error.account.signin.failure";
	
	public static final int Account_Signin_Input_Status = 10100;
	public static final int Account_Signin_Failure_Status = 100101;
	public static final String Account_Signin_Input_Message = "error.account.signin.input";
	public static final String Account_Signin_Failure_Message = "error.account.signin.failure";
	
	public static final int Account_FollowAccount_Input_Status = 100102;
	public static final int Account_FollowAccount_Failure_Status = 100103;
	public static final String Account_FollowAccount_Input_Message = "error.account.followaccount.input";
	public static final String Account_FollowAccount_Failure_Message = "error.account.followaccount.failure";
	/* 
	 * account ends
	 */
	
	/* 
	 * card begins
	 */
	public static final int Card_CreateCard_Input_Status = 20000;
	public static final String Card_CreateCard_Input_Message = "error.card.createcard.input";
	public static final int Card_CreateCard_Failure_Status = 20001;
	public static final String Card_CreateCard_Failure_Message = "error.card.createcard.failure";
	public static final int Card_CreateComment_Input_Status = 20002;
	public static final String Card_CreateComment_Input_Message = "error.card.commentcard.input";
	public static final int Card_CommentCard_Failure_Status = 20003;
	public static final String Card_CommentCard_Failure_Message = "error.card.commentcard.failure";
	public static final int Card_RepostCard_Input_Status = 20004;
	public static final String Card_RepostCard_Input_Message = "error.card.repostcard.input";
	public static final int Card_RepostCard_Failure_Status = 20005;
	public static final String Card_RepostCard_Failure_Message = "error.card.repostcard.failure";
	public static final int Card_LikeCard_Input_Status = 20006;
	public static final String Card_LikeCard_Input_Message = "error.card.likecard.input";
	public static final int Card_LikeCard_Failure_Status = 20007;
	public static final String Card_LikeCard_Failure_Message = "error.card.likecard.failure";
	public static final int Card_EditCard_Input_Status = 20008;
	public static final String Card_EditCard_Input_Message = "error.card.editcard.input";
	public static final int Card_EditCard_Failure_Status = 20009;
	public static final String Card_EditCard_Failure_Message = "error.card.editcard.failure";
	
	//public static final int Card_EditCard_Modified_Status = 20010;
	//public static final String Card_EditCard_Modified_Message = "error.card.modified.failure";

	/* 
	 * card ends
	 */
	
	/* 
	 * album begins
	 */
	public static final int Album_CreateAlbum_Input_Status = 30000;
	public static final String Album_CreateAlbum_Input_Message = "error.album.createalbum.input";
	public static final int Album_CreateAlbum_Failure_Status = 30001;
	public static final String Album_CreateAlbum_Failure_Message = "error.album.createcard.failure";
	
	public static final int Album_BlockAlbum_Input_Status = 30002;
	public static final String Album_BlockAlbum_Input_Message = "error.album.blockalbum.input";
	public static final int Album_BlockAlbum_Failure_Status = 30003;
	public static final String Album_BlockAlbum_Failure_Message = "error.album.blockalbums.failure";
	/* 
	 * album ends
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
