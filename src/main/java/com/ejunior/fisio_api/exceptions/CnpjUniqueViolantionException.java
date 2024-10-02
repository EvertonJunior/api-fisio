package com.ejunior.fisio_api.exceptions;

public class CnpjUniqueViolantionException extends RuntimeException {
    public CnpjUniqueViolantionException(String msg) {
        super(msg);
    }
}
