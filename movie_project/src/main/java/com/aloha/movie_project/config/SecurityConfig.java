package com.aloha.movie_project.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import com.aloha.movie_project.security.CustomAccessDeniedHandler;
import com.aloha.movie_project.security.LoginFailureHandler;
import com.aloha.movie_project.security.LoginSuccesHandler;
import com.aloha.movie_project.service.UserDetailServiceImpl;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true,securedEnabled = true)
public class SecurityConfig {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private UserDetailServiceImpl userDetailServiceImpl;

    @Autowired
    private LoginSuccesHandler loginSuccesHandler;

    @Autowired
    private LoginFailureHandler loginFailureHandler;

    @Autowired
    private CustomAccessDeniedHandler customAccessDeniedHandler;

    // 스프링 시큐리티 설정 메소드
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // ✅ 인가 설정
        http.authorizeHttpRequests(auth -> auth
                    .requestMatchers("/admin","/admin/**").hasRole("ADMIN")
                    .requestMatchers("/user","/user/**").hasAnyRole("USER","ADMIN")
                    .requestMatchers("/board","/board/**").permitAll()
                    .requestMatchers("/img","/img/**").permitAll()
                    .requestMatchers("/comment","/comment/**").permitAll()
                    .requestMatchers("/**").permitAll()
                    .anyRequest().permitAll()
                                            );
        
        
        
        


        // 🔐 폼 로그인 설정
        // ✅ 커스텀 로그인 페이지
        http.formLogin(login -> login.usernameParameter("id")   // 아이디 파라미터
                        .passwordParameter("pw")    // 비밀번호 파라미터
        .loginPage("/login")    //로그인 페이지 경로
        .loginProcessingUrl("/login")   // 로그인 요청 경로
        //.defaultSuccessUrl("/?success") // 로그인 성공 경로
        .successHandler(loginSuccesHandler) //로그인 성공 처리자 설정
        .failureHandler(loginFailureHandler)
        //.failureUrl("/login?error")
        );

        // ✅ 사용자 정의 인증 설정
        http.userDetailsService(userDetailServiceImpl);

        // 폼 로그인 설정
        http.formLogin(login -> login.permitAll());
        // 🔄 자동 로그인 설정
        http.rememberMe(me -> me.key("aloha")
                                // .rememberMeParameter("auto-login")
                                .tokenRepository(tokenRepository())
                                .tokenValiditySeconds(60 * 60 * 24 * 7));    //7일 유효기간
        
        // 인증 예외 처리
        http.exceptionHandling(exception -> exception
                    .accessDeniedHandler(customAccessDeniedHandler)
        
        );                        
        http.logout(logout -> logout
                            .logoutUrl("/logout")
                            .logoutSuccessUrl("/login?logout")
                            .invalidateHttpSession(true)
                            .deleteCookies("remember-id")
                            );
        return http.build();
    }
    
    /**
     * 비밀번호 암호화 빈 등록
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * AuthenticationManager 인증 관리자 빈 등록
     * @param authenticationConfiguration
     * @return
     * @throws Exception
     */
    @Bean
    public AuthenticationManager AuthenticationManager( AuthenticationConfiguration authenticationConfiguration) throws Exception{
        
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * JDBC 인증 방식 빈 등록
     */
    // @Bean
    // public UserDetailsService userDetailsService() {
    // JdbcUserDetailsManager userDetailsManager 
    //         = new JdbcUserDetailsManager(dataSource);

    // // 사용자 인증 쿼리
    // String sql1 = " SELECT username, password, enabled "
    //             + " FROM user "
    //             + " WHERE username = ? "
    //             ;
    // // 사용자 권한 쿼리
    // String sql2 = " SELECT username, auth "
    //             + " FROM user_auth "
    //             + " WHERE username = ? "
    //             ;
    // userDetailsManager.setUsersByUsernameQuery(sql1);
    // userDetailsManager.setAuthoritiesByUsernameQuery(sql2);
    // return userDetailsManager;
    // }

    /**
    * 🍃 자동 로그인 저장소 빈 등록
    * ✅ 데이터 소스
    * ⭐ persistent_logins 테이블 생성
            create table persistent_logins (
                username varchar(64) not null
                , series varchar(64) primary key
                , token varchar(64) not null
                , last_used timestamp not null
            );
    * 🔄 자동 로그인 프로세스
    * ✅ 로그인 시 
    *     ➡ 👩‍💼(ID, 시리즈, 토큰) 저장
    * ✅ 로그아웃 시, 
    *     ➡ 👩‍💼(ID, 시리즈, 토큰) 삭제
    * @return
    */
    @Bean
    public PersistentTokenRepository tokenRepository() {
        // JdbcTokenRepositoryImpl : 토큰 저장 데이터 베이스를 등록하는 객체
        JdbcTokenRepositoryImpl repositoryImpl = new JdbcTokenRepositoryImpl();
        // ✅ 토큰 저장소를 사용하는 데이터 소스 지정
        // - 시큐리티가 자동 로그인 프로세스를 처리하기 위한 DB를 지정합니다.
        repositoryImpl.setDataSource(dataSource);   
        // persistent_logins 테이블 생성
        try {
            repositoryImpl.getJdbcTemplate().execute(JdbcTokenRepositoryImpl.CREATE_TABLE_SQL);
        } 
        catch (BadSqlGrammarException e) {
            log.error("persistent_logins 테이블이 이미 존재합니다.");   
        }
        catch (Exception e) {
            log.error("자동 로그인 테이블 생성 중 , 예외 발생");
        }
        return repositoryImpl;
    }


}
