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
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Beneficiary {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long beneficiaryAccountNumber;
	
	private String beneficiaryAccountName;
}
