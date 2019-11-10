package com.baggage.entity;

import java.io.Serializable;

public class FriendshipId implements Serializable{
    private Integer ownerId;
    private Integer recipientId;

    public FriendshipId() {

    }

    public FriendshipId(Integer ownerId, Integer recipientId) {
        this.ownerId = ownerId;
        this.recipientId = recipientId;
    }

    public Integer getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(Integer recipientId) {
        this.recipientId = recipientId;
    }

    public Integer getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Integer ownerId) {
        this.ownerId = ownerId;
    }
}
