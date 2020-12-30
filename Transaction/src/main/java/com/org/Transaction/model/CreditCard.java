package com.org.Transaction.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class CreditCard {
	
	private long accountNumber;
	private long creditCardNumber;
	private double creditLimit;
	private double availableBalance;
	private String paymentDueDate;
	
}
