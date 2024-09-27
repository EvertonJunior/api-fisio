package com.ejunior.fisio_api.exceptions;

public class InvalidPasswordException extends RuntimeException {

    public InvalidPasswordException(String msg) {
        super(msg);
    }
}
