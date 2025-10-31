package com.spLogin.common.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandler {

  @ExceptionHandler({CustomException.class})
  protected ResponseEntity<ErrorResponseEntity> handleCustomException(CustomException e) {
    e.printStackTrace();
    return ErrorResponseEntity.toResponseEntity(e.getErrorCode());
  }


}
