package com.baggage.service;


import com.baggage.entity.dao.BankAccountDao;
import com.baggage.entity.dao.PaymentRequestDao;

import java.util.List;
import java.util.Optional;

public interface PaymentRequestService {
    PaymentRequestDao savePaymentRequest(PaymentRequestDao paymentRequestDao);
    List<PaymentRequestDao> findByBankAccountId(Integer bankAccountId);
    Optional<PaymentRequestDao> findByPaymentRequestId(Integer paymentRequestId);
}
