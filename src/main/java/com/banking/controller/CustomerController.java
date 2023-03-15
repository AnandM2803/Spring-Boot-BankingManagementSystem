package com.banking.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.banking.entity.BankAccount;
import com.banking.entity.BankTransaction;
import com.banking.entity.Customer;
import com.banking.entity.Login;
import com.banking.exception.MyException;
import com.banking.helper.ResponseStructure;
import com.banking.service.CustomerService;

@RestController
@RequestMapping("customer")
public class CustomerController {

	@Autowired
	CustomerService custser;
	
	@PostMapping("add")
	public ResponseStructure<Customer> save(@RequestBody Customer customer) throws MyException
	{
		return custser.save(customer);
	}
	
	@PutMapping("otp/{cust_id}/{otp}")
	public ResponseStructure<Customer> otpverify(@PathVariable int cust_id,@PathVariable int otp) throws MyException
	{
		return custser.otpverify(cust_id,otp);
	}
	
	@PostMapping("login")
	public ResponseStructure<Customer> login(@RequestBody Login login) throws MyException{
		return custser.login(login);
	}
	@PostMapping("account/{id}/{type}")
	public ResponseStructure<Customer> createAccount(@PathVariable int id, @PathVariable String type) throws MyException{
		
		return custser.createAccount(id,type) ;
		
	}
	
	@GetMapping("/accounts/{custid}")
	public ResponseStructure<List<BankAccount>> fetchAllTrue(@PathVariable int custid) throws MyException
	{
		return custser.fetchAllTrue(custid);
	}
	
	@GetMapping("/account/check/{acno}")
	public ResponseStructure<Double> checkBalance(@PathVariable long acno)
	{
		return custser.checkBalance(acno);
	}
	
	@PutMapping("/account/deposit/{acno}/{amount}")
	public ResponseStructure<BankAccount> deposit(@PathVariable long acno,@PathVariable double amount)
	{
		return custser.deposit(acno,amount);
	}
	
	@PutMapping("/account/withdraw/{acno}/{amount}")
	public ResponseStructure<BankAccount> withdraw(@PathVariable long acno,@PathVariable double amount) throws MyException
	{
		return custser.withdraw(acno,amount);
	}
	
	@GetMapping("/account/viewtransaction/{acno}")
	public ResponseStructure<List<BankTransaction>> viewTransaction(@PathVariable long acno) throws MyException
	{
		return custser.viewTransaction(acno);
	}
	
	
}
