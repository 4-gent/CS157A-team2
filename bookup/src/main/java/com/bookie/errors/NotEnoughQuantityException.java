package com.bookie.errors;

public class NotEnoughQuantityException extends Exception {

	private static final long serialVersionUID = 1L;

	public NotEnoughQuantityException(String error) {
		super(error);
	}

}
