package com.baggage.entity.httpRequests;

public class CreateBankAccountRequest {

	private Integer currency;
	private String name;
	private Double balance;

	public CreateBankAccountRequest() {

	}

	public Integer getCurrency() {
		return currency;
	}

	public void setCurrency(Integer currency) {
		this.currency = currency;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getBalance() {
		return balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}

	public CreateBankAccountRequest(Integer currency, String name, Double balance) {
		this.currency = currency;
		this.name = name;
		this.balance = balance;
	}
}
