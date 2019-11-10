package com.baggage.service;


import com.baggage.entity.dao.BankAccountDao;
import com.baggage.entity.dao.ClientDao;

import java.util.List;
import java.util.Optional;

public interface BankAccountsService {
    List<BankAccountDao> findAllBankAccountsByUserId(Integer userId);
    BankAccountDao save(BankAccountDao bankAccountDao);
    Optional<BankAccountDao> findByBankAccountId(Integer bankAccountId);
    void deleteBankAccountById(Integer bankAccoutnId);
}
