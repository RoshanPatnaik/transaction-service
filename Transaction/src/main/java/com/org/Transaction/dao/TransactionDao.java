package com.org.Transaction.dao;

import java.util.List;

import com.org.Transaction.model.Transaction;

public interface TransactionDao {
	
	public List<Transaction> getAllTransactions();
	public void saveTransactions(List<Transaction> list);
	public void saveTransaction(Transaction transaction);
	public int getBeneficiary(Long beneficiaryAccNo);
}
