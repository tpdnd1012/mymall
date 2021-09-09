package com.megait.mymall.aop;

import org.aspectj.lang.JoinPoint;

public class AuthAspect {

    // 인증되었는지 확인.. 인증이 안된 상태라면 로그인 페이지로 리다이렉트
    // --> 컨트롤러에서 로그인이 필요한 메서드들
    public void checkAuth(JoinPoint joinPoint) {



    }

}
