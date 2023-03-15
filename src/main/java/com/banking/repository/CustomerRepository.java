package com.banking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.banking.entity.Customer;
import com.banking.helper.ResponseStructure;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {

	
	
}
