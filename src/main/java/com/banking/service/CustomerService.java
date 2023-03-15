package com.banking.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.banking.entity.BankAccount;
import com.banking.entity.BankTransaction;
import com.banking.entity.BankAccount;
import com.banking.entity.Customer;
import com.banking.entity.Login;
import com.banking.exception.MyException;
import com.banking.helper.MailVerifaction;
import com.banking.helper.ResponseStructure;
import com.banking.repository.BankRepository;
import com.banking.repository.CustomerRepository;

@Service
public class CustomerService {

	@Autowired
	CustomerRepository custrepo;

	@Autowired
	MailVerifaction mailVerifaction;

	@Autowired
	BankAccount accounts;
	
	@Autowired
	BankRepository bankRepository;
	
	@Autowired
	BankTransaction transactionrepo;

	public ResponseStructure<Customer> save(Customer customer) throws MyException {
		ResponseStructure<Customer> structure = new ResponseStructure<>();

		int age = Period.between(customer.getDob().toLocalDate(), LocalDate.now()).getYears();
		customer.setAge(age);
		if (age < 18) {
//			structure.setCode(HttpStatus.CONFLICT.value());
//			structure.setMessgae("Your Age Should be 18+ to create Account ");
//			structure.setData(customer);
			throw new MyException("Your Age Should be 18+ to create Account");
		} else {
			Random random = new Random();
			int otp = random.nextInt(100000, 999999);
			customer.setOtp(otp);

//			mailVerifaction.sendMail(customer);

			structure.setCode(HttpStatus.PROCESSING.value());
			structure.setMessgae("Varification Mail Sent ");
			structure.setData(custrepo.save(customer));
		}

		return structure;
	}

	public ResponseStructure<Customer> otpverify(int cust_id, int otp) throws MyException {

		ResponseStructure<Customer> structure = new ResponseStructure<>();

		Optional<Customer> optional = custrepo.findById(cust_id);
		if (optional.isEmpty()) {
//			structure.setCode(HttpStatus.NOT_FOUND.value());
			// by throwing exception
			throw new MyException("Check Id and Try Again");
		} else {
			Customer customer = optional.get();
			if (customer.getOtp() == otp) {
				structure.setCode(HttpStatus.CREATED.value());
				structure.setMessgae("Account Created Successfully");
				customer.setStatus(true);
				structure.setData(custrepo.save(customer));
			} else
				throw new MyException("OTP Invalid");
		}

		return structure;
	}

	public ResponseStructure<Customer> login(Login login) throws MyException {
		ResponseStructure<Customer> structure = new ResponseStructure<>();

		Optional<Customer> optional = custrepo.findById(login.getId());
		if (optional.isEmpty()) {
			throw new MyException("invalid customer id");
		} else {
			Customer customer = optional.get();
			if (customer.getPassword().equals(login.getPassword())) {
				if (customer.isStatus()) {

					structure.setCode(HttpStatus.ACCEPTED.value());
					structure.setMessgae("login sucessfully");
					structure.setData(customer);

				} else {
					throw new MyException("verify the your email");
				}

			} else {
				throw new MyException("invalid password");
			}
		}
		return structure;
	}

	public ResponseStructure<Customer> createAccount(int id, String type) throws MyException {
		ResponseStructure<Customer> structure = new ResponseStructure<Customer>();
		Optional<Customer> optional = custrepo.findById(id);
		if (optional.isEmpty()) {
			throw new MyException("check id and try again");
		} else {
			Customer customer = optional.get();
			List<BankAccount> list = customer.getAccounts();
			boolean flag = true;
			for (BankAccount account : list) {
				if (account.getType().equals(type)) {
					flag = false;
					break;
				}
			}
			if (!flag) {
				throw new MyException(type + "Account already exits");
			} else {
				accounts.setType(type);
				if (type.equals("savings")) {
					accounts.setBanklimit(50000);
				} else {
					accounts.setBanklimit(10000);
				}
				
				list.add(accounts);
				customer.setAccounts(list);
			}
			structure.setCode(HttpStatus.CREATED.value());
			structure.setMessgae("Account created wait for managemnt tp aprove");
			structure.setData(custrepo.save(customer));
			custrepo.save(customer);

		}

		return structure;
	}

