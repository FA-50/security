package io.wjdtn747.formbased.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {
    // SecurityFilterChain을 생성 후 Spring Bean으로 등록
    // HttpSecurity 를 의존성 주입
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity){
        return httpSecurity
                // `체이닝 방식`은 현재 비권고이고, `람다방식`으로 설정을 수행해야함.
                // 각각 cors와 csrf에 대한 정책을 지정하도록 설정할 수 있음.
                .cors(cors -> cors.disable())
                .csrf(csrf-> csrf.disable())
                // 폼 로그인 관련 설정
                .formLogin(
//                        form ->{
//                            //
//                            form.usernameParameter("loginAccount")
//                            .passwordParameter("loginPwd")
//                            .loginPage("/login-page")
//                            .loginProcessingUrl("/login")
//                            .successForwardUrl("/")
//                                    // 로그인 하는 도중 페이지를 변화시키는 경우
//
//                            .defaultSuccessUrl("/") // 로그인 성공 시 영구적으로 페이지 이동을 하는 경우
//                            // 직전페이지를 기억하므로 로그인 시 직전페이지로 이동할 수 있음.
//                            // 직전페이지가 없는 경우 .defaultSuccessUrl("다음페이지html경로")로 설정하여
//                            // 영구적으로 다음 페이지로 이동하도록 설정
//                                    .failureUrl("/login-page?error=true") // 로그인 실패 시
//                                    // Query Parameter를 표시하고 싶은 경우
//                                    .failureForwardUrl("/login-page?error=true");
//                                    // URL 주소는 고정하고 보여주는 페이지를 변경하는 것
                        //}
                        Customizer.withDefaults() // 스프링에서 기본 지정한 설정으로 설정
                )
                .logout(logOutConfig->{
//                    logOutConfig.logoutUrl("/logout") // 로그아웃 URL
//                            .invalidateHttpSession(true) // 로그아웃 시 Session을 없앨건지 여부 설정
//                            .deleteCookies("JSESSIONID") // 해당 쿠키를 삭제
//                            .clearAuthentication(true) // 인증정보 삭제 여부 결정

                    Customizer.withDefaults(); // 스프링 기본 설정
                })
                // 인가검사 수행
                .authorizeHttpRequests(
                        auth -> {
                            // URL 패턴에 대해 접속 시 인가 검사를 수행
                            // antMatchers() : url 전체를 기입하고 모두 일치해야 허용
                            // requestMatcher()의 경우 url 패턴 입력시 URL 패턴에 해당하는 경우 허락하도록 설정
                            // ex ) /login/** ....
                            auth.requestMatchers("/login-page")
                                    // 인가 검사 메서드
//                                    .anonymous() // 인증이 안된 사람들만 접속할 수 있도록 설정
                            // 인증된 사람은 차단
                            // ▶ 보통 로그인 페이지에서 작성 ( 로그인 한 사람이 또 로그인 하면 안되므로 )
//                                    .permitAll() // 인증안되거나 인증되거나 상관없이 모두 접근 가능
//                                    .hasAnyRole() // ROLE 가변적으로 받음 "ROLE_역할명"
//                                    .hasAnyAuthority() // ROLE 가변인자로 받음  "역할명"으로
//                                    .hasRole() // ROLE 하나만 받음 "ROLE_역할명"
//                                    .hasAuthority() "역할명" -> "ROLE_역할명"으로 반영됨.
                        }
                )
                .build();
    }




    // UserDetailsService 설정
    // 설정하지 않으면 Default값이 자동으로 등록됨.
    // 커스텀해서 설정하기.
    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder bcrypt){
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();



        // 커스텀한 UserDetails 계정이 넣어지면서 Spring 기본계정이 도출되지않음.
        // 이떄, 반드시 비밀번호는 암호화해서 저장되어야함.
        // 만약 평문으로 들어가면 Spring Framework 로그인이 안됨.
        manager.createUser(
                User.withUsername("wjdtn")
                        .password(bcrypt.encode("wjd747"))
                        .authorities("ROLE_권한명1", "ROLE_권한명2")
                        .roles("권한명1", "권한명2")
                        .build()
        );

        return manager;
    }


}
