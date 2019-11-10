package com.baggage.repository;

import com.baggage.entity.dao.ClientDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientRepository extends JpaRepository<ClientDao, Integer> {
    ClientDao findByUserName(String username);

    List<ClientDao> findAllByIdContaining(List<Integer> ids);

    List<ClientDao> findByIdIn(List<Integer> ids);
}