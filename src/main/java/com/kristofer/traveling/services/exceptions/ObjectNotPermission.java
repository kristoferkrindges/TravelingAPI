package com.kristofer.traveling.services.exceptions;

public class ObjectNotPermission extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public ObjectNotPermission(String msg){
        super(msg);
    }
}
    
