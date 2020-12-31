package com.org.serviceclient;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Account {
	private long accountNo;
	
	private String userId;
	private double accountBal;
	private String accountNickName;
	private long creditCardNumber;
	private String accountType;
}
