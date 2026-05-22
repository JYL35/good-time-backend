package com.wooteco.haveagoodtime.repository;

import com.wooteco.haveagoodtime.domain.Gathering;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GatheringRepository extends JpaRepository<Gathering, Long> {

    List<Gathering> findAllByOrderByIdDesc();
}
