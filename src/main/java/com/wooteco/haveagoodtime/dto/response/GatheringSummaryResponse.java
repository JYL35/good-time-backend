package com.wooteco.haveagoodtime.dto.response;

import com.wooteco.haveagoodtime.domain.Gathering;
import com.wooteco.haveagoodtime.domain.GatheringStatus;

import java.time.LocalDateTime;

public record GatheringSummaryResponse(Long id, String name, int headCount,
                                       LocalDateTime gatheringDatetime, LocalDateTime dueDate,
                                       GatheringStatus status) {

    public static GatheringSummaryResponse from(Gathering gathering) {
        return new GatheringSummaryResponse(
                gathering.getId(),
                gathering.getName(),
                gathering.getHeadCount(),
                gathering.getGatheringDatetime(),
                gathering.getDueDate(),
                gathering.getStatus()
        );
    }
}
