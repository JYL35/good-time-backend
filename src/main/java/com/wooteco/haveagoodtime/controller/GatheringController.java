package com.wooteco.haveagoodtime.controller;

import com.wooteco.haveagoodtime.dto.request.GatheringCreateRequest;
import com.wooteco.haveagoodtime.dto.request.GatheringUpdateRequest;
import com.wooteco.haveagoodtime.dto.response.GatheringDetailResponse;
import com.wooteco.haveagoodtime.dto.response.GatheringSummaryResponse;
import com.wooteco.haveagoodtime.dto.response.ParticipantResponse;
import com.wooteco.haveagoodtime.security.CustomOAuth2User;
import com.wooteco.haveagoodtime.service.GatheringService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/gatherings")
@RequiredArgsConstructor
public class GatheringController {

    private final GatheringService gatheringService;

    @GetMapping
    public ResponseEntity<List<GatheringSummaryResponse>> getGatherings() {
        return ResponseEntity.ok(gatheringService.getGatherings());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GatheringDetailResponse> getGathering(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomOAuth2User currentUser) {
        Long memberId = currentUser != null ? currentUser.getMemberId() : null;
        return ResponseEntity.ok(gatheringService.getGathering(id, memberId));
    }

    @PostMapping
    public ResponseEntity<Void> createGathering(@RequestBody GatheringCreateRequest request,
                                                @AuthenticationPrincipal CustomOAuth2User currentUser) {
        Long id = gatheringService.createGathering(request, currentUser.getMemberId());
        return ResponseEntity.created(URI.create("/api/gatherings/" + id)).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateGathering(@PathVariable Long id,
                                                @RequestBody GatheringUpdateRequest request,
                                                @AuthenticationPrincipal CustomOAuth2User currentUser) {
        gatheringService.updateGathering(id, request, currentUser.getMemberId());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGathering(@PathVariable Long id,
                                                @AuthenticationPrincipal CustomOAuth2User currentUser) {
        gatheringService.deleteGathering(id, currentUser.getMemberId());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/participate")
    public ResponseEntity<Void> participate(@PathVariable Long id,
                                            @AuthenticationPrincipal CustomOAuth2User currentUser) {
        gatheringService.participate(id, currentUser.getMemberId());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/participate")
    public ResponseEntity<Void> cancelParticipation(@PathVariable Long id,
                                                    @AuthenticationPrincipal CustomOAuth2User currentUser) {
        gatheringService.cancelParticipation(id, currentUser.getMemberId());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/participate")
    public ResponseEntity<List<ParticipantResponse>> getParticipants(@PathVariable Long id) {
        return ResponseEntity.ok(gatheringService.getParticipants(id));
    }
}
