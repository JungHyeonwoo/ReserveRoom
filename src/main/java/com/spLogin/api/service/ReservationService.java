package com.spLogin.api.service;

import com.spLogin.api.repository.MeetingRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReservationService {

  private final MeetingRoomRepository meetingRoomRepository;



}
