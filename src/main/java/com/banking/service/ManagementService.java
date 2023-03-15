package com.banking.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.banking.entity.BankAccount;
import com.banking.entity.Customer;
import com.banking.entity.Management;
import com.banking.exception.MyException;
import com.banking.helper.ResponseStructure;
import com.banking.repository.BankRepository;
import com.banking.repository.ManagementRepository;

@Service
public class ManagementService {

	@Autowired
	ManagementRepository mngrepo;

	@Autowired
	BankRepository bankRepository;

	public ResponseStructure<Management> addDetails(Management management) {

		ResponseStructure<Management> structure = new ResponseStructure<Management>();

		structure.setCode(HttpStatus.CREATED.value());
		structure.setMessgae("Account Created Successfully");
		structure.setData(mngrepo.save(management));
		return structure;
	}

	public ResponseStructure<Management> login(Management management) throws MyException {
		ResponseStructure<Management> structure = new ResponseStructure<>();

		Management management1 = mngrepo.findByEmail(management.getEmail());
		if (management1 == null) {
			throw new MyException("invalid management email");
		} else {

			if (management1.getPassword().equals(management.getPassword())) {

				structure.setCode(HttpStatus.ACCEPTED.value());
				structure.setMessgae("login sucessfully");
				structure.setData(management);

			}

			else {
				throw new MyException("invalid password");
			}
		}
		return structure;
	}

	public ResponseStructure<List<BankAccount>> fetchAllAccounts() throws MyException {

		ResponseStructure<List<BankAccount>> structure = new ResponseStructure<List<BankAccount>>();

		List<BankAccount> list = bankRepository.findAll();
		if (list.isEmpty()) {
			throw new MyException("No Accounts Present");
		} else {
			structure.setCode(HttpStatus.FOUND.value());
			structure.setMessgae("Data found");
			structure.setData(list);
		}
		return structure;
	}

	public ResponseStructure<BankAccount> changeStatus(long acno) {

		ResponseStructure<BankAccount> structure = new ResponseStructure<BankAccount>();

		Optional<BankAccount> optional = bankRepository.findById(acno);
		BankAccount account = optional.get();

		if (account.isStatus()) {
			account.setStatus(false);
		} else {
			account.setStatus(true);
		}
		structure.setCode(HttpStatus.OK.value());
		structure.setMessgae("Status is Changed");
		structure.setData(bankRepository.save(account));
		return structure;
	}

}
