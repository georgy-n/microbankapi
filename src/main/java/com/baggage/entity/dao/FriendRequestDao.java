package com.baggage.entity.dao;

import javax.persistence.*;

@Entity
@Table(name = "friend_request")
public class FriendRequestDao {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "id_from")
    private Integer senderId;

    @Column(name = "id_to")
    private Integer recipientId;

    public FriendRequestDao(Integer senderId, Integer recipientId) {
        this.senderId = senderId;
        this.recipientId = recipientId;
    }

    public FriendRequestDao() {
    }

    public Integer getSenderId() {
        return senderId;
    }

    public Integer getRecipientId() {
        return recipientId;
    }

    public Integer getId() {
        return id;
    }
}
