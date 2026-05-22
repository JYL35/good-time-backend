package com.wooteco.haveagoodtime.dto.response;

import com.wooteco.haveagoodtime.domain.Participant;

import java.time.LocalDateTime;

public record ParticipantResponse(Long memberId, String crewNickname, LocalDateTime joinTime) {

    public static ParticipantResponse from(Participant participant) {
        return new ParticipantResponse(
                participant.getMember().getId(),
                participant.getMember().getCrewNickname(),
                participant.getJoinTime()
        );
    }
}
