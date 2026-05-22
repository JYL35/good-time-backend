package com.wooteco.haveagoodtime.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long githubUserId;
    private String crewNickname;
    private String githubProfileImageUrl;

    public Member(Long githubUserId, String crewNickname, String githubProfileImageUrl) {
        this.githubUserId = githubUserId;
        this.crewNickname = crewNickname;
        this.githubProfileImageUrl = githubProfileImageUrl;
    }

    public void update(String crewNickname, String githubProfileImageUrl) {
        this.crewNickname = crewNickname;
        this.githubProfileImageUrl = githubProfileImageUrl;
    }

    public void updateCrewNickname(String crewNickname) {
        this.crewNickname = crewNickname;
    }

    public void updateGithubProfileImageUrl(String githubProfileImageUrl) {
        this.githubProfileImageUrl = githubProfileImageUrl;
    }
}
