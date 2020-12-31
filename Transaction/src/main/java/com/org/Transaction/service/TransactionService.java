package com.org.Transaction.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.org.Transaction.dao.TransactionDaoImpl;
import com.org.Transaction.model.Account;
import com.org.Transaction.model.Beneficiary;
import com.org.Transaction.model.CreditCard;
import com.org.Transaction.model.Transaction;

import net.bytebuddy.asm.Advice.Local;

@Service
public class TransactionService {

	@Autowired
	private TransactionDaoImpl dao;

	@Autowired
	private RestTemplate restTemplate;

	public List<Transaction> getTransactionsWithFilters(LocalDate date, String type, LocalDate startDate,
			LocalDate endDate) {
		List<Transaction> list = dao.getAllTransactions();
		if (date != null) {
			list = list.stream().filter(transaction -> transaction.getTransactionDate().equals(date))
					.collect(Collectors.toList());
		}
		if (!type.equals("")) {
			list = list.stream().filter(transaction -> transaction.getTransactionType().equals(type))
					.collect(Collectors.toList());
		}
		if (startDate != null && endDate != null) {
			list = list.stream().filter(transaction -> transaction.getTransactionDate().isAfter(startDate)
					&& transaction.getTransactionDate().isBefore(endDate)).collect(Collectors.toList());
		}
		return list;
	}

	public List<Transaction> getAllTransactions() {
		return dao.getAllTransactions();
	}

	public void addTransaction(String userId, long accountNo, long beneficiaryAccountNumber, double amount,
			String transactionType) {
		// get all accounts for the user Id from Accounts
		// Account
		// account=rest.getForObject("http://ACCOUNTS-SERVICE/.....",Account.class);

		List<Account> accounts = new ArrayList();
		for (Account account : accounts) {
			if (account.getAccountNo() == accountNo) {
				if(beneficiaryExists(beneficiaryAccountNumber)) {
					if(account.getAccountBal() < amount) {
						if(transactionType.equals("credit")) {
							//CreditCard card = rest.getForObject("http://ACCOUNTS-SERVICE/.....",Credit.class);
							CreditCard card = new CreditCard();
							if(amount <= card.getAvailableBalance()) {
								card.setAvailableBalance(card.getAvailableBalance()-amount);
								Transaction transaction = new Transaction(userId, amount, LocalDate.now(), transactionType,beneficiaryAccountNumber, accountNo);
								dao.saveTransaction(transaction);
							}
						}
						else {
							//insufficient balance
						}
					}
					else {
						Transaction transaction = new Transaction(userId, amount, LocalDate.now(), transactionType,beneficiaryAccountNumber, accountNo);
						dao.saveTransaction(transaction);
					}
				}
				else {
					//beneficiary does not exist
				}

			}

		}
	}

	public boolean beneficiaryExists(long beneficiaryAccountNumber) {
		if (dao.getBeneficiary(beneficiaryAccountNumber) != null) {
			return true;
		}
		return false;
	}
	public List<Transaction> getAllTransactions(String accountNumber){
		return dao.getAllTransactions().stream().filter(t -> t.getAccountNumber().equals(accountNumber)).collect(Collectors.toList());
	}
	public List<Transaction> getLastFiveTransactions(String accountNumber){
		return dao.getAllTransactions().stream().filter(t -> t.getAccountNumber().equals(accountNumber)).sorted(Comparator.comparing(Transaction::getTransactionDate).reversed()).limit(5).collect(Collectors.toList());

	}

}
