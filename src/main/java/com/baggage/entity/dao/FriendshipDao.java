package com.baggage.entity.dao;

import com.baggage.entity.FriendshipId;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "friendship")
@IdClass(FriendshipId.class)
public class FriendshipDao implements Serializable {

    @Id
    @Column(name = "id_a")
    private Integer ownerId;

    @Id
    @Column(name = "id_b")
    private Integer recipientId;


    public FriendshipDao() {
    }

    public FriendshipDao(Integer ownerId, Integer recipientId) {
        this.ownerId = ownerId;
        this.recipientId = recipientId;
    }

    public Integer getOwnerId() {
        return ownerId;
    }
    public Integer getRecipientId() {
        return recipientId;
    }

}
