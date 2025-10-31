package com.spLogin.api.domain.request;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MeetingRoomBookRequest {

  private Long meetingRoomId;
  private String description;
  @DateTimeFormat(iso = ISO.DATE_TIME)
  private LocalDateTime meetingStartDateTime;
  @DateTimeFormat(iso = ISO.DATE_TIME)
  private LocalDateTime meetingEndDateTime;

  private Long memberId;

}
