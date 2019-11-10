package com.baggage.repository;

import com.baggage.entity.dao.BankAccountDao;
import com.baggage.entity.dao.ClientDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccountDao, Integer> {
    Optional<BankAccountDao> findById(Integer bankAccountId);
    List<BankAccountDao> findAllByOwnerId(Integer ownerId);
}