package com.baggage.repository;

import com.baggage.entity.dao.BankAccountDao;
import com.baggage.entity.dao.PaymentRequestDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRequestRepository extends JpaRepository<PaymentRequestDao, Integer> {
    Optional<PaymentRequestDao> findById(Integer paymentRequestId);
    List<PaymentRequestDao> findAllByIdBankAccountFromOrIdBankAccountTo(Integer idBankAccountFrom, Integer idBankAccountTo);
}