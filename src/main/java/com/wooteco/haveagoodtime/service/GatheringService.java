package com.wooteco.haveagoodtime.service;

import com.wooteco.haveagoodtime.domain.Gathering;
import com.wooteco.haveagoodtime.domain.GatheringStatus;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
@Transactional
public class GatheringService {

    private final GatheringRepository gatheringRepository;
    private final MemberRepository memberRepository;
    private final ParticipantRepository participantRepository;

    @Transactional(readOnly = true)
    public List<GatheringSummaryResponse> getGatherings() {
        return gatheringRepository.findAllByOrderByIdDesc().stream()
                .map(gathering -> GatheringSummaryResponse.from(
                        gathering,
                        participantRepository.countByGathering(gathering)
                ))
                .toList();
    }

    @Transactional(readOnly = true)
    public GatheringDetailResponse getGathering(Long id, Long currentMemberId) {
        Gathering gathering = findGatheringById(id);
        int count = participantRepository.countByGathering(gathering);
        boolean isHost = currentMemberId != null && gathering.isHost(currentMemberId);
        boolean isParticipating = currentMemberId != null
                && participantRepository.existsByGatheringAndMember_Id(gathering, currentMemberId);
        return GatheringDetailResponse.from(gathering, count, isHost, isParticipating);
    }

    public Long createGathering(GatheringCreateRequest request, Long hostId) {
        validateSchedule(request.gatheringDatetime(), request.dueDate());
        Member host = findMemberById(hostId);
        Gathering gathering = new Gathering(
                request.name(), request.headCount(), request.gatheringDatetime(),
                request.dueDate(), request.description(), host
        );
        Gathering savedGathering = gatheringRepository.save(gathering);
        participantRepository.save(new Participant(savedGathering, host));
        return savedGathering.getId();
    }

    public void updateGathering(Long id, GatheringUpdateRequest request, Long memberId) {
        validateSchedule(request.gatheringDatetime(), request.dueDate());
        Gathering gathering = findGatheringById(id);
        if (!gathering.isHost(memberId)) {
            throw new HaveagoodtimeException("모임 방장만 수정할 수 있습니다.", HttpStatus.FORBIDDEN);
        }
        int participantCount = participantRepository.countByGathering(gathering);
        if (request.headCount() < participantCount) {
            throw new HaveagoodtimeException("현재 참여자 수보다 적은 인원으로 수정할 수 없습니다.", HttpStatus.BAD_REQUEST);
        }
        gathering.update(request.name(), request.headCount(), request.gatheringDatetime(),
                request.dueDate(), request.description());
        if (gathering.getStatus() == GatheringStatus.RECRUITING && participantCount >= request.headCount()) {
            gathering.match();
        }
    }

    public void deleteGathering(Long id, Long memberId) {
        Gathering gathering = findGatheringById(id);
        if (!gathering.isHost(memberId)) {
            throw new HaveagoodtimeException("모임 방장만 삭제할 수 있습니다.", HttpStatus.FORBIDDEN);
        }
        participantRepository.deleteByGathering(gathering);
        gatheringRepository.delete(gathering);
    }

    public void participate(Long id, Long memberId) {
        Gathering gathering = findGatheringById(id);
        if (gathering.getStatus() != GatheringStatus.RECRUITING) {
            throw new HaveagoodtimeException("모집 중인 모임에만 참여할 수 있습니다.", HttpStatus.BAD_REQUEST);
        }
        Member member = findMemberById(memberId);
        if (participantRepository.existsByGatheringAndMember(gathering, member)) {
            throw new HaveagoodtimeException("이미 참여한 모임입니다.", HttpStatus.CONFLICT);
        }
        participantRepository.save(new Participant(gathering, member));

        int currentCount = participantRepository.countByGathering(gathering);
        if (currentCount >= gathering.getHeadCount()) {
            gathering.match();
        }
    }

    public void cancelParticipation(Long id, Long memberId) {
        Gathering gathering = findGatheringById(id);
        if (gathering.getStatus() != GatheringStatus.RECRUITING) {
            throw new HaveagoodtimeException("모집 중인 모임에서만 참여를 취소할 수 있습니다.", HttpStatus.BAD_REQUEST);
        }
        if (gathering.isHost(memberId)) {
            throw new HaveagoodtimeException("방장은 참여를 취소할 수 없습니다. 모임을 삭제해주세요.", HttpStatus.BAD_REQUEST);
        }
        Member member = findMemberById(memberId);
        Participant participant = participantRepository.findByGatheringAndMember(gathering, member)
                .orElseThrow(() -> new HaveagoodtimeException("참여하지 않은 모임입니다.", HttpStatus.NOT_FOUND));
        participantRepository.delete(participant);
    }

    @Transactional(readOnly = true)
    public List<ParticipantResponse> getParticipants(Long id) {
        Gathering gathering = findGatheringById(id);
        List<Participant> participants = participantRepository.findByGatheringOrderByJoinTime(gathering);
        boolean revealed = gathering.getStatus() == GatheringStatus.MATCHED;

        AtomicInteger index = new AtomicInteger(1);
        return participants.stream()
                .map(p -> ParticipantResponse.of(p, index.getAndIncrement(), revealed))
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

    private void validateSchedule(LocalDateTime gatheringDatetime, LocalDateTime dueDate) {
        if (gatheringDatetime != null && dueDate != null && !dueDate.isBefore(gatheringDatetime)) {
            throw new HaveagoodtimeException("모집 마감 일시는 모임 일시보다 빨라야 합니다.", HttpStatus.BAD_REQUEST);
        }
    }
}
