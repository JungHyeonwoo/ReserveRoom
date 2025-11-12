package com.spLogin.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
  ErrorCode errorCode;
  private String detailMessage;

  public CustomException(ErrorCode customErrorCode) {
    super(customErrorCode.getMessage());
    this.errorCode = customErrorCode;
    this.detailMessage = errorCode.getMessage();
  }

  public CustomException(ErrorCode errorCode, String detailMessage) {
    super(detailMessage);
    this.errorCode = errorCode;
    this.detailMessage = detailMessage;
  }
}
