package com.org.Transaction.dao;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.org.Transaction.model.Beneficiary;
import com.org.Transaction.model.Transaction;
import com.org.Transaction.repository.BeneficiaryRepository;
import com.org.Transaction.repository.TransactionRepository;

@Component
public class TransactionDaoImpl {

	@Autowired
	private TransactionRepository repo;
	
	@Autowired
	private BeneficiaryRepository beneficiaryRepo;
	
	public List<Transaction> getAllTransactions(){
		return repo.findAll();
	}
	
	public void saveTransactions(List<Transaction> list) {
		System.out.println(list);
		list.forEach(transaction -> repo.save(transaction));

	}
	public void saveTransaction(Transaction transaction) {
		repo.save(transaction);
	}
	
	public Beneficiary getBeneficiary(Long beneficiaryAccNo) {
		return beneficiaryRepo.findById(beneficiaryAccNo).get();
	}

	
}
