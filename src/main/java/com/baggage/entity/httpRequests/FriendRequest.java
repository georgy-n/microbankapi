package com.baggage.entity.httpRequests;

public class FriendRequest {

	private String ownerUserName;

	public String getOwnerUserName() {
		return ownerUserName;
	}

	public void setOwnerUserName(String ownerUserName) {
		this.ownerUserName = ownerUserName;
	}

	public String getRecipientUserName() {
		return recipientUserName;
	}

	public void setRecipientUserName(String recipientUserName) {
		this.recipientUserName = recipientUserName;
	}

	private String recipientUserName;

	public FriendRequest() {
	}

	public FriendRequest(String ownerUserName, String recipientUserName) {
		this.ownerUserName = ownerUserName;
		this.recipientUserName = recipientUserName;
	}
}
