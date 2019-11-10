package com.baggage.entity.httpRequests;

public class FriendRequest {

	private String ownerId;

	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	public String getRecipientId() {
		return recipientId;
	}

	public void setRecipientId(String recipientId) {
		this.recipientId = recipientId;
	}

	private String recipientId;

	public FriendRequest(String ownerId, String recipientId) {
		this.ownerId = ownerId;
		this.recipientId = recipientId;
	}
}
