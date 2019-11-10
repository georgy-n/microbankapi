package com.baggage.service;

import com.baggage.entity.dao.FriendshipDao;
import com.baggage.utils.CustomError;

import java.util.List;

public interface FriendsService {
    boolean hasFriendship(Integer userIdA, Integer userIdB);
    List<Integer> findAllFriendIdById(Integer userId);
    FriendshipDao addFriend(Integer userIdA, Integer userIdB) throws CustomError;
    void deleteFriendship(Integer userIdA, Integer userIdB) throws CustomError;
}
