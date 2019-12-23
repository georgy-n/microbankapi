package com.baggage.controllers;

import com.baggage.entity.CustomResponse;
import com.baggage.entity.dao.ClientDao;
import com.baggage.entity.dao.FriendRequestDao;
import com.baggage.entity.httpRequests.FriendRequest;
import com.baggage.service.ClientService;
import com.baggage.service.FriendRequestService;
import com.baggage.service.FriendsService;
import com.baggage.utils.CustomError;
import com.baggage.utils.TokenUtil;
import javafx.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static com.baggage.utils.Constants.*;

@RestController
@RequestMapping("/api/friends")
public class FriendController {

    private FriendRequestService friendRequestService;
    private FriendsService friendsService;
    private ClientService clientService;
    private Logger logger = LoggerFactory.getLogger(FriendController.class);

    @Autowired
    public FriendController(ClientService clientService, FriendRequestService friendRequestService,
                            FriendsService friendsService) {
        this.clientService = clientService;
        this.friendRequestService = friendRequestService;
        this.friendsService = friendsService;
    }

    @PostMapping("/createRequest")
    public ResponseEntity<?> createFriendRequest(@RequestBody FriendRequest friendRequest,
                                                 @RequestHeader(value = AUTH_HEADER_NAME) String authHeader) {
        try {
            String currentUserName = TokenUtil.getUserNameFromToken(authHeader);
            if (currentUserName.equals(friendRequest.getOwnerUserName())) {
                Optional<ClientDao> sender = clientService.findByUsername(friendRequest.getOwnerUserName());
                Optional<ClientDao> recipient = clientService.findByUsername(friendRequest.getRecipientUserName());

                if (sender.isPresent() && recipient.isPresent()) {
                    if (!friendsService.hasFriendship(sender.get().getId(), recipient.get().getId())) {
                        FriendRequestDao req = friendRequestService
                                .saveFriendshipRequest(sender.get().getId(), recipient.get().getId());
                        return new ResponseEntity<>(new CustomResponse<>(OK,
                                Optional.empty(),
                                Optional.of(req.getId())
                        ), HttpStatus.OK);
                    } else
                        return new ResponseEntity<>(new CustomResponse<>(INTERNAL_ERROR,
                            Optional.of("Request is exist"),
                            Optional.empty()
                        ), HttpStatus.OK);
                } else
                    return new ResponseEntity<>(
                            new CustomResponse<>(
                                    INTERNAL_ERROR,
                                    Optional.of("User not found"),
                                    Optional.empty()
                            ), HttpStatus.OK);
            } else
                return new ResponseEntity<>(new CustomResponse<>(INTERNAL_ERROR,
                        Optional.of("You can create request only for yourself"),
                        Optional.empty()
                ), HttpStatus.OK);

        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>(new CustomResponse<>(INTERNAL_ERROR,
                    Optional.of("Create friend request failed"),
                    Optional.empty()
            ), HttpStatus.OK);
        }
    }

