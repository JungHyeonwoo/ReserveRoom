package com.spLogin.api.controller;

import com.spLogin.api.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/summer")
@RequiredArgsConstructor
public class ReservationController {

  private final ReservationService reservationService;

  @GetMapping("/meetingroom/list")
  public ResponseEntity<?> getMeetingRoomList() {
    return reservationService.getMeetingRoomList();
  }

  @PostMapping("/reserve/meetingroom")
  public ResponseEntity<?> reserveMeetingRoom() {
    return reservationService.reserveMeetingRoom();
  }

  @PostMapping("/reserve/meetingroom/cancel")
  public ResponseEntity<?> reserveCancelMeetingRoom() {
    return reservationService.reserveCancelMeetingRoom();
  }

  @GetMapping("/reserved/meetingroom/{id}")
  public ResponseEntity<?> getReservedMeetingRoom(@PathVariable Long id) {
    return reservationService.getReservedMeetingRoom(id);
  }

  @

}
