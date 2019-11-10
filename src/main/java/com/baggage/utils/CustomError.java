package com.baggage.utils;

public class CustomError extends Exception {

	private String errorMessage;

	public CustomError(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getErrorMessage() {
		return errorMessage;
	}
}
