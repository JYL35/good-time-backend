package com.wooteco.haveagoodtime.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

@Schema(description = "모임 수정 요청")
public record GatheringUpdateRequest(
        @Schema(description = "모임 이름", example = "잠실 보드게임 모임")
        @NotBlank(message = "모임 이름을 입력해주세요.")
        @Size(max = 40, message = "모임 이름은 40자 이하여야 합니다.")
        String name,

        @Schema(description = "모집 인원", example = "6")
        @Min(value = 2, message = "모집 인원은 최소 2명이어야 합니다.")
        @Max(value = 20, message = "모집 인원은 최대 20명까지 가능합니다.")
        int headCount,

        @Schema(description = "모임 일시", example = "2026-05-30T19:00:00")
        @NotNull(message = "모임 일시를 입력해주세요.")
        @Future(message = "모임 일시는 현재 이후여야 합니다.")
        LocalDateTime gatheringDatetime,

        @Schema(description = "모집 마감 일시", example = "2026-05-29T23:59:00")
        @NotNull(message = "모집 마감 일시를 입력해주세요.")
        @Future(message = "모집 마감 일시는 현재 이후여야 합니다.")
        LocalDateTime dueDate,

        @Schema(description = "모임 설명", example = "가볍게 보드게임을 즐길 크루를 모집합니다.")
        @Size(max = 200, message = "모임 설명은 200자 이하여야 합니다.")
        String description
) {
}
