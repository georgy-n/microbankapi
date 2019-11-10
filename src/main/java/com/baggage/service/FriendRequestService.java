package com.baggage.service;

import com.baggage.entity.dao.FriendRequestDao;
import com.baggage.utils.CustomError;

import java.util.List;
import java.util.Optional;

public interface FriendRequestService {
    FriendRequestDao saveFriendshipRequest(Integer userIdOwner, Integer userIdRecipient) throws CustomError;
    List<Integer> findAllRequestsByUserId(Integer userId);
    void deleteFriendRequest(Integer requestId) throws CustomError;
    Optional<FriendRequestDao> findByRequestId(Integer requestId);
}
