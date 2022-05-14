package com.cos.security1.repository;

import com.cos.security1.model.User;
import org.springframework.data.jpa.repository.JpaRepository;


//CRUD 함수를 JpaRepository가 들고있다.
//@Repository가 없어도 IoC된다. 어노테이션도 상속받기 때문
public interface UserRepository extends JpaRepository<User, Integer> {

    // JPA의 findBy 문법에 따라 Username을 찾는다. 이건 JpaRepository 상속시에 적용 (jpa query method 검색)
    // select * from user where username= :username
    public User findByUsername(String username);
}
