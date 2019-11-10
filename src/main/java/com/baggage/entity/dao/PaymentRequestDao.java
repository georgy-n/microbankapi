package com.baggage.entity.dao;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name = "payment_requests")
public class PaymentRequestDao {
    @Id
    @JsonIgnore
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "id_bank_account_from")
    private Integer idBankAccountFrom;

    @Column(name = "id_bank_account_to")
    private Integer idBankAccountTo;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "currency")
    private Integer currency;

    @Column(name = "status")
    private String status;

    public PaymentRequestDao() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdBankAccountFrom() {
        return idBankAccountFrom;
    }

    public void setIdBankAccountFrom(Integer idBankAccountFrom) {
        this.idBankAccountFrom = idBankAccountFrom;
    }

    public Integer getIdBankAccountTo() {
        return idBankAccountTo;
    }

    public void setIdBankAccountTo(Integer idBankAccountTo) {
        this.idBankAccountTo = idBankAccountTo;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Integer getCurrency() {
        return currency;
    }

    public void setCurrency(Integer currency) {
        this.currency = currency;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public PaymentRequestDao(Integer idBankAccountFrom, Integer idBankAccountTo, Double amount, Integer currency, String status) {
        this.idBankAccountFrom = idBankAccountFrom;
        this.idBankAccountTo = idBankAccountTo;
        this.amount = amount;
        this.currency = currency;
        this.status = status;
    }
}