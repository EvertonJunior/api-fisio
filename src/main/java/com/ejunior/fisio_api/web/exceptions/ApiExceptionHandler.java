package com.ejunior.fisio_api.web.exceptions;


import com.ejunior.fisio_api.exceptions.CpfUniqueViolationException;
import com.ejunior.fisio_api.exceptions.UserUniqueViolationException;
import com.ejunior.fisio_api.exceptions.InvalidPasswordException;
import com.ejunior.fisio_api.exceptions.NotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<StandardError> accessDeniedException(HttpServletRequest request,
                                                               AccessDeniedException ex){
        StandardError error = new StandardError(request, HttpStatus.FORBIDDEN, ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<StandardError> methodArgumentNotValidException(HttpServletRequest request,
                                                                         MethodArgumentNotValidException ex,
                                                                         BindingResult result){
        StandardError error = new StandardError(request, HttpStatus.UNPROCESSABLE_ENTITY, "Dados invalidos.", result);
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(error);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<StandardError> notFoundException(HttpServletRequest request,
                                                           RuntimeException ex){
        StandardError error = new StandardError(request, HttpStatus.NOT_FOUND, ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<StandardError> InvalidPasswordException(HttpServletRequest request,
                                                                  RuntimeException ex){
        StandardError error = new StandardError(request, HttpStatus.BAD_REQUEST, ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler({UserUniqueViolationException.class, CpfUniqueViolationException.class})
    public ResponseEntity<StandardError> existingUserException(HttpServletRequest request,
                                                               RuntimeException ex){
        StandardError error = new StandardError(request, HttpStatus.CONFLICT, ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }
}
