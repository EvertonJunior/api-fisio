package com.ejunior.fisio_api.exceptions;

public class UserUniqueViolationException extends RuntimeException{

    public UserUniqueViolationException(String msg){
        super(msg);
    }
}
