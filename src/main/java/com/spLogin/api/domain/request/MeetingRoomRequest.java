package com.spLogin.api.domain.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MeetingRoomRequest {

  private String name;

  private String location;
  private Long peopleLimit;

}
