package com.baggage.service;


import com.baggage.entity.dao.ClientDao;

import java.util.List;
import java.util.Optional;

public interface ClientService {
    Optional<ClientDao> findByUsername(String userName);
    ClientDao save(ClientDao clientDao);
    List<String> findLoginsByIds(List<Integer> ids);
    Optional<ClientDao> findById(Integer id);
}
