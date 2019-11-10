package com.baggage.entity.dao;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name = "bank_accounts")
public class BankAccountDao {
    @Id
    @JsonIgnore
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    public Integer getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Integer ownerId) {
        this.ownerId = ownerId;
    }

    @Column(name = "number")
    private Long number;

    @Column(name = "name")
    private String name;

    @Column(name = "ownerid")
    private Integer ownerId;

    @Column(name = "balance")
    private Double balance;

    @Column(name = "currency")
    private Integer currency;

    public BankAccountDao() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
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

    public void plusBalance(Double amount) {this.balance += amount; }
    public void minusBalance(Double amount) {this.balance -= amount; }

    public Integer getCurrency() {
        return currency;
    }

    public void setCurrency(Integer currency) {
        this.currency = currency;
    }

    public BankAccountDao(Long number, String name, Integer ownerId, Double balance, Integer currency) {
        this.number = number;
        this.name = name;
        this.ownerId = ownerId;
        this.balance = balance;
        this.currency = currency;
    }
}