package com.banking.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.banking.entity.Customer;
import com.banking.entity.Management;

@Repository
public interface ManagementRepository extends JpaRepository<Management, Integer>{

	Management findByEmail(String email);

	
}
