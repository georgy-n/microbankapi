package com.baggage.service.impl;

import com.baggage.entity.FriendshipId;
import com.baggage.entity.dao.FriendshipDao;
import com.baggage.repository.FriendsRepository;
import com.baggage.service.FriendsService;
import com.baggage.utils.CustomError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FriendsServiceImpl implements FriendsService {
    @Autowired
    private FriendsRepository friendsRepository;

    public boolean hasFriendship(Integer userIdA, Integer userIdB) {
        Optional<FriendshipDao> record = friendsRepository.findById(new FriendshipId(userIdA, userIdB));
        Optional<FriendshipDao> permutatedRecord = friendsRepository.findById(new FriendshipId(userIdB, userIdA));
        return record.isPresent() || permutatedRecord.isPresent();
    }

    @Override
    public List<Integer> findAllFriendIdById(Integer userId) {
        List<FriendshipDao> friends = friendsRepository.findAllFriendIdByOwnerId(userId);
        return friends.stream().map(FriendshipDao::getRecipientId).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public FriendshipDao addFriend(Integer userIdA, Integer userIdB) throws CustomError {
        try {
            FriendshipDao friendshipAB = new FriendshipDao(userIdA, userIdB);
            FriendshipDao friendshipBA = new FriendshipDao(userIdB, userIdA);
            friendsRepository.save(friendshipAB);
            friendsRepository.save(friendshipBA);
            return friendshipAB;
        } catch (Exception e) {
            throw new CustomError("Add friendship is failed");
        }
    }

    @Transactional
    @Override
    public void deleteFriendship(Integer userIdA, Integer userIdB) throws CustomError {
        Optional<FriendshipDao> recordA = friendsRepository.findByOwnerIdAndRecipientId(userIdA, userIdB);
        Optional<FriendshipDao> recordB = friendsRepository.findByOwnerIdAndRecipientId(userIdB, userIdA);

        if (recordA.isPresent() && recordB.isPresent()) {
            friendsRepository.delete(recordA.get());
            friendsRepository.delete(recordB.get());
        } else throw new CustomError("not consistent frindship " + userIdA + " " + userIdB);
    }
}
