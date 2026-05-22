package com.wooteco.haveagoodtime.service;

import com.wooteco.haveagoodtime.domain.Gathering;
import com.wooteco.haveagoodtime.domain.Member;
import com.wooteco.haveagoodtime.domain.Participant;
import com.wooteco.haveagoodtime.dto.request.GatheringCreateRequest;
import com.wooteco.haveagoodtime.dto.request.GatheringUpdateRequest;
import com.wooteco.haveagoodtime.dto.response.GatheringDetailResponse;
import com.wooteco.haveagoodtime.dto.response.GatheringSummaryResponse;
import com.wooteco.haveagoodtime.dto.response.ParticipantResponse;
import com.wooteco.haveagoodtime.exception.HaveagoodtimeException;
import com.wooteco.haveagoodtime.repository.GatheringRepository;
import com.wooteco.haveagoodtime.repository.MemberRepository;
import com.wooteco.haveagoodtime.repository.ParticipantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class GatheringService {

    private final GatheringRepository gatheringRepository;
    private final MemberRepository memberRepository;
    private final ParticipantRepository participantRepository;

    @Transactional(readOnly = true)
    public List<GatheringSummaryResponse> getGatherings() {
        return gatheringRepository.findAll().stream()
                .map(GatheringSummaryResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public GatheringDetailResponse getGathering(Long id) {
        Gathering gathering = findGatheringById(id);
        int count = participantRepository.findByGathering(gathering).size();
        return GatheringDetailResponse.from(gathering, count);
    }

    public Long createGathering(GatheringCreateRequest request, Long hostId) {
        Member host = findMemberById(hostId);
        Gathering gathering = new Gathering(
                request.name(), request.headCount(), request.gatheringDatetime(),
                request.dueDate(), request.description(), host
        );
        return gatheringRepository.save(gathering).getId();
    }

    public void updateGathering(Long id, GatheringUpdateRequest request, Long memberId) {
        Gathering gathering = findGatheringById(id);
        if (!gathering.isHost(memberId)) {
            throw new HaveagoodtimeException("모임 방장만 수정할 수 있습니다.", HttpStatus.FORBIDDEN);
        }
        gathering.update(request.name(), request.headCount(), request.gatheringDatetime(),
                request.dueDate(), request.description());
    }

    public void deleteGathering(Long id, Long memberId) {
        Gathering gathering = findGatheringById(id);
        if (!gathering.isHost(memberId)) {
            throw new HaveagoodtimeException("모임 방장만 삭제할 수 있습니다.", HttpStatus.FORBIDDEN);
        }
        gatheringRepository.delete(gathering);
    }

    public void participate(Long id, Long memberId) {
        Gathering gathering = findGatheringById(id);
        Member member = findMemberById(memberId);
        if (participantRepository.existsByGatheringAndMember(gathering, member)) {
            throw new HaveagoodtimeException("이미 참여한 모임입니다.", HttpStatus.CONFLICT);
        }
        participantRepository.save(new Participant(gathering, member));
    }

    public void cancelParticipation(Long id, Long memberId) {
        Gathering gathering = findGatheringById(id);
        Member member = findMemberById(memberId);
        Participant participant = participantRepository.findByGatheringAndMember(gathering, member)
                .orElseThrow(() -> new HaveagoodtimeException("참여하지 않은 모임입니다.", HttpStatus.NOT_FOUND));
        participantRepository.delete(participant);
    }

    @Transactional(readOnly = true)
    public List<ParticipantResponse> getParticipants(Long id) {
        Gathering gathering = findGatheringById(id);
        return participantRepository.findByGathering(gathering).stream()
                .map(ParticipantResponse::from)
                .toList();
    }

    private Gathering findGatheringById(Long id) {
        return gatheringRepository.findById(id)
                .orElseThrow(() -> new HaveagoodtimeException("모임을 찾을 수 없습니다.", HttpStatus.NOT_FOUND));
    }

    private Member findMemberById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new HaveagoodtimeException("회원을 찾을 수 없습니다.", HttpStatus.NOT_FOUND));
    }
}
