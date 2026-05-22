package com.wooteco.haveagoodtime.dto.response;

import com.wooteco.haveagoodtime.domain.Gathering;
import com.wooteco.haveagoodtime.domain.GatheringStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "모임 요약 응답")
public record GatheringSummaryResponse(
        @Schema(description = "모임 ID", example = "1")
        Long id,

        @Schema(description = "모임 이름", example = "잠실 보드게임 모임")
        String name,

        @Schema(description = "모집 인원", example = "6")
        int headCount,

        @Schema(description = "현재 참여자 수", example = "3")
        int participantCount,

        @Schema(description = "모임 일시", example = "2026-05-30T19:00:00")
        LocalDateTime gatheringDatetime,

        @Schema(description = "모집 마감 일시", example = "2026-05-29T23:59:00")
        LocalDateTime dueDate,

        @Schema(description = "모임 상태", example = "RECRUITING")
        GatheringStatus status
) {

    public static GatheringSummaryResponse from(Gathering gathering, int participantCount) {
        return new GatheringSummaryResponse(
                gathering.getId(),
                gathering.getName(),
                gathering.getHeadCount(),
                participantCount,
                gathering.getGatheringDatetime(),
                gathering.getDueDate(),
                gathering.getStatus()
        );
    }
}
