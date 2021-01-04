package com.org.Transaction.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.org.Transaction.dao.TransactionDaoImpl;
import com.org.Transaction.model.Account;
import com.org.Transaction.model.AccountList;
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
		AccountList list = restTemplate.getForObject("http://DUMMY-CLIENT/getList", AccountList.class);
		accounts = list.getAccounts();
		Transaction transaction=null;
		int flag=0;
		for (Account account : accounts) {
			if (account.getAccountNo() == accountNo) {
				flag=1;
				if(beneficiaryExists(beneficiaryAccountNumber)) {
					if(transactionType.equals("credit")) {
						CreditCard card = restTemplate.getForObject("http://DUMMY-CLIENT/getCreditCard", CreditCard.class);
						if(amount <= card.getAvailableBalance()) {
							card.setAvailableBalance(card.getAvailableBalance()-amount);
							transaction = new Transaction(userId, amount, LocalDate.now(), transactionType,beneficiaryAccountNumber, accountNo);
							restTemplate.postForObject("http://DUMMY-CLIENT/creditCardDetailsAfterTransaction", card, CreditCard.class);
							dao.saveTransaction(transaction);
						}
						else {
							//credit limit exceeded
							System.out.println("Credit Limit Exceeded");
							break;
						}
					}
						
					else if(account.getAccountBal()>=amount){
						account.setAccountBal(account.getAccountBal()-amount);
						transaction = new Transaction(userId, amount, LocalDate.now(), transactionType,beneficiaryAccountNumber, accountNo);
						dao.saveTransaction(transaction);
					}
				}
				else {
					System.out.println("Beneficiary Does not exist");
					break;
				}

			}

		}
		if(flag==0) {
			System.out.println("Account number Does Not exist");
		}
		else {
			System.out.println(transaction);
		}
	}

	public boolean beneficiaryExists(long beneficiaryAccountNumber) {
		if (dao.getBeneficiary(beneficiaryAccountNumber) != null) {
			return true;
		}
		return false;
	}

	public List<Transaction> getAllTransactionsUsingAccountNumber(long accountNumber) {
		return dao.getAllTransactions().stream().filter(t -> t.getAccountNumber() == accountNumber)
				.collect(Collectors.toList());
	}
	
	
	public List<Transaction> getLastFive(String userId){
		return dao.getAllTransactions().stream().filter(transaction -> transaction.getUserId().equals(userId)).collect(Collectors.toList());
	}

	public List<Transaction> getLastFiveTransactions(long accountNumber) {
		return dao.getAllTransactions().stream().filter(t -> t.getAccountNumber() == accountNumber)
				.sorted(Comparator.comparing(Transaction::getTransactionDate).reversed()).limit(5)
				.collect(Collectors.toList());

	}

	public void getCreditCard() {
		AccountList list = restTemplate.getForObject("http://DUMMY-CLIENT/getList", AccountList.class);
		System.out.println(list.getAccounts());
		CreditCard card = restTemplate.getForObject("http://DUMMY-CLIENT/getCreditCard", CreditCard.class);
	}

}
