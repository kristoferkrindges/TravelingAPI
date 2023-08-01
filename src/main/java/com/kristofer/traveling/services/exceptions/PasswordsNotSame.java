package com.kristofer.traveling.services.exceptions;

public class PasswordsNotSame extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public PasswordsNotSame(String msg){
        super(msg);
    }
}