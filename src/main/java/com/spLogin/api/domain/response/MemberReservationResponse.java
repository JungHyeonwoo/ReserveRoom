package com.spLogin.api.domain.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberReservationResponse {
  private String name;
  private List<MeetingRoomReserveHistoryResponse> histories;

}
