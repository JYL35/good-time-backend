package com.wooteco.haveagoodtime.dto.request;

import java.time.LocalDateTime;

public record GatheringCreateRequest(String name, int headCount, LocalDateTime gatheringDatetime,
                                     LocalDateTime dueDate, String description) {
}
