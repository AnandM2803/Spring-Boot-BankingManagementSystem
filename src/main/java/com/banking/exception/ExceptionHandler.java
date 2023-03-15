package com.banking.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;

import com.banking.helper.ResponseStructure;

@ControllerAdvice
public class ExceptionHandler {

	
   	@org.springframework.web.bind.annotation.ExceptionHandler(value = MyException.class)
		public ResponseEntity<ResponseStructure<String>> idNotFound(MyException ie) {
			ResponseStructure<String> responseStructure = new ResponseStructure<String>();
			responseStructure.setCode(HttpStatus.NOT_FOUND.value());
			responseStructure.setMessgae("Request failed");
			responseStructure.setData(ie.toString());
			return new ResponseEntity<ResponseStructure<String>>(responseStructure, HttpStatus.NOT_ACCEPTABLE);


	}
}
