package com.spLogin.api.domain.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MeetingRoomReserveHistoryResponse {
  private Long id;
  private String description;
  private LocalDateTime startDateTime;
  private LocalDateTime endDateTime;

}
