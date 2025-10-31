package com.spLogin.api.domain.response;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Getter
@Setter
public class ErrorResponse {

  int status;
  String title;
  String detail;
  String path;

  public ErrorResponse(int status, String title, String detail) {
    this.status = status;
    this.title = title;
    this.detail = detail;
    if (RequestContextHolder.getRequestAttributes() != null) {
      this.path = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest()
          .getRequestURI();
    }
  }
}