package com.org.serviceclient;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class AccountController {
	
	@Autowired
	private RestTemplate restTemplate;
	
	@RequestMapping(value = "/getList")
	public AccountList getAccountList(){
		List<Account> list = new ArrayList<>();
		Account a1 = new Account(12345, "12", 1000000, "John", 45678, "Savings");
		Account a2 = new Account(12346, "13", 10000, "Amith", 45608, "Checkings");
		Account a3 = new Account(12389, "22", 100000000, "Shah", 45276, "Savings");
		list.add(a1);
		list.add(a2);
		list.add(a3);
		AccountList accountList = new AccountList();
		accountList.setAccounts(list);
		return accountList;
	}
	
	@RequestMapping(value = "/getCreditCard")
	public CreditCard getCreditCard() {
		CreditCard card = new CreditCard(12346, 45608, 3000, 3000, "2020-12-12");
		return card;
	}
	
	@PostMapping(value = "/creditCardDetailsAfterTransaction")
	public CreditCard saveCreditCardFromTransaction(@RequestBody CreditCard card) {
		CreditCard card1 = getCreditCard();
		card1.setAvailableBalance(card.getAvailableBalance());
		return card1;
	}
	
	
	
	
}
