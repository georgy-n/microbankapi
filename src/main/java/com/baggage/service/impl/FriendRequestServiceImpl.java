package com.baggage.service.impl;

import com.baggage.entity.dao.FriendRequestDao;
import com.baggage.repository.FriendRequestRepository;
import com.baggage.service.FriendRequestService;
import com.baggage.utils.CustomError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FriendRequestServiceImpl implements FriendRequestService {

    @Autowired
    private FriendRequestRepository friendRequestRepository;

    public FriendRequestDao saveFriendshipRequest(Integer userIdOwner, Integer userIdRecipient) throws CustomError {
        try {
            FriendRequestDao req = new FriendRequestDao(userIdOwner, userIdRecipient);
            friendRequestRepository.save(req);
            return req;
        }
        catch (Exception e) {
            throw new CustomError("Add friend request failed");
        }
    }

    public List<Integer> findAllRequestsByUserId(Integer userId) {
        List<FriendRequestDao> reqs = friendRequestRepository.findAllById(userId);
        return reqs.stream().map(FriendRequestDao::getSenderId).collect(Collectors.toList());
    }

    public void deleteFriendRequest(Integer requestId) throws CustomError {
        try {
            friendRequestRepository.deleteById(requestId);
        }
        catch (Exception e) {
            throw new CustomError("delete friend resuest failed " + requestId);
        }
    }

    public Optional<FriendRequestDao> findByRequestId(Integer id) {
        return friendRequestRepository.findById(id);
    }
}
