package com.megait.mymall.validation;

import com.megait.mymall.domain.Member;
import com.megait.mymall.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;


@Component
@RequiredArgsConstructor
@Slf4j
public class JoinFormValidator implements Validator {

    private final MemberRepository memberRepository;

    // 이 Validator(JoinFormValidator)가 해당 클래스(clazz)를 지원하는지
    // (유효성 검사가 가능한 클래스인지)
    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(JoinFormVo.class);
            // clazz 가 JoinFormVo.class 거나, JoinFormVo.class 의 자식이면 true
    }

    @Override
    public void validate(Object target, Errors errors) {
        // target : 유효성 검사를 할 객체 (JoinFormVo)
        // errors : 유효성 검사 실패 시 에러메시지를 담을 객체

        JoinFormVo vo = (JoinFormVo) target;
        Optional<Member> optional = memberRepository.findByEmail(vo.getEmail());
        if(optional.isPresent()){
            errors.rejectValue(
                    "email",   // 검사 에러가 발생한 필드 이름
                    "duplicate.email",  // 에러 코드 (내 맘대로)
                    "이미 가입된 이메일입니다."); // 보여줄 메시지
            log.info("중복된 이메일 : {}", errors.getAllErrors());
        }
    }
}
