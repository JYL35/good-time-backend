package com.wooteco.haveagoodtime.dto.response;

import com.wooteco.haveagoodtime.domain.Participant;

import java.time.LocalDateTime;

public record ParticipantResponse(String alias, String crewNickname, LocalDateTime joinTime) {

    public static ParticipantResponse of(Participant participant, int index, boolean revealed) {
        return new ParticipantResponse(
                "크루 #" + index,
                revealed ? participant.getMember().getCrewNickname() : null,
                participant.getJoinTime()
        );
    }
}
