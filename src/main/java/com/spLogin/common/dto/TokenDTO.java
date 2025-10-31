package com.spLogin.common.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TokenDTO {

  private String grantType;
  private String accessToken;
  private String refreshToken;
  private Long accessTokenExpiresIn;
  private Long refreshTokenExpiresIn;
  private Long memberId;

  @Builder(builderClassName = "of", builderMethodName = "of")
  public TokenDTO(String grantType, String accessToken, String refreshToken, Long accessTokenExpiresIn, Long refreshTokenExpiresIn, Long memberId) {
    this.grantType = grantType;
    this.accessToken = accessToken;
    this.refreshToken = refreshToken;
    this.accessTokenExpiresIn = accessTokenExpiresIn;
    this.refreshTokenExpiresIn = refreshTokenExpiresIn;
    this.memberId = memberId;
  }

}
