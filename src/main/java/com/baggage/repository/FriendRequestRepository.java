package com.baggage.repository;

import com.baggage.entity.dao.FriendRequestDao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FriendRequestRepository extends JpaRepository<FriendRequestDao, Integer> {
    List<FriendRequestDao> findAllByRecipientId(Integer idTo);
}
