package com.sportsclubmanager.backend.shared.exception;

import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private static final String MESSAGE_KEY = "message";
    private static final String DETAILS_KEY = "details";
    private static final String STATUS_KEY = "status";

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException e) {
        logger.error("Constraint violation occurred: {}", e.getMessage(), e);

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put(MESSAGE_KEY, "Constraint violation occurred. Check unique fields.");
        errorResponse.put(DETAILS_KEY, e.getMessage());
        errorResponse.put("constraint violations", e.getConstraintViolations());
        errorResponse.put(STATUS_KEY, HttpStatus.CONFLICT.value());

        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        logger.error("Data integrity violation occurred: {}", e.getMessage(), e);

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put(MESSAGE_KEY, "Data integrity violation occurred. Check unique fields.");
        errorResponse.put(DETAILS_KEY, e.getMessage());
        errorResponse.put(STATUS_KEY, HttpStatus.CONFLICT.value());

        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGeneralException(Exception e) {
        logger.error("An unexpected error occurred: {}", e.getMessage(), e);

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("exception type", e.getClass().getSimpleName());
        errorResponse.put(MESSAGE_KEY, "An unexpected error occurred.");
        errorResponse.put(DETAILS_KEY, e.getMessage());
        errorResponse.put(STATUS_KEY, HttpStatus.INTERNAL_SERVER_ERROR.value());

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
