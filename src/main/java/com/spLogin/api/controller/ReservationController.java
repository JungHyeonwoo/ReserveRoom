package com.spLogin.api.controller;

import com.spLogin.api.domain.request.MeetingRoomBookRequest;
import com.spLogin.api.domain.request.MeetingRoomRequest;
import com.spLogin.api.domain.response.MeetingRoomDetailResponse;
import com.spLogin.api.domain.response.MeetingRoomResponse;
import com.spLogin.api.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/summer")
@RequiredArgsConstructor
public class ReservationController {

  private final ReservationService reservationService;

  @GetMapping("/meetingroom/list")
  public ResponseEntity<?> getMeetingRoomList() {
    return ResponseEntity.ok(reservationService.getMeetingRoomList());
  }

  @PostMapping("/reserve/meetingroom")
  public ResponseEntity<?> reserveMeetingRoom(@RequestBody MeetingRoomBookRequest meetingRoomBookRequest) {
    return ResponseEntity.ok(reservationService.reserveMeetingRoom(meetingRoomBookRequest));
  }

  @PostMapping("/reserve/meetingroom/cancel")
  public ResponseEntity<?> reserveCancelMeetingRoom(Long reserveRoomId) {
    reservationService.reserveCancelMeetingRoom(reserveRoomId);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/create/meetingroom")
  public MeetingRoomResponse createMeetingRoom(@RequestBody MeetingRoomRequest meetingRoomRequest) {
    return reservationService.createMeetingRoom(meetingRoomRequest);
  }

  @GetMapping("/meetingroom/{meetingRoomId}")
  public MeetingRoomDetailResponse getMeetingRoom(@PathVariable Long meetingRoomId) {
    return reservationService.getMeetingRoom(meetingRoomId);
  }

  @GetMapping("/my-reservation")
  public ResponseEntity<?> getMyReservation(@RequestParam Long id) {
    return ResponseEntity.ok(reservationService.getMyReservation(id));
  }

}
