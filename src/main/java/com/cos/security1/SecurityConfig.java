package com.cos.security1;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity //시큐리티 설정의 활성화. 스프링 시큐리티 필터가 스프링 필터 체인에 등록된다.
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true) // [securedEnabled = true]secured 어노테이션 활성화.컨트롤러 및 전역 @Secured 를 활성화 처리, [prePostEnabled = true] @PreAuth
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    //암호화를 위한 빈등록. 해당 매서드의 리턴되는 오브젝트를 빈으로 등록한다. return 의 new 객체가 빈에 등록
    @Bean
    public BCryptPasswordEncoder encodePwd(){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable(); //Cross-Site Request Forgery(위조[포저리]). 보안을 끈다.
        http.authorizeRequests()
                .antMatchers("/user/**").authenticated() //인증만되면 들어 갈수있는 주소
                .antMatchers("/manager/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')") // /manager는 매니저와 어드민 권한이 접근가능하다.
                .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')") // //admin접근은 어드민 권한만 가능하다.
                .anyRequest().permitAll() // 이외의 접근은 모두 허용한다. 이때 다른장소는 로그인 하지않고 접근하면 403에러(서버가 클라이언트의 접근 거부)
                .and()
                .formLogin()
                .loginPage("/loginForm") //이 설정을 통해 로그인페이지를 지정할수있다.
                .loginProcessingUrl("/login") // login주소가 호출이 되면 시큐리티가 낚아채서 대신 로그인을 진행해준다.
                .defaultSuccessUrl("/"); //기본 로그인 성공 후 접근 페이지. 만약에 다른 페이지를 요청했다면 로그인후 그 페이지로 넘겨준다.(자동)
    }
}
