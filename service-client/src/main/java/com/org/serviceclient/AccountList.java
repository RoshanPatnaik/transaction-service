package com.org.serviceclient;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@Setter
@ToString
public class AccountList {
	private List<Account> accounts;
	public AccountList() {
		accounts = new ArrayList<>();
	}
}
