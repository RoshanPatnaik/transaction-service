package com.org.Transaction.model;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.springframework.data.annotation.Reference;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Transaction {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long transactionId;
	private String userId;
	private double transactionAmount;
	private LocalDate transactionDate;
	private String transactionType;
	private long beneficiaryAccountNumber;
	private long accountNumber;
	public Transaction(String userId, double transactionAmount, LocalDate transactionDate,
			String transactionType, long beneficiaryAccountNumber, long accountNumber) {
		super();
		this.userId = userId;
		this.transactionAmount = transactionAmount;
		this.transactionDate = transactionDate;
		this.transactionType = transactionType;
		this.beneficiaryAccountNumber = beneficiaryAccountNumber;
		this.accountNumber = accountNumber;
	}
	
	
	
}
