package com.kristofer.traveling.services.exceptions;

public class ObjectNotNullException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public ObjectNotNullException(String msg){
        super(msg);
    }
}
