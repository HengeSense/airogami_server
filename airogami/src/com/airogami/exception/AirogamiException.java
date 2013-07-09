package com.airogami.exception;

public class AirogamiException extends Exception implements AirogamiError{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5951162812583275078L;	
	
	private int status;

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public AirogamiException() {
		// TODO Auto-generated constructor stub
	}

	public AirogamiException(int status,String message) {
		super(message);
		this.status=status;
		// TODO Auto-generated constructor stub
	}

	public AirogamiException(Throwable arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public AirogamiException(String arg0, Throwable arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

	
	public String toString(){
		return status + ": "+ this.getMessage();
	}	
	

}
