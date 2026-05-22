package com.wooteco.haveagoodtime.security;

import com.wooteco.haveagoodtime.domain.Member;
import com.wooteco.haveagoodtime.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        Long githubUserId = ((Number) oAuth2User.getAttribute("id")).longValue();
        String crewNickname = oAuth2User.getAttribute("login");
        String githubProfileImageUrl = oAuth2User.getAttribute("avatar_url");

        Member member = memberRepository.findByGithubUserId(githubUserId)
                .map(existing -> {
                    existing.update(crewNickname, githubProfileImageUrl);
                    return existing;
                })
                .orElseGet(() -> memberRepository.save(
                        new Member(githubUserId, crewNickname, githubProfileImageUrl)));

        return new CustomOAuth2User(oAuth2User, member.getId());
    }
}
