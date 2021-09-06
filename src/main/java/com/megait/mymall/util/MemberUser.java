package com.megait.mymall.util;

import com.megait.mymall.domain.Member;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;

/**
 * User형이면서 동시에 getMember()를 가지고 있는 Adapter 클래스
 *  -> MemberService의 loadUserByUsername()에서
 *     User를 리턴하는 대신 이 객체를 리턴할 목적
 */
@Getter
public class MemberUser extends User {

    private final Member member;

    public MemberUser(Member member){
        super(
                member.getEmail(),
                member.getPassword(),
                List.of(new SimpleGrantedAuthority(member.getMemberType().name()))
        );
        this.member = member;
    }

}
