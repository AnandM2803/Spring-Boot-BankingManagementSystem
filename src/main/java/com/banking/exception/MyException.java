package com.banking.exception;

public class MyException extends Exception{
	
	String msg="Id Not Found";
	
	 public MyException(String msg) 
	{
		this.msg=msg;
	}

	public MyException()
	{
		
	}

	@Override
	public String toString() {
		return msg;
	}
	
	
}
