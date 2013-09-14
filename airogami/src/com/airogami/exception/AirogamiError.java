package com.airogami.exception;

public interface AirogamiError {
	public static final int Others_Status = -2;
	public static final int Unknown_Status = -1;
	public static final String Unknown_Message = "unknown";
	
	public static final int OK_Status = 0;
	public static final String OK_Message = "success";
	
	/* 
	 * display messages begins
	 */
	public static final int Account_No_Signin_Status = 1;
	public static final String Account_No_Signin_Message = "error.account.notsignin";
	public static final int Account_Signin_ElseWhere_Status = 2;
	public static final String Account_Signin_ElseWhere_Message = "error.account.signin.elsewhere";
	
	public static final int Application_Input_Status = 101;
	public static final String Application_Input_Message = "error.application.input";
	
	public static final int Application_Exception_Status = 102;
	public static final String Application_Exception_Message = "error.application.exception";
	
	/* 
	 * display messages ends
	 */
	
	/* 
	 * account begins (10000)
	 */
	
	/* 
	 * account ends
	 */
	
	/* 
	 * plane begins (20000)
	 */
	
	/* 
	 * plane ends
	 */
	
	/* 
	 * chain begins (30000)
	 */
	
	/*  
	 * chain ends
	 */
	
	/* 
	 * Data begins (40000)
	 */
	
	/* 
	 * data ends
	 */
}
