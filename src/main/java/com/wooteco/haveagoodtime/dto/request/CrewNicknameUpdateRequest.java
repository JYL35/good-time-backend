package com.wooteco.haveagoodtime.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "크루 닉네임 변경 요청")
public record CrewNicknameUpdateRequest(
        @Schema(description = "우테코 크루 닉네임", example = "코덱스")
        @NotBlank(message = "닉네임을 입력해주세요.")
        @Size(min = 2, max = 20, message = "닉네임은 2자 이상 20자 이하여야 합니다.")
        String crewNickname
) {
}
