package com.org.Transaction.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Account {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private double accountNo;
	
	private double userId;
	private double accountBal;
	private String accountNickName;
	private double creditCardNumber;
	private String accountType;
}
