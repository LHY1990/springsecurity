package com.cos.security1.auth;
//시큐리티가 /login 요청을 낚아채서 로그인을 진행시킬때
//로그인 진행이 완료되면 시큐리티가 세션을 만들어서 관리한다. 기본 세션과 같지만 고유세션을 갖는다.(Security ContextHolder)
//이때 시큐리트의 세션에 접근이 가능한 오브젝트가 지정되어있는데 --->Authentication 객체
// Authentication 객체안에 User 정보가 있어야 한다.
//User 오브젝트 타입 =>UserDetails타입 객체

// Security Session => Authentication => UserDetails 순으로 접근해서 값을 찾는다.
// 얘는 그중에 [2단계]인 Authentication 처리

import com.cos.security1.model.User;
import com.cos.security1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

//시큐리티 설정에서 .loginProcessingUrl("/login")
// /login 요청이 오면 자동으로 UserDetailsService 타입의 IoC 되어있는 loadUserByUsername 함수가 실행된다.
@Service
public class PrincipalDatailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    // /login 요청시 자동호출. 이때 아이디를 받는 html의 form의 input에서 name이 username이 아니라면, http.authorizeRequests().usernameParameter("다른이름")으로 처리해줘야한다. 기본은 username이다. 그냥 쓰는걸 추천
    // 이해안돼면 인텔리제이 전역 찾기로 http.authorizeRequests 검색해볼것.
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //여기서 유저의 유무가 확인되어야 한다.
        User user = userRepository.findByUsername(username);

        if(user!=null){
            return new PrincipalDetails(user);
            // 이 결과값은 Security Session => Authentication => UserDetails 중에서 2단계인 Authentication에 값이들어간다
            // 쉽게 session(내부 Authentication(내부 UserDetails)) 가 처리되서 세션에 실제 값이 들어간다.
        }

        return null;
    }
}
