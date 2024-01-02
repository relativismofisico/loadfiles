package com.loadfilesservice.loadfiles.exceptions;

public class ResourceNotFoundException extends RuntimeException{

	public ResourceNotFoundException() {
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }
	
	private static final long serialVersionUID = 1L;

}
