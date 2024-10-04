package com.ejunior.fisio_api.exceptions;

public class CodeUniqueViolationException extends RuntimeException {


    public CodeUniqueViolationException(String msg) {
        super(msg);
    }
}
