package com.baggage.repository;

import com.baggage.entity.FriendshipId;
import com.baggage.entity.dao.FriendshipDao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FriendsRepository extends JpaRepository<FriendshipDao, FriendshipId> {
    List<FriendshipDao> findAllFriendIdByOwnerId(Integer ownerId);
    Optional<FriendshipDao> findByOwnerIdAndRecipientId(Integer ownerId, Integer recipientId);
}
