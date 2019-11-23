package com.baggage.service.impl;

import com.baggage.entity.dao.BankAccountDao;
import com.baggage.entity.dao.PaymentRequestDao;
import com.baggage.repository.BankAccountRepository;
import com.baggage.repository.PaymentRequestRepository;
import com.baggage.service.BankAccountsService;
import com.baggage.service.PaymentRequestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("paymentRequestService")
public class PaymentRequestServiceImpl implements PaymentRequestService {

    private Logger logger = LoggerFactory.getLogger(PaymentRequestServiceImpl.class);
    private final BankAccountsService bankAccountsService;
    private final PaymentRequestRepository paymentRequestRepository;

    public PaymentRequestServiceImpl(BankAccountsService bankAccountsService,
                                     PaymentRequestRepository paymentRequestRepository) {
        this.bankAccountsService = bankAccountsService;
        this.paymentRequestRepository = paymentRequestRepository;
    }

    @Override
    public PaymentRequestDao savePaymentRequest(PaymentRequestDao paymentRequestDao) {
        return paymentRequestRepository.saveAndFlush(paymentRequestDao);
    }

    @Override
    public List<PaymentRequestDao> findAllByBankAccountId(Integer bankAccountId) {
        return paymentRequestRepository.findAllByIdBankAccountFromOrIdBankAccountTo(bankAccountId, bankAccountId);
    }

    @Override
    public Optional<PaymentRequestDao> findByPaymentRequestId(Integer paymentRequestId) {
        return paymentRequestRepository.findById(paymentRequestId);
    }

}
