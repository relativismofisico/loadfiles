package com.loadfilesservice.loadfiles.exceptions;

public class BadRequestException extends RuntimeException{

	public BadRequestException() {
    }

    public BadRequestException(String message) {
        super(message);
    }
	
	private static final long serialVersionUID = 1L;

}
