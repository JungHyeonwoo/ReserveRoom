package com.spLogin.api.domain.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MeetingRoomDetailResponse {
  private Long id;
  private String name;
  private Long peopleLimit;
  private String location;
  private List<MeetingRoomReserveHistoryResponse> reserveHistories;


}
