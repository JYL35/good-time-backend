package com.wooteco.haveagoodtime.repository;

import com.wooteco.haveagoodtime.domain.Gathering;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GatheringRepository extends JpaRepository<Gathering, Long> {
}
