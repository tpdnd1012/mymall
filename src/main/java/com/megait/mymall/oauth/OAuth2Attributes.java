package com.megait.mymall.oauth;

import com.megait.mymall.domain.AuthType;
import com.megait.mymall.domain.Member;
import com.megait.mymall.domain.MemberType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Getter
@Builder
@AllArgsConstructor
@Slf4j
public class OAuth2Attributes {
    private final Map<String, Object> attributes;
    private final String nameAttributeKey;
    private final String name;
    private final String email;
    private final String picture;
    private final AuthType authType;


    public static OAuth2Attributes of(String registeredId, String userNameAttributeName, Map<String, Object> attributes) {
        if ("naver".equals(registeredId)) {
            return ofNaver("id", attributes);
        }
        return ofGoogle(userNameAttributeName, attributes);
    }

    private static OAuth2Attributes ofNaver(String id, Map<String, Object> attributes) {

        log.info("Naver 로 로그인!");
        log.info("userNameAttributeName : {}", id);
        log.info("attributes : {}", attributes);
        log.info("attributes.get(\"response\") : {}", attributes.get("response"));

        Map<String, Object> response = (Map<String, Object>) attributes.get("response");

        return OAuth2Attributes.builder()
                .name((String) response.get("name"))
                .email((String) response.get("email"))
                .picture((String) response.get("profile_image"))
                .authType(AuthType.NAVER)
                .attributes(response)
                .nameAttributeKey(id)
                .build();

    }

    private static OAuth2Attributes ofGoogle(String id, Map<String, Object> attributes) {
        log.info("Google 로 로그인!");
        log.info("userNameAttributeName : {}", id);
        log.info("attributes : {}", attributes);
        log.info("attributes.get(\"response\") : {}", attributes.get("response"));

        return OAuth2Attributes.builder()
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .picture((String) attributes.get("picture"))
                .authType(AuthType.GOOGLE)
                .attributes(attributes)
                .nameAttributeKey(id)
                .build();
    }

    public Member toMember(){
        return Member.builder()
                .email(email)
                .memberType(MemberType.ROLE_USER)
                .emailVerified(true)
                .password("{noop}")
                .name(name)
                .picture(picture)
                .authType(authType)
                .build();
    }
}
