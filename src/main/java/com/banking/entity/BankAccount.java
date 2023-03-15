package com.banking.entity;

import java.util.List;

import org.springframework.stereotype.Component;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import lombok.Data;

@Entity
@Data
@Component
public class BankAccount {

	@Id
	@SequenceGenerator(name="ac_id",initialValue = 1002121200,allocationSize = 1)
	@GeneratedValue(generator = "ac_id")
	long accnumber;
	String type;
	double banklimit;
	double amount;
	boolean status;
	
	@OneToMany(cascade = CascadeType.ALL)
	List< BankTransaction> bankTransaction;
	}

