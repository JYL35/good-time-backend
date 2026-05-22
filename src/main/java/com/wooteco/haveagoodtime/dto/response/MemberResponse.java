package com.wooteco.haveagoodtime.dto.response;

import com.wooteco.haveagoodtime.domain.Member;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "현재 로그인한 회원 응답")
public record MemberResponse(
        @Schema(description = "회원 ID", example = "1")
        Long id,

        @Schema(description = "크루 닉네임", example = "코덱스")
        String crewNickname,

        @Schema(description = "GitHub 프로필 이미지 URL")
        String githubProfileImageUrl
) {

    public static MemberResponse from(Member member) {
        return new MemberResponse(
                member.getId(),
                member.getCrewNickname(),
                member.getGithubProfileImageUrl()
        );
    }
}