    @GetMapping("/getAllRequests")
    public ResponseEntity getAllFriendRequests(@RequestHeader(value = AUTH_HEADER_NAME) String authHeader) {
        try {
            String userName = TokenUtil.getUserNameFromToken(authHeader);
            Optional<ClientDao> client = clientService.findByUsername(userName);
            if (client.isPresent()) {
                List<Integer> reqs = friendRequestService.findAllRequestsByUserId(client.get().getId());
                List<Pair<Integer, String>> reqsUsernames = clientService.findLoginsByIdsAndGetPairs(reqs);
                return new ResponseEntity<>(new CustomResponse<>(OK,
                        Optional.empty(),
                        Optional.of(reqsUsernames)
                ), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new CustomResponse<>(INTERNAL_ERROR,
                        Optional.of("user not found"),
                        Optional.empty()
                ), HttpStatus.OK);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>(new CustomResponse<>(INTERNAL_ERROR,
                    Optional.of("Load friend request failed"),
                    Optional.empty()
            ), HttpStatus.OK);
        }
    }

    @GetMapping("/getAll")
    public ResponseEntity getAllFriends(@RequestHeader(value = AUTH_HEADER_NAME) String authHeader) {
        try {
            String userName = TokenUtil.getUserNameFromToken(authHeader);
            Optional<ClientDao> client = clientService.findByUsername(userName);
            if (client.isPresent()) {
                List<Integer> friendIds = friendsService.findAllFriendIdById(client.get().getId());
                List<String> friendLogins = clientService.findLoginsByIds(friendIds);
                return new ResponseEntity<>(new CustomResponse<>(OK,
                        Optional.empty(),
                        Optional.of(friendLogins)
                ), HttpStatus.OK);
            } else {
                throw new CustomError("user not found");
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>(new CustomResponse<>(INTERNAL_ERROR,
                    Optional.of("Load friends failed"),
                    Optional.empty()
            ), HttpStatus.OK);
        }
    }

    @Transactional
    @GetMapping("/acceptRequest")
    public ResponseEntity acceptFriendRequest(@RequestParam Integer requestId,
                                              @RequestHeader(value = AUTH_HEADER_NAME) String authHeader) {
        try {
            String userName = TokenUtil.getUserNameFromToken(authHeader);
            Optional<ClientDao> client = clientService.findByUsername(userName);
            Optional<FriendRequestDao> friendRequest = friendRequestService.findByRequestId(requestId);
            if (friendRequest.isPresent() && client.isPresent() && friendRequest.get().getRecipientId().equals(client.get().getId())) {
                friendsService.addFriend(friendRequest.get().getSenderId(), friendRequest.get().getRecipientId());
                friendRequestService.deleteFriendRequest(requestId);
                return new ResponseEntity<>(new CustomResponse<>(OK,
                        Optional.empty(),
                        Optional.empty()
                ), HttpStatus.OK);
            } else {
                throw new Exception("This friend request no longer exists");
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>(new CustomResponse<>(INTERNAL_ERROR,
                    Optional.of("accept request if failed"),
                    Optional.empty()
            ), HttpStatus.OK);
        }
    }

    @Transactional
    @GetMapping("/declineRequest")
    public ResponseEntity declineFriendRequest(@RequestParam Integer requestId) {
        try {
            Optional<FriendRequestDao> friendRequest = friendRequestService.findByRequestId(requestId);
            if (friendRequest.isPresent()){
                friendRequestService.deleteFriendRequest(requestId);
                return new ResponseEntity<>(new CustomResponse<>(OK,
                        Optional.empty(),
                        Optional.empty()
                ), HttpStatus.OK);
            } else {
                throw new Exception("No friend request with this ids");
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>(new CustomResponse<>(INTERNAL_ERROR,
                    Optional.of("decline request is failed"),
                    Optional.empty()
            ), HttpStatus.OK);
        }
    }

    @PostMapping("/delete")
    public ResponseEntity deleteFriendShip(@RequestHeader(value = AUTH_HEADER_NAME) String authHeader, @RequestParam String friendUserName) {
        try {
            String currentUserName = TokenUtil.getUserNameFromToken(authHeader);
            Optional<ClientDao> currentUser = clientService.findByUsername(currentUserName);
            Optional<ClientDao> friend = clientService.findByUsername(friendUserName);
            if (currentUser.isPresent() && friend.isPresent()) {
                if (friendsService.hasFriendship(currentUser.get().getId(), friend.get().getId())) {
                    friendsService.deleteFriendship(currentUser.get().getId(), friend.get().getId());
                    return new ResponseEntity<>(new CustomResponse<>(OK,
                            Optional.empty(),
                            Optional.empty()
                    ), HttpStatus.OK);
                } else {
                    throw new Exception("No friendship between them");
                }

            } else throw new Exception("Users not found");
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>(new CustomResponse<>(INTERNAL_ERROR,
                    Optional.of("delete friend is failed"),
                    Optional.empty()
            ), HttpStatus.OK);
        }
    }
}
