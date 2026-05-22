package com.wooteco.haveagoodtime.service;

import com.wooteco.haveagoodtime.domain.Member;
import com.wooteco.haveagoodtime.dto.response.MemberResponse;
import com.wooteco.haveagoodtime.exception.HaveagoodtimeException;
import com.wooteco.haveagoodtime.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public MemberResponse getMe(Long memberId) {
        return MemberResponse.from(findMemberById(memberId));
    }

    public void updateCrewNickname(Long memberId, String crewNickname) {
        String trimmed = crewNickname == null ? "" : crewNickname.trim();
        if (trimmed.length() < 2 || trimmed.length() > 20) {
            throw new HaveagoodtimeException("닉네임은 2자 이상 20자 이하여야 합니다.", HttpStatus.BAD_REQUEST);
        }
        findMemberById(memberId).updateCrewNickname(trimmed);
    }

    private Member findMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new HaveagoodtimeException("회원을 찾을 수 없습니다.", HttpStatus.NOT_FOUND));
    }
}
