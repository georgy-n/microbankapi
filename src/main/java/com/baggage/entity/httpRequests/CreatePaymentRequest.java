package com.baggage.entity.httpRequests;

public class CreatePaymentRequest {

	private Integer currency;
	private Integer bankAccountFrom;
	private Integer bankAccountTo;
	private Double amount;

	public CreatePaymentRequest() {

	}

	public Integer getCurrency() {
		return currency;
	}

	public void setCurrency(Integer currency) {
		this.currency = currency;
	}

	public Integer getBankAccountFrom() {
		return bankAccountFrom;
	}

	public void setBankAccountFrom(Integer bankAccountFrom) {
		this.bankAccountFrom = bankAccountFrom;
	}

	public Integer getBankAccountTo() {
		return bankAccountTo;
	}

	public void setBankAccountTo(Integer bankAccountTo) {
		this.bankAccountTo = bankAccountTo;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public CreatePaymentRequest(Integer currency, Integer bankAccountFrom, Integer bankAccountTo, Double amount) {
		this.currency = currency;
		this.bankAccountFrom = bankAccountFrom;
		this.bankAccountTo = bankAccountTo;
		this.amount = amount;
	}
}
