package com.wooteco.haveagoodtime.controller;

import com.wooteco.haveagoodtime.dto.request.CrewNicknameUpdateRequest;
import com.wooteco.haveagoodtime.dto.response.MemberResponse;
import com.wooteco.haveagoodtime.security.CustomOAuth2User;
import com.wooteco.haveagoodtime.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
@Tag(name = "Member", description = "회원 API")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/me")
    @Operation(summary = "내 정보 조회", description = "현재 로그인한 회원 정보를 조회합니다.")
    public ResponseEntity<MemberResponse> getMe(
            @Parameter(hidden = true) @AuthenticationPrincipal CustomOAuth2User currentUser) {
        return ResponseEntity.ok(memberService.getMe(currentUser.getMemberId()));
    }

    @PatchMapping("/nickname")
    @Operation(summary = "크루 닉네임 변경", description = "현재 로그인한 회원의 크루 닉네임을 변경합니다.")
    public ResponseEntity<Void> updateCrewNickname(
            @Valid @RequestBody CrewNicknameUpdateRequest request,
            @Parameter(hidden = true) @AuthenticationPrincipal CustomOAuth2User currentUser) {
        memberService.updateCrewNickname(currentUser.getMemberId(), request.crewNickname());
        return ResponseEntity.ok().build();
    }
}
