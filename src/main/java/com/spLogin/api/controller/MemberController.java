package com.spLogin.api.controller;

import com.spLogin.api.domain.request.LoginRequest;
import com.spLogin.api.domain.request.RegisterRequest;
import com.spLogin.api.domain.response.UserResponse;
import com.spLogin.api.service.MemberService;
import com.spLogin.common.dto.TokenDTO;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/v1/user")
@RestController
@RequiredArgsConstructor
public class MemberController {

  private final MemberService userService;

  @PostMapping("/sing/up")
  public UserResponse singUp(@RequestBody @Valid RegisterRequest registerRequest) {
    return userService.register(registerRequest);
  }

  @PostMapping("/login")
  public TokenDTO login(@RequestBody @Valid LoginRequest loginRequest) {
    return userService.login(loginRequest);
  }
}
