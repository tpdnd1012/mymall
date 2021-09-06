package com.megait.mymall.service;

import com.megait.mymall.domain.Member;
import com.megait.mymall.oauth.MemberOAuth2User;
import com.megait.mymall.oauth.OAuth2Attributes;
import com.megait.mymall.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService {

    private final MemberRepository memberRepository;
    private final MemberService memberService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2UserService service = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = service.loadUser(userRequest);

        String registeredId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration()
                    .getProviderDetails()
                    .getUserInfoEndpoint()
                    .getUserNameAttributeName();
        OAuth2Attributes attributes = OAuth2Attributes.of(
                registeredId, userNameAttributeName, oAuth2User.getAttributes()
        );

        Member member = saveOrUpdate(attributes);

        return new MemberOAuth2User(member, attributes);

    }


    private Member saveOrUpdate(OAuth2Attributes attributes) {
        String email = attributes.getEmail();
        Member member = memberRepository.findByEmail(email).map(entity -> {
                        // 지금 oauth로 인증들어온 유저가 이미 DB에 있는 기존회원인 경우
                        entity.setName(attributes.getName());
                        entity.setPicture(attributes.getPicture());
                        return entity;
                    }).orElse(attributes.toMember()); // 신규회원인 경우

        member = memberRepository.save(member);
        memberService.login(member);
        return member;
    }
}
