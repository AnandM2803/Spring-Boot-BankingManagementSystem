package com.banking.helper;

import lombok.Data;

@Data
public class ResponseStructure<T> 
{

	int code;
	String messgae;
	T data;
}
