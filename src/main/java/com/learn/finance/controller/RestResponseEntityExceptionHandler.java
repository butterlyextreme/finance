package com.learn.finance.controller;

import com.learn.finance.exception.DownStreamException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@Slf4j
@ControllerAdvice
public class RestResponseEntityExceptionHandler {

  // Default exception handler, in case of unexpected errors, we still need to handle them properly.
  @ExceptionHandler(value = {DownStreamException.class})
  public ResponseEntity<String> exceptionHandler(Exception ex) {
    return ResponseEntity.badRequest().body(ex.toString());
  }
}
