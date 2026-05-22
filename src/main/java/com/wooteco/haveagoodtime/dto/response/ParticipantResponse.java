package com.wooteco.haveagoodtime.dto.response;

import com.wooteco.haveagoodtime.domain.Participant;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "참여자 응답")
public record ParticipantResponse(
        @Schema(description = "익명 별칭 (항상 공개)", example = "크루 #1")
        String alias,

        @Schema(description = "크루 닉네임 (MATCHED 상태일 때만 공개, 그 외 null)", example = "코덱스")
        String crewNickname,

        @Schema(description = "참여 일시", example = "2026-05-22T15:30:00")
        LocalDateTime joinTime
) {

    public static ParticipantResponse of(Participant participant, int index, boolean revealed) {
        return new ParticipantResponse(
                "크루 #" + index,
                revealed ? participant.getMember().getCrewNickname() : null,
                participant.getJoinTime()
        );
    }
}
