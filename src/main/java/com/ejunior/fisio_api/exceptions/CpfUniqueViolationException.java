package com.ejunior.fisio_api.exceptions;

public class CpfUniqueViolationException extends RuntimeException {

    public CpfUniqueViolationException(String s) {
        super(s);
    }
}
