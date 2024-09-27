package com.ejunior.fisio_api.exceptions;

public class ExistingUserException extends RuntimeException{

    public ExistingUserException(String msg){
        super(msg);
    }
}
