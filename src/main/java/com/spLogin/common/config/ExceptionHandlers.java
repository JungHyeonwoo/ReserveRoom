package com.spLogin.common.config;

import com.spLogin.api.domain.response.ErrorResponse;
import com.spLogin.common.exception.CustomException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandlers {

  @ExceptionHandler
  public ResponseEntity<ErrorResponse> handleBadRequest(Exception exception) {
    exception.printStackTrace();
    return new ResponseEntity<>(new ErrorResponse(
        HttpStatus.BAD_REQUEST.value(),
        HttpStatus.BAD_REQUEST.getReasonPhrase(),
        exception.getMessage()
    ), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler
  public ResponseEntity<ErrorResponse> handleCustomException(CustomException exception) {
    exception.printStackTrace();
    return new ResponseEntity<>(new ErrorResponse(
        HttpStatus.BAD_REQUEST.value(),
        HttpStatus.BAD_REQUEST.getReasonPhrase(),
        exception.getMessage()
    ), HttpStatus.BAD_REQUEST);
  }
}
