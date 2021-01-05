package com.org.Transaction.service;

import java.time.LocalDate;
import java.util.List;

import com.org.Transaction.exception.AccountNotExistsException;
import com.org.Transaction.exception.BeneficiaryNotExistsException;
import com.org.Transaction.exception.CreditLimitExceededException;
import com.org.Transaction.exception.InsufficientBalanceException;
import com.org.Transaction.model.Transaction;

public interface TransactionService {
	public List<Transaction> getTransactionsWithFilters(String userId, LocalDate date, String type, LocalDate startDate,LocalDate endDate);
	public List<Transaction> getAllTransactions(String userId);
	public void addTransaction(String userId, long accountNo, long beneficiaryAccountNumber, double amount, String transactionType) throws CreditLimitExceededException, BeneficiaryNotExistsException, AccountNotExistsException, InsufficientBalanceException;
	public boolean beneficiaryExists(long beneficiaryAccountNumber);
	public List<Transaction> getAllTransactionsUsingAccountNumber(long accountNumber);
	public List<Transaction> getLastFive(String userId);
	public List<Transaction> getLastFiveTransactions(long accountNumber);
}
