package com.org.Transaction.controller;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.annotation.JsonValue;
import com.org.Transaction.exception.AccountNotExistsException;
import com.org.Transaction.exception.BeneficiaryNotExistsException;
import com.org.Transaction.exception.CreditLimitExceededException;
import com.org.Transaction.exception.InsufficientBalanceException;
import com.org.Transaction.model.CreditCard;
import com.org.Transaction.model.Transaction;
import com.org.Transaction.service.TransactionService;

@RestController
@RequestMapping("/transaction")
public class TransactionController {

	@Autowired
	private TransactionService service;

	@GetMapping(value = "add/{userId}/{accountNo}/{beneficiaryAccountNumber}/{amount}/{transactionType}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> addTransaction(@PathVariable("userId") String userId, @PathVariable("accountNo") long accountNo,@PathVariable("beneficiaryAccountNumber") long beneficiaryAccountNumber, @PathVariable("amount") double amount,@PathVariable("transactionType") String transactionType) {	
		ResponseEntity<Object> response = null;
		try {
			service.addTransaction(userId,accountNo,beneficiaryAccountNumber,amount,transactionType);
			response = ResponseEntity.status(200).body("Transaction Successful");
		} catch (CreditLimitExceededException | BeneficiaryNotExistsException | AccountNotExistsException | InsufficientBalanceException e) {
			response = ResponseEntity.status(404).body(e.getMessage());
		}
		return response;
	}

	@RequestMapping(value = "/filters", produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
	public List<Transaction> getTransactionsWithFilters(@RequestParam("date") @Nullable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date, @RequestParam("type") @Nullable String type,@RequestParam("startDate") @Nullable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate, @RequestParam("endDate") @Nullable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate){
		List<Transaction> list = service.getTransactionsWithFilters(date, type, startDate, endDate);
		return list;
	}
	@GetMapping(value= "/getall/{account}", produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
	public List<Transaction> getList(@PathVariable("account") long accountNo){
		return service.getAllTransactionsUsingAccountNumber(accountNo);
	}
	@GetMapping(value="/getfive/{account}", produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
	public List<Transaction> getLastFive(@PathVariable("account") long account) {
		return service.getLastFiveTransactions(account);
	}
	
	@GetMapping(value = "/lastfive/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Transaction> lastFive(@PathVariable String userId){
		return service.getLastFive(userId);
	}
	
//	@GetMapping(value = "/showCreditCard")
//	public CreditCard getCreditCard() {
//		return service.getCreditCard();
//	}
	
	


}
