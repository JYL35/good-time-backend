package com.wooteco.haveagoodtime.controller;

import com.wooteco.haveagoodtime.dto.request.GatheringCreateRequest;
import com.wooteco.haveagoodtime.dto.request.GatheringUpdateRequest;
import com.wooteco.haveagoodtime.dto.response.ErrorResponse;
import com.wooteco.haveagoodtime.dto.response.GatheringDetailResponse;
import com.wooteco.haveagoodtime.dto.response.GatheringSummaryResponse;
import com.wooteco.haveagoodtime.dto.response.ParticipantResponse;
import com.wooteco.haveagoodtime.security.CustomOAuth2User;
import com.wooteco.haveagoodtime.service.GatheringService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/gatherings")
@RequiredArgsConstructor
@Tag(name = "Gathering", description = "모임 API")
public class GatheringController {

    private static final String ERROR_MEDIA_TYPE = "application/json";

    private final GatheringService gatheringService;

    @GetMapping
    @Operation(summary = "모임 목록 조회", description = "등록된 모든 모임의 요약 정보를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "모임 목록 조회 성공")
    @SecurityRequirements
    public ResponseEntity<List<GatheringSummaryResponse>> getGatherings() {
        return ResponseEntity.ok(gatheringService.getGatherings());
    }

    @GetMapping("/{id}")
    @Operation(summary = "모임 상세 조회", description = "모임 ID로 상세 정보와 참여자 수를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "모임 상세 조회 성공"),
            @ApiResponse(responseCode = "404", description = "모임을 찾을 수 없음",
                    content = @Content(mediaType = ERROR_MEDIA_TYPE, schema = @Schema(implementation = ErrorResponse.class)))
    })
    @SecurityRequirements
    public ResponseEntity<GatheringDetailResponse> getGathering(
            @Parameter(description = "모임 ID", example = "1") @PathVariable Long id,
            @Parameter(hidden = true) @AuthenticationPrincipal CustomOAuth2User currentUser) {
        Long memberId = currentUser != null ? currentUser.getMemberId() : null;
        return ResponseEntity.ok(gatheringService.getGathering(id, memberId));
    }

    @PostMapping
    @Operation(summary = "모임 생성", description = "로그인한 사용자를 방장으로 새 모임을 생성합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "모임 생성 성공"),
            @ApiResponse(responseCode = "401", description = "인증 필요"),
            @ApiResponse(responseCode = "404", description = "회원을 찾을 수 없음",
                    content = @Content(mediaType = ERROR_MEDIA_TYPE, schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<Void> createGathering(@RequestBody GatheringCreateRequest request,
                                                @Parameter(hidden = true) @AuthenticationPrincipal CustomOAuth2User currentUser) {
        Long id = gatheringService.createGathering(request, currentUser.getMemberId());
        return ResponseEntity.created(URI.create("/api/gatherings/" + id)).build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "모임 수정", description = "방장만 모임 정보를 수정할 수 있습니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "모임 수정 성공"),
            @ApiResponse(responseCode = "401", description = "인증 필요"),
            @ApiResponse(responseCode = "403", description = "방장 권한 필요",
                    content = @Content(mediaType = ERROR_MEDIA_TYPE, schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "모임을 찾을 수 없음",
                    content = @Content(mediaType = ERROR_MEDIA_TYPE, schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<Void> updateGathering(
            @Parameter(description = "모임 ID", example = "1") @PathVariable Long id,
            @RequestBody GatheringUpdateRequest request,
            @Parameter(hidden = true) @AuthenticationPrincipal CustomOAuth2User currentUser) {
        gatheringService.updateGathering(id, request, currentUser.getMemberId());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "모임 삭제", description = "방장만 모임을 삭제할 수 있습니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "모임 삭제 성공"),
            @ApiResponse(responseCode = "401", description = "인증 필요"),
            @ApiResponse(responseCode = "403", description = "방장 권한 필요",
                    content = @Content(mediaType = ERROR_MEDIA_TYPE, schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "모임을 찾을 수 없음",
                    content = @Content(mediaType = ERROR_MEDIA_TYPE, schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<Void> deleteGathering(
            @Parameter(description = "모임 ID", example = "1") @PathVariable Long id,
            @Parameter(hidden = true) @AuthenticationPrincipal CustomOAuth2User currentUser) {
        gatheringService.deleteGathering(id, currentUser.getMemberId());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/participate")
    @Operation(summary = "모임 참여", description = "로그인한 사용자가 모임에 참여합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "모임 참여 성공"),
            @ApiResponse(responseCode = "400", description = "모집 중이 아닌 모임",
                    content = @Content(mediaType = ERROR_MEDIA_TYPE, schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "인증 필요"),
            @ApiResponse(responseCode = "404", description = "모임 또는 회원을 찾을 수 없음",
                    content = @Content(mediaType = ERROR_MEDIA_TYPE, schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "이미 참여한 모임",
                    content = @Content(mediaType = ERROR_MEDIA_TYPE, schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<Void> participate(
            @Parameter(description = "모임 ID", example = "1") @PathVariable Long id,
            @Parameter(hidden = true) @AuthenticationPrincipal CustomOAuth2User currentUser) {
        gatheringService.participate(id, currentUser.getMemberId());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/participate")
    @Operation(summary = "모임 참여 취소", description = "로그인한 사용자의 모임 참여를 취소합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "모임 참여 취소 성공"),
            @ApiResponse(responseCode = "400", description = "모집 중이 아닌 모임",
                    content = @Content(mediaType = ERROR_MEDIA_TYPE, schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "인증 필요"),
            @ApiResponse(responseCode = "404", description = "모임, 회원 또는 참여 정보를 찾을 수 없음",
                    content = @Content(mediaType = ERROR_MEDIA_TYPE, schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<Void> cancelParticipation(
            @Parameter(description = "모임 ID", example = "1") @PathVariable Long id,
            @Parameter(hidden = true) @AuthenticationPrincipal CustomOAuth2User currentUser) {
        gatheringService.cancelParticipation(id, currentUser.getMemberId());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/participate")
    @Operation(summary = "모임 참여자 목록 조회", description = "RECRUITING 상태면 익명, MATCHED 상태면 닉네임 공개.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "참여자 목록 조회 성공"),
            @ApiResponse(responseCode = "404", description = "모임을 찾을 수 없음",
                    content = @Content(mediaType = ERROR_MEDIA_TYPE, schema = @Schema(implementation = ErrorResponse.class)))
    })
    @SecurityRequirements
    public ResponseEntity<List<ParticipantResponse>> getParticipants(
            @Parameter(description = "모임 ID", example = "1") @PathVariable Long id) {
        return ResponseEntity.ok(gatheringService.getParticipants(id));
    }
}
