package com.baggage.service.impl;

import com.baggage.entity.dao.ClientDao;
import com.baggage.repository.ClientRepository;
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

@Service("clientService")
public class ClientServiceImpl implements ClientService {

    private Logger logger = LoggerFactory.getLogger(ClientServiceImpl.class);

    @Autowired
    ClientRepository clientRepository;

    @Override
    @Transactional(readOnly = true)
    public Optional<ClientDao> findByUsername(String userName) {
        try {
            return Optional.ofNullable(clientRepository.findByUserName(userName));
        } catch (Exception e) {
            logger.error(e.getMessage());
            return Optional.empty();
        }
    }

    //TODO i think throw it is bad
    public ClientDao save(ClientDao clientDao) {
        try {
            return clientRepository.save(clientDao);
        } catch (Exception e) {
            logger.error("Failed save to database", e.getMessage());
            throw e;
        }
    }

    //TODO unsafe huerga
    @Override
    public List<String> findLoginsByIds(List<Integer> ids) {
        try {
            List<ClientDao> clientDaos = clientRepository.findAllByIdContaining(ids);
            return clientDaos.stream().map(ClientDao::getLogin).collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("cant take logins by ids " + ids);
            return new ArrayList<>();
        }
    }

    @Override
    public Optional<ClientDao> findById(Integer id) {
        return clientRepository.findById(id);
    }
}
