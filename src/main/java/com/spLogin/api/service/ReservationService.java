package com.spLogin.api.service;

import com.spLogin.api.domain.entity.MeetingRoom;
import com.spLogin.api.domain.entity.Reservation;
import com.spLogin.api.domain.enumerate.Status;
import com.spLogin.api.domain.request.MeetingRoomBookRequest;
import com.spLogin.api.domain.request.MeetingRoomRequest;
import com.spLogin.api.domain.response.MeetingRoomDetailResponse;
import com.spLogin.api.domain.response.MeetingRoomReserveHistoryResponse;
import com.spLogin.api.domain.response.MeetingRoomResponse;
import com.spLogin.api.domain.response.MemberReservationResponse;
import com.spLogin.api.repository.MeetingRoomRepository;
import com.spLogin.api.repository.MemberRepository;
import com.spLogin.api.repository.ReservationRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReservationService {

  private final MeetingRoomRepository meetingRoomRepository;
  private final MemberRepository userRepository;
  private final ReservationRepository reservationRepository;

  public List<MeetingRoomResponse> getMeetingRoomList() {
    return meetingRoomRepository.findAll()
        .stream()
        .map(this::meetingRoomToMeetingRoomResponse)
        .collect(Collectors.toList());

  }

  public MeetingRoomResponse meetingRoomToMeetingRoomResponse (MeetingRoom meetingRoom) {
    return new MeetingRoomResponse(
        meetingRoom.getId(),
        meetingRoom.getName(),
        isUsingMeetingRoomNow(meetingRoom) ? Status.RESERVED : Status.EMPTY,
        meetingRoom.getMaxPeople(),
        meetingRoom.getLocation()
    );
  }

  public MeetingRoomDetailResponse getMeetingRoom(Long id) {
    return meetingRoomRepository.findById(id)
        .map(this::meetingRoomToMeetingRoomDetailResponse)
        .orElseThrow(() -> new IllegalArgumentException("MeetingRoom not found"));
  }

  public MeetingRoomDetailResponse meetingRoomToMeetingRoomDetailResponse(MeetingRoom meetingRoom) {
    return new MeetingRoomDetailResponse(
        meetingRoom.getId(),
        meetingRoom.getName(),
        meetingRoom.getMaxPeople(),
        meetingRoom.getLocation(),
        meetingRoom.getReservations()
            .stream()
            .map(this::meetingRoomReserveHistoryToMeetingRoomReserveHistoryResponse)
            .collect(Collectors.toList())
    );
  }

  public List<MeetingRoomReserveHistoryResponse> getMeetingRoomReservedHistory(Long id) {
    return meetingRoomRepository.findById(id)
        .map(this::meetingRoomToMeetingRoomReservedHistory)
        .orElseThrow(() -> new IllegalArgumentException("MeetingRoom not found"));
  }

  public List<MeetingRoomReserveHistoryResponse> meetingRoomToMeetingRoomReservedHistory(MeetingRoom meetingRoom) {
    return meetingRoom.getReservations()
            .stream()
            .map(this::meetingRoomReserveHistoryToMeetingRoomReserveHistoryResponse)
            .collect(Collectors.toList());
  }

  private MeetingRoomReserveHistoryResponse meetingRoomReserveHistoryToMeetingRoomReserveHistoryResponse(Reservation reservation) {
    return new MeetingRoomReserveHistoryResponse(
        reservation.getId(),
        reservation.getDescription(),
        reservation.getStartDateTime(),
        reservation.getEndDateTime()
    );
  }

  private boolean isUsingMeetingRoomNow(MeetingRoom meetingRoom) {
    return meetingRoom.getReservations()
        .stream()
        .anyMatch(meetingRoomReserveHistory -> meetingRoomReserveHistory.getEndDateTime().isAfter(
            LocalDateTime.now()) && meetingRoomReserveHistory.getStartDateTime().isBefore(LocalDateTime.now()));
  }

  private boolean isUsingMeetingRoomAtTime(MeetingRoom meetingRoom, LocalDateTime startDateTime, LocalDateTime endDateTime) {
    return meetingRoom.getReservations()
        .stream()
        .anyMatch(meetingRoomReserveHistory -> meetingRoomReserveHistory.getStartDateTime().isBefore(endDateTime) &&
            meetingRoomReserveHistory.getEndDateTime().isAfter(startDateTime));
  }

  public MeetingRoomResponse reserveMeetingRoom(MeetingRoomBookRequest reserveMeetingRoomRequest) {
    MeetingRoom meetingRoom = meetingRoomRepository.findById(reserveMeetingRoomRequest.getMeetingRoomId())
        .orElseThrow(() -> new IllegalArgumentException("MeetingRoom not found"));

    if (isUsingMeetingRoomAtTime(meetingRoom, reserveMeetingRoomRequest.getMeetingStartDateTime(), reserveMeetingRoomRequest.getMeetingEndDateTime())) {
      throw new IllegalArgumentException("MeetingRoom is already reserved");
    }

    Reservation reservation = new Reservation(
        null,
        reserveMeetingRoomRequest.getDescription(),
        reserveMeetingRoomRequest.getMeetingStartDateTime(),
        reserveMeetingRoomRequest.getMeetingEndDateTime(),
        meetingRoom,
        userRepository.save(userRepository.findById(reserveMeetingRoomRequest.getMemberId()).get())
    );
    reservationRepository.save(reservation);

    return meetingRoomToMeetingRoomResponse(meetingRoom);
  }

  public void reserveCancelMeetingRoom(Long reserveRoomId) {
    Reservation reservation = reservationRepository.findById(reserveRoomId)
        .orElseThrow(() -> new IllegalArgumentException("Reservation not found"));
    reservationRepository.delete(reservation);
  }

  public MeetingRoomResponse createMeetingRoom(MeetingRoomRequest meetingRoomRequest) {
    MeetingRoom meetingRoom = new MeetingRoom(
        null,
        meetingRoomRequest.getName(),
        meetingRoomRequest.getLocation(),
        meetingRoomRequest.getPeopleLimit(),
        new ArrayList<>()
    );
    meetingRoomRepository.save(meetingRoom);
    return meetingRoomToMeetingRoomResponse(meetingRoom);
  }

  public MemberReservationResponse getMyReservation(Long id) {
    return new MemberReservationResponse(
        userRepository.findById(id).get().getNickname(),
        userRepository.findById(id).get().getReservations()
            .stream()
            .map(this::meetingRoomReserveHistoryToMeetingRoomReserveHistoryResponse)
            .collect(Collectors.toList())
    );
  }

}