	public ResponseStructure<List<BankAccount>> fetchAllTrue(int custid) throws MyException {

		ResponseStructure<List<BankAccount>> structure=new ResponseStructure<List<BankAccount>>();
		
	Optional<Customer>	optional=custrepo.findById(custid);
		Customer customer=optional.get();
		List<BankAccount> list=customer.getAccounts();
		List<BankAccount> res=new ArrayList<BankAccount>();
		
		for(BankAccount account:list)
		{
			if(account.isStatus())
			{
				res.add(account);
			}
		}
		if(res.isEmpty()) {
			throw new MyException("No Active Accounts");
		}
		else
		{
		structure.setCode(HttpStatus.FOUND.value());
		structure.setMessgae("Accounts Found");
		structure.setData(res);
		}
		return structure;
	}

	public ResponseStructure<Double> checkBalance(long acno) {
		
		ResponseStructure<Double> structure=new ResponseStructure<Double>();
		
	Optional<BankAccount>	optional=bankRepository.findById(acno);
		BankAccount account=optional.get();
		
		structure.setCode(HttpStatus.FOUND.value());
		structure.setMessgae("Data Found");
		structure.setData(account.getAmount());
		
		return structure;
	}

	public ResponseStructure<BankAccount> deposit(long acno, double amount) {

		ResponseStructure<BankAccount> structure=new ResponseStructure<BankAccount>();
		
		BankAccount account=bankRepository.findById(acno).get();
		account.setAmount(account.getAmount()+amount);;
		
		transactionrepo.setDatetime(LocalDateTime.now());
		transactionrepo.setDeposit(amount);
		transactionrepo.setBalance(account.getAmount());
		
		
		List<BankTransaction> transactions=account.getBankTransaction();
		transactions.add(transactionrepo);
		
		structure.setCode(HttpStatus.ACCEPTED.value());
		structure.setMessgae("Amount Added");
		structure.setData(bankRepository.save(account));
		
		return structure;
	}

	public ResponseStructure<BankAccount> withdraw(long acno, double amount) throws MyException {

		ResponseStructure<BankAccount> structure=new ResponseStructure<BankAccount>();
		
		BankAccount account=bankRepository.findById(acno).get();
		if(amount>account.getBanklimit())
		{
			throw new MyException("out of limit"); 
		}
		else
		{
		if(amount>account.getAmount())	
		{
			throw new MyException("Insufficent Funds");
		}
		else
		{
		account.setAmount(account.getAmount()-amount);
		
		transactionrepo.setDatetime(LocalDateTime.now());
		transactionrepo.setDeposit(0);
		transactionrepo.setWithdraw(amount);
		transactionrepo.setBalance(account.getAmount());
		
		
		List<BankTransaction> transactions=account.getBankTransaction();
		transactions.add(transactionrepo);
		
		structure.setCode(HttpStatus.ACCEPTED.value());
		structure.setMessgae("Amount withdraw Successfully");
		structure.setData(bankRepository.save(account));
		}
		}
		return structure;
	}

	public ResponseStructure<List<BankTransaction>> viewTransaction(long acno) throws MyException {
ResponseStructure<List<BankTransaction>> structure=new ResponseStructure<List<BankTransaction>>();
		
		BankAccount account=bankRepository.findById(acno).get();
		
		List<BankTransaction> list=account.getBankTransaction();
		if(list.isEmpty())
		{
			throw new MyException("No Transaction");
		}
		else
		{
			structure.setCode(HttpStatus.FOUND.value());
			structure.setMessgae("Data Found");
			structure.setData(list);
		}
		return structure;
	}

	
	
	
}








