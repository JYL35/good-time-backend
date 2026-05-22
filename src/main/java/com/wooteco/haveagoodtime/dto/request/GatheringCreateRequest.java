package com.wooteco.haveagoodtime.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "모임 생성 요청")
public record GatheringCreateRequest(
        @Schema(description = "모임 이름", example = "잠실 보드게임 모임")
        String name,

        @Schema(description = "모집 인원", example = "6")
        int headCount,

        @Schema(description = "모임 일시", example = "2026-05-30T19:00:00")
        LocalDateTime gatheringDatetime,

        @Schema(description = "모집 마감 일시", example = "2026-05-29T23:59:00")
        LocalDateTime dueDate,

        @Schema(description = "모임 설명", example = "가볍게 보드게임을 즐길 크루를 모집합니다.")
        String description
) {
}
