package com.baggage.service;


import com.baggage.entity.dao.ClientDao;
import javafx.util.Pair;

import java.util.List;
import java.util.Optional;

public interface ClientService {
    Optional<ClientDao> findByUsername(String userName);
    ClientDao save(ClientDao clientDao);
    List<String> findLoginsByIds(List<Integer> ids);
    List<Pair<Integer, String>> findLoginsByIdsAndGetPairs(List<Integer> ids);
    Optional<ClientDao> findById(Integer id);
}
