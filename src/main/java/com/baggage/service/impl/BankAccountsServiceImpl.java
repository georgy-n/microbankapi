package com.baggage.service.impl;

import com.baggage.entity.dao.BankAccountDao;
import com.baggage.entity.dao.ClientDao;
import com.baggage.repository.BankAccountRepository;
import com.baggage.repository.ClientRepository;
import com.baggage.service.BankAccountsService;
import com.baggage.service.ClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service("bankAccountsService")
public class BankAccountsServiceImpl implements BankAccountsService {

    private Logger logger = LoggerFactory.getLogger(BankAccountsServiceImpl.class);

    @Autowired
    BankAccountRepository bankAccountRepository;

    @Override
    public List<BankAccountDao> findAllBankAccountsByUserId(Integer userId) {
        return bankAccountRepository.findAllByOwnerId(userId);
    }

    @Override
    public BankAccountDao save(BankAccountDao bankAccountDao) {
        return bankAccountRepository.save(bankAccountDao);
    }

    @Override
    public Optional<BankAccountDao> findByBankAccountId(Integer bankAccountId) {
        return bankAccountRepository.findById(bankAccountId);
    }

    @Override
    public void deleteBankAccountById(Integer bankAccoutnId) {
        bankAccountRepository.deleteById(bankAccoutnId);
    }
}
