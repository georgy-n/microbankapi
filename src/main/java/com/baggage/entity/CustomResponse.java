package com.baggage.entity;

import java.util.Optional;

public class CustomResponse<T> {
    private String status;
    private Optional<String> message;
    private Optional<T> payload;

    public String getStatus() {
        return status;
    }

    public Optional<String> getMessage() {
        return message;
    }

    public Optional<T> getPayload() {
        return payload;
    }

    public CustomResponse(String status, Optional<String> message, Optional<T> payload) {
        this.status = status;
        this.message = message;
        this.payload = payload;
    }
}
