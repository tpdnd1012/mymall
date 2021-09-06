package com.megait.mymall.configuration;
// 시큐리티 관련 Configuration 빈

import com.megait.mymall.service.CustomOAuth2UserService;
import com.megait.mymall.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final CustomOAuth2UserService customOAuth2UserService;

    private final MemberService memberService;

    private final DataSource dataSource; // DBCP



    // 웹 관련 보안 설정 (예). 방화벽)
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .requestMatchers(
                        PathRequest.toStaticResources().atCommonLocations()
                );

        web.ignoring().antMatchers("/h2-console/**");

    }


    // 요청/응답 관련 보안 설정
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // 127.0.0.1:8080/  요청은 모든 사용자에게 허용
/*        http.authorizeRequests().mvcMatchers("/").permitAll();

        http.formLogin()  // 로그인 form 사용 및 설정
            .loginPage("/login")  // 로그인 페이지 경로를 "/login"가 아닌 다른 경로로 잡고 싶을 때..
            .defaultSuccessUrl("/", true); // 로그인 성공 시 리다이렉트할 경로

        http.logout() // 로그아웃 설정
            .logoutUrl("/logout") // 로그아웃을 실행할 url. 디폴트는 "/logout"
            .invalidateHttpSession(true) // 로그아웃 완료 시 세션을 종료시킬 것인가.
            .logoutSuccessUrl("/"); // 로그아웃 성공 시 리다이렉트할 경로
*/
        http
                .authorizeRequests()
                        .mvcMatchers("/", "email-check")
                        .permitAll()

                        .antMatchers("/h2-console/**").permitAll()
                        .antMatchers("/mypage/**")
                        .authenticated()

                        .mvcMatchers(HttpMethod.GET, "email-check")
                        .permitAll()
                // mvcMatchers() : MVC 컨트롤러가 사용하는 요청 URL 패턴.
                // antMatchers() : "**"을 사용할 수 있는 URL 패턴.

                .and()
                        .formLogin()  // 로그인 form 사용 및 설정
                        .loginPage("/login")  // 로그인 페이지 경로를 "/login"가 아닌 다른 경로로 잡고 싶을 때..
                        .defaultSuccessUrl("/", true)
                .and()
                        .logout() // 로그아웃 설정
                        .logoutUrl("/logout") // 로그아웃을 실행할 url. 디폴트는 "/logout"
                        .invalidateHttpSession(true) // 로그아웃 완료 시 세션을 종료시킬 것인가.
                        .logoutSuccessUrl("/")
                .and()
                        .rememberMe()
                        .userDetailsService(memberService)
                        .tokenRepository(tokenRepository())


                .and()
                        .oauth2Login()
                        .loginPage("/login")
                        .userInfoEndpoint()
                        .userService(customOAuth2UserService)

        ;
    }

    @Bean
    public PersistentTokenRepository tokenRepository() {
        // PersistentTokenRepository : rememberMe 선택한 사용자들의 토큰값을 관리한다.
        JdbcTokenRepositoryImpl jdbcTokenRepository =
                new JdbcTokenRepositoryImpl();

        jdbcTokenRepository.setDataSource(dataSource);
        return jdbcTokenRepository;
    }
}
