package com.banking.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.banking.entity.BankAccount;
import com.banking.entity.Management;
import com.banking.exception.MyException;
import com.banking.helper.ResponseStructure;
import com.banking.service.ManagementService;

@RestController
@RequestMapping("management")
public class ManagementController {

	@Autowired
	ManagementService mngser;

	@PostMapping("add")
	public ResponseStructure<Management> addDeatils(@RequestBody Management management)
	{
		return mngser.addDetails(management);
	}
	
	@PostMapping("/login")
	public ResponseStructure<Management> login(@RequestBody Management management) throws MyException
	{
		return mngser.login(management);
	}
	
	@GetMapping("/accounts")
	public ResponseStructure<List<BankAccount>> fetchAllAccounts() throws MyException
	{
		return mngser.fetchAllAccounts();
	}
	
	@PutMapping("/accountchange/{acno}")
		public ResponseStructure<BankAccount> changeStatus(@PathVariable long acno)
		{
		return mngser.changeStatus(acno);	
		}
	
	
}
