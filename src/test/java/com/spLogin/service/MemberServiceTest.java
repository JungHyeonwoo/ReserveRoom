package com.spLogin.service;

import com.spLogin.api.domain.entity.Member;
import com.spLogin.api.domain.request.LoginRequest;
import com.spLogin.api.domain.request.RegisterRequest;
import com.spLogin.api.domain.response.UpdatePasswordDTO;
import com.spLogin.api.repository.MemberRepository;
import com.spLogin.api.service.MemberService;
import com.spLogin.common.dto.TokenDTO;
import com.spLogin.common.dto.TokenRequestDTO;
import com.spLogin.common.exception.CustomException;
import com.spLogin.common.exception.ErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.authentication.*;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class MemberServiceTest {

  @Mock
  private MemberRepository userRepository;
  @Mock
  private PasswordEncoder passwordEncoder;
  @Mock
  private AuthenticationManagerBuilder authenticationManagerBuilder;
  @Mock
  private com.spLogin.common.provider.JwtTokenProvider jwtTokenProvider;
  @Mock
  private StringRedisTemplate redisTemplate;
  @Mock
  private ValueOperations<String, String> valueOperations;
  @Mock
  private Authentication authentication;

  @InjectMocks
  private MemberService memberService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    when(redisTemplate.opsForValue()).thenReturn(valueOperations);
  }

  // -----------------------------------------------------------
  @Test
  @DisplayName("회원가입 성공")
  void register_success() {
    RegisterRequest request = new RegisterRequest("test@test.com", "Password1!", "nick");

    when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.empty());
    when(userRepository.findUserByNickname(anyString())).thenReturn(Optional.empty());
    when(passwordEncoder.encode(anyString())).thenReturn("encoded");
    Member member = Member.createNewUser(passwordEncoder, request);
    when(userRepository.save(any(Member.class))).thenReturn(member);

    var response = memberService.register(request);

    assertThat(response.getEmail()).isEqualTo("test@test.com");
    assertThat(response.getNickname()).isEqualTo("nick");
  }

  @Test
  @DisplayName("회원가입 실패 - 이미 존재하는 이메일")
  void register_fail_duplicateEmail() {
    RegisterRequest request = new RegisterRequest("dup@test.com", "Password1!", "nick");
    when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.of(new Member()));

    assertThatThrownBy(() -> memberService.register(request))
        .isInstanceOf(CustomException.class)
        .hasMessageContaining(ErrorCode.ALREADY_USER.getMessage());
  }

  // -----------------------------------------------------------
  @Test
  @DisplayName("로그인 성공")
  void login_success() {
    LoginRequest request = new LoginRequest("test@test.com", "Password1!");
    UsernamePasswordAuthenticationToken token =
        new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());

    when(authenticationManagerBuilder.getObject()).thenReturn(mock(AuthenticationManager.class));
    when(authenticationManagerBuilder.getObject().authenticate(token)).thenReturn(authentication);
    when(jwtTokenProvider.generateTokenDTO(authentication)).thenReturn(
        new TokenDTO("", "access", "refresh", 1000L, 2000L, 0L)
    );

    var result = memberService.login(request);

    assertThat(result.getAccessToken()).isEqualTo("access");
  }

  // -----------------------------------------------------------
//  @Test
//  @DisplayName("토큰 재발급 성공")
//  void reissue_success() {
//    TokenRequestDTO tokenReq = new TokenRequestDTO("accessToken", "refreshToken");
//    when(jwtTokenProvider.validateToken(anyString())).thenReturn(true);
//    when(jwtTokenProvider.getAuthentication(anyString())).thenReturn(authentication);
//    when(valueOperations.get("RT:accessToken")).thenReturn("refreshToken");
//    when(jwtTokenProvider.generateTokenDTO(authentication))
//        .thenReturn(new TokenDTO("newAccess", "newRefresh", 1000L, 2000L));
//
//    var result = memberService.reissue(tokenReq);
//
//    assertThat(result.getAccessToken()).isEqualTo("newAccess");
//    verify(valueOperations).set(eq("RT:newAccess"), eq("newRefresh"), anyLong(), eq(TimeUnit.MILLISECONDS));
//  }

  @Test
  @DisplayName("토큰 재발급 실패 - 리프레시 토큰 불일치")
  void reissue_fail_notMatch() {
    TokenRequestDTO tokenReq = new TokenRequestDTO("accessToken", "refreshToken");
    when(jwtTokenProvider.validateToken(anyString())).thenReturn(true);
    when(jwtTokenProvider.getAuthentication(anyString())).thenReturn(authentication);
    when(valueOperations.get("RT:accessToken")).thenReturn("otherRefresh");

    assertThatThrownBy(() -> memberService.reissue(tokenReq))
        .isInstanceOf(CustomException.class)
        .hasMessageContaining(ErrorCode.NOT_MATCH_TOKEN_INFO.getMessage());
  }

  // -----------------------------------------------------------
  @Test
  @DisplayName("로그아웃 성공")
  void logout_success() {
    TokenRequestDTO tokenReq = new TokenRequestDTO("accessToken", "refreshToken");
    when(jwtTokenProvider.validateToken(anyString())).thenReturn(true);
    when(valueOperations.get("RT:accessToken")).thenReturn("refreshToken");

    memberService.logout(tokenReq);

    verify(redisTemplate).delete("RT:accessToken");
  }

  // -----------------------------------------------------------
  @Test
  @DisplayName("비밀번호 변경 성공")
  void updatePassword_success() {
    UpdatePasswordDTO dto = new UpdatePasswordDTO("test@test.com", "NewPassword1!");
    Member member = mock(Member.class);

    when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.of(member));
    when(passwordEncoder.encode(anyString())).thenReturn("encoded");

    memberService.updatePassword(dto);

    verify(member).changePassword("encoded");
  }

  @Test
  @DisplayName("비밀번호 변경 실패 - 사용자 없음")
  void updatePassword_fail_userNotFound() {
    UpdatePasswordDTO dto = new UpdatePasswordDTO("notfound@test.com", "NewPassword1!");
    when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.empty());

    assertThatThrownBy(() -> memberService.updatePassword(dto))
        .isInstanceOf(CustomException.class)
        .hasMessageContaining(ErrorCode.USER_NOT_FOUND.getMessage());
  }
}
