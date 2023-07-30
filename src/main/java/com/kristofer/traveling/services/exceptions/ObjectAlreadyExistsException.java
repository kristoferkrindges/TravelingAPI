package com.kristofer.traveling.services.exceptions;

public class ObjectAlreadyExistsException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public ObjectAlreadyExistsException(String msg){
        super(msg);
    }
}
