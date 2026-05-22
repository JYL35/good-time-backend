package com.wooteco.haveagoodtime.repository;

import com.wooteco.haveagoodtime.domain.Gathering;
import com.wooteco.haveagoodtime.domain.Member;
import com.wooteco.haveagoodtime.domain.Participant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {

    List<Participant> findByGatheringOrderByJoinTime(Gathering gathering);

    Optional<Participant> findByGatheringAndMember(Gathering gathering, Member member);

    boolean existsByGatheringAndMember(Gathering gathering, Member member);

    boolean existsByGatheringAndMember_Id(Gathering gathering, Long memberId);

    int countByGathering(Gathering gathering);

    void deleteByGathering(Gathering gathering);
}
