package com.spLogin.api.domain.response;

import com.spLogin.api.domain.enumerate.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MeetingRoomResponse {
  private Long id;
  private String name;
  private Status status;
  private Long peopleLimit;
  private String location;


}
