package com.spLogin.api.repository;

import com.spLogin.api.domain.entity.MeetingRoom;
import com.spLogin.api.domain.entity.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeetingRoomRepository extends JpaRepository<MeetingRoom, Long> {

}
