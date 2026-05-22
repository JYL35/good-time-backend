package com.wooteco.haveagoodtime.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Gathering {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int headCount;
    private LocalDateTime gatheringDatetime;
    private LocalDateTime dueDate;
    private String description;

    @Enumerated(EnumType.STRING)
    private GatheringStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member host;

    public Gathering(String name, int headCount, LocalDateTime gatheringDatetime,
                     LocalDateTime dueDate, String description, Member host) {
        this.name = name;
        this.headCount = headCount;
        this.gatheringDatetime = gatheringDatetime;
        this.dueDate = dueDate;
        this.description = description;
        this.host = host;
        this.status = GatheringStatus.RECRUITING;
    }

    public void update(String name, int headCount, LocalDateTime gatheringDatetime,
                       LocalDateTime dueDate, String description) {
        this.name = name;
        this.headCount = headCount;
        this.gatheringDatetime = gatheringDatetime;
        this.dueDate = dueDate;
        this.description = description;
    }

    public boolean isHost(Long memberId) {
        return host.getId().equals(memberId);
    }
}
