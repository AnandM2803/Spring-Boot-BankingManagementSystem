package com.banking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.banking.entity.BankAccount;

@Repository
public interface BankRepository extends JpaRepository<BankAccount, Long>{

}
