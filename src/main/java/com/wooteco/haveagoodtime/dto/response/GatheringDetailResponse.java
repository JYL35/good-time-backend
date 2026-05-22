package com.wooteco.haveagoodtime.dto.response;

import com.wooteco.haveagoodtime.domain.Gathering;
import com.wooteco.haveagoodtime.domain.GatheringStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "모임 상세 응답")
public record GatheringDetailResponse(
        @Schema(description = "모임 ID", example = "1")
        Long id,

        @Schema(description = "모임 이름", example = "잠실 보드게임 모임")
        String name,

        @Schema(description = "모집 인원", example = "6")
        int headCount,

        @Schema(description = "모임 일시", example = "2026-05-30T19:00:00")
        LocalDateTime gatheringDatetime,

        @Schema(description = "모집 마감 일시", example = "2026-05-29T23:59:00")
        LocalDateTime dueDate,

        @Schema(description = "모임 설명", example = "가볍게 보드게임을 즐길 크루를 모집합니다.")
        String description,

        @Schema(description = "모임 상태", example = "RECRUITING")
        GatheringStatus status,

        @Schema(description = "내가 방장인지 여부", example = "false")
        boolean isHost,

        @Schema(description = "현재 참여자 수", example = "3")
        int participantCount
) {

    public static GatheringDetailResponse from(Gathering gathering, int participantCount, boolean isHost) {
        return new GatheringDetailResponse(
                gathering.getId(),
                gathering.getName(),
                gathering.getHeadCount(),
                gathering.getGatheringDatetime(),
                gathering.getDueDate(),
                gathering.getDescription(),
                gathering.getStatus(),
                isHost,
                participantCount
        );
    }
}
