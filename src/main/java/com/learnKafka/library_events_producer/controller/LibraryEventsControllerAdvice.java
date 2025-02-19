package com.learnKafka.library_events_producer.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
public class LibraryEventsControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleException(MethodArgumentNotValidException ex){
        var errorMessages = ex.getBindingResult()
                .getFieldErrors()
                .stream().map(fe-> fe.getField() + " - " +fe.getDefaultMessage() + " , ")
                .sorted()
                .collect(Collectors.joining());
        log.info("errorMessage : {}" , errorMessages);
        return new ResponseEntity<>(errorMessages, HttpStatus.BAD_REQUEST);
    }
}
