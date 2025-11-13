package com.spLogin.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;
import javax.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class MemberControllerTest {

  @Autowired
  private MockMvc mockMvc;

  protected MediaType contentType =
      new MediaType(MediaType.APPLICATION_JSON.getType(),
          MediaType.APPLICATION_JSON.getSubtype(),
          StandardCharsets.UTF_8);

  @Test
  void registerMember() throws Exception {
    String requestBody = "{\"email\":\"sktom2@naver.com\", \"password\":\"asdf1234!\", \"nickname\":\"sktom2\"}";

    mockMvc.perform(post("/v1/user/sing/up")
            .contentType(contentType)
            .content(requestBody))
        .andExpect(status().isOk());

  }
}
