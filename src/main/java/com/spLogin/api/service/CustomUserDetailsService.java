package com.spLogin.api.service;

import com.spLogin.api.domain.entity.Member;
import com.spLogin.api.repository.MemberRepository;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService, OAuth2User {

  private Map<String, Object> attributes;
  private final MemberRepository memberRepository;


  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    Member member = memberRepository.findByEmailAndIsDeletedFalse(email)
        .orElseThrow(() -> new UsernameNotFoundException(email + "아이디 또는 비밀번호를 확인해주세요."));
    return createUserDetails(member);
  }

  private UserDetails createUserDetails(Member user) {
    SimpleGrantedAuthority authority = new SimpleGrantedAuthority(user.getRole().toString());
    return new org.springframework.security.core.userdetails.User(
        user.getEmail(),
        user.getPassword(),
        Collections.singleton(authority)
    );
  }

  @Override
  public Map<String, Object> getAttributes() {
    return attributes;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    Collection<GrantedAuthority> collection = new ArrayList<>();
    collection.add(new GrantedAuthority() {
      @Override
      public String getAuthority() {
        return "";
      }
    });

    return collection;
  }

  @Override
  public String getName() {
    return null;
  }
}
