package com.wooteco.haveagoodtime.dto.response;

import com.wooteco.haveagoodtime.domain.Participant;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "참여자 응답")
public record ParticipantResponse(
        @Schema(description = "회원 ID", example = "1")
        Long memberId,

        @Schema(description = "크루 닉네임", example = "코덱스")
        String crewNickname,

        @Schema(description = "참여 일시", example = "2026-05-22T15:30:00")
        LocalDateTime joinTime
) {

    public static ParticipantResponse from(Participant participant) {
        return new ParticipantResponse(
                participant.getMember().getId(),
                participant.getMember().getCrewNickname(),
                participant.getJoinTime()
        );
    }
}
