package com.cos.security1.auth;

//시큐리티가 /login 요청을 낚아채서 로그인을 진행시킬때
//로그인 진행이 완료되면 시큐리티가 세션을 만들어서 관리한다. 기본 세션과 같지만 고유세션을 갖는다.(Security ContextHolder)
//이때 시큐리트의 세션에 접근이 가능한 오브젝트가 지정되어있는데 --->Authentication 객체
// Authentication 객체안에 User 정보가 있어야 한다.
//User 오브젝트 타입 =>UserDetails타입 객체

// Security Session => Authentication => UserDetails 순으로 접근해서 값을 찾는다.
// 얘는 그중에 [3단계]인 UserDetails 처리

import com.cos.security1.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

//로그인을 할때 로그인하는 정보를 시큐리티의 세션으로 데이터를 받아서 처리한다.
// 이 경우 스프링객체가 아닌 new로 직접 객체를 만들어서 쓸것이다.
public class PrincipalDetails implements UserDetails {

    private User user; //콤포지션

    public PrincipalDetails(User user){ //생성자로 받아버린다.
        this.user = user;
    }

    @Override // 해당유저의 권한을 리턴하는 곳. String을 리턴해야하지만 오버라이드 매서드는 콜렉션 제네릭이다.
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>(); //ArrayList는 콜렉션의 자손
        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return user.getRole();
            }
        });
        return collection;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    //유저이름 받기
    @Override
    public String getUsername() {
        return user.getUsername();
    }

    //유저가 만료되었는가
    @Override
    public boolean isAccountNonExpired() {
        return true; //true로 변경함
    }

    //유저가 잠금 여부
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

//    비밀번호가 기간이 지나지 않았는지 오래사용했는지
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

//    계정이 활성화 상태인가
    @Override
    public boolean isEnabled() {
        //서버에서 1년 이상 로그인하지 않은경우 이걸로 처리한다 User에 loginDate 필드로 처리하면된다. 1년 넘으면 false처리
        return true;
    }
}
