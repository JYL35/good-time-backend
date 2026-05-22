package com.wooteco.haveagoodtime.dto.response;

import com.wooteco.haveagoodtime.domain.Gathering;
import com.wooteco.haveagoodtime.domain.GatheringStatus;

import java.time.LocalDateTime;

public record GatheringDetailResponse(Long id, String name, int headCount,
                                      LocalDateTime gatheringDatetime, LocalDateTime dueDate,
                                      String description, GatheringStatus status,
                                      Long hostId, int participantCount) {

    public static GatheringDetailResponse from(Gathering gathering, int participantCount) {
        return new GatheringDetailResponse(
                gathering.getId(),
                gathering.getName(),
                gathering.getHeadCount(),
                gathering.getGatheringDatetime(),
                gathering.getDueDate(),
                gathering.getDescription(),
                gathering.getStatus(),
                gathering.getHost().getId(),
                participantCount
        );
    }
}
