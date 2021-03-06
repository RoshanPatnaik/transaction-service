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

import com.org.Transaction.dao.TransactionDao;
import com.org.Transaction.exception.AccountNotExistsException;
import com.org.Transaction.exception.BeneficiaryNotExistsException;
import com.org.Transaction.exception.CreditLimitExceededException;
import com.org.Transaction.exception.InsufficientBalanceException;
import com.org.Transaction.model.Account;
import com.org.Transaction.model.AccountList;
import com.org.Transaction.model.Beneficiary;
import com.org.Transaction.model.CreditCard;
import com.org.Transaction.model.Transaction;

import net.bytebuddy.asm.Advice.Local;

@Service
public class TransactionServiceImpl implements TransactionService{

	@Autowired
	private TransactionDao dao;

	@Autowired
	private RestTemplate restTemplate;

	public List<Transaction> getTransactionsWithFilters(String userId, LocalDate date, String type, LocalDate startDate,
			LocalDate endDate) {
		List<Transaction> list = dao.getAllTransactions().stream().filter(transaction -> transaction.getUserId().equals(userId)).collect(Collectors.toList());
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

	public List<Transaction> getAllTransactions(String userId) {
		return dao.getAllTransactions().stream().filter(transaction -> transaction.getUserId().equals(userId)).collect(Collectors.toList());
	}

	public void addTransaction(String userId, long accountNo, long beneficiaryAccountNumber, double amount,
			String transactionType) throws CreditLimitExceededException, BeneficiaryNotExistsException, AccountNotExistsException, InsufficientBalanceException {
		// get all accounts for the user Id from Accounts
		// Account
		// account=rest.getForObject("http://ACCOUNTS-SERVICE/.....",Account.class);

		List<Account> accounts = new ArrayList();
		AccountList list = restTemplate.getForObject("http://DUMMY-CLIENT/getList", AccountList.class);
		accounts = list.getAccounts();
		Transaction transaction=null;
		int flag=0;
		for (Account account : accounts) {
			if (account.getAccountNo() == accountNo && account.getUserId().equals(userId)) {
				flag=1;
				if(beneficiaryExists(beneficiaryAccountNumber)) {
					if(transactionType.equals("credit")) {
						CreditCard card = restTemplate.getForObject("http://DUMMY-CLIENT/getCreditCard/"+account.getCreditCardNumber(), CreditCard.class);
						System.out.println(card.getCreditCardNumber());
						if(amount <= card.getAvailableBalance()) {
							card.setAvailableBalance(card.getAvailableBalance()-amount);
							transaction = new Transaction(userId, amount, LocalDate.now(), transactionType,beneficiaryAccountNumber, accountNo);
							restTemplate.postForObject("http://DUMMY-CLIENT/saveCreditCardDetailsAfterTransaction", card, CreditCard.class);
							dao.saveTransaction(transaction);
						}
						else {
							//credit limit exceeded
							throw new CreditLimitExceededException("Credit Limit Exceeded");
						}
					}
						
					else if(account.getAccountBal()>=amount){
						account.setAccountBal(account.getAccountBal()-amount);
						transaction = new Transaction(userId, amount, LocalDate.now(), transactionType,beneficiaryAccountNumber, accountNo);
						dao.saveTransaction(transaction);
					}
					else {
						throw new InsufficientBalanceException("Insufficient Balance");
					}
				}
				else{
					throw new BeneficiaryNotExistsException("Beneficiary does not exist");
				}

			}
			

		}
		if(flag==0) {
			throw new AccountNotExistsException("Account Does Not exist");
		}
		else {
			System.out.println(transaction);
		}
	}

	public boolean beneficiaryExists(long beneficiaryAccountNumber) {
		if (dao.getBeneficiary(beneficiaryAccountNumber) != 0) {
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

//	public void getCreditCard() {
//		AccountList list = restTemplate.getForObject("http://DUMMY-CLIENT/getList", AccountList.class);
//		System.out.println(list.getAccounts());
//		CreditCard card = restTemplate.getForObject("http://DUMMY-CLIENT/getCreditCard", CreditCard.class);
//	}
	

}
