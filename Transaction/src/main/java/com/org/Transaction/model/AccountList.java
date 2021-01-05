package com.org.Transaction.model;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


public class AccountList {
	private List<Account> accounts;
	
	public AccountList() {
		accounts = new ArrayList<>();
	}

	public AccountList(List<Account> accounts) {
		super();
		this.accounts = accounts;
	}

	public List<Account> getAccounts() {
		return accounts;
	}

	public void setAccounts(List<Account> accounts) {
		this.accounts = accounts;
	}

	@Override
	public String toString() {
		return "AccountList [accounts=" + accounts + "]";
	}
	
}
