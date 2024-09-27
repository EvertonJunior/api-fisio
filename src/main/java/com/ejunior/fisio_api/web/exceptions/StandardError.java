package com.ejunior.fisio_api.web.exceptions;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.servlet.http.HttpServletRequest;
import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Getter @Setter @NoArgsConstructor @ToString
public class StandardError {

    private String path;
    private String method;
    private Integer status;
    private String statusText;
    private String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Map<String, String> errors;


    public StandardError(HttpServletRequest request, HttpStatus status, String message) {
        this.method = request.getMethod();
        this.path = request.getRequestURI();
        this.status = status.value();
        this.statusText = status.getReasonPhrase();
        this.message = message;
    }

    public StandardError(HttpServletRequest request, HttpStatus status, String message, BindingResult result) {
        this.method = request.getMethod();
        this.path = request.getRequestURI();
        this.status = status.value();
        this.statusText = status.getReasonPhrase();
        this.message = message;
        addErrors(result);
    }

    private void addErrors(BindingResult result) {
        this.errors = new HashMap<>();
        for(FieldError error : result.getFieldErrors()){
            this.errors.put(error.getField(), error.getDefaultMessage());
        }
    }


}
