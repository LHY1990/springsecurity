package com.cos.security1.controller;


import com.cos.security1.model.User;
import com.cos.security1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IndexController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    //만약에 index.html 파일이 없어도 필터링을 하기때문에 로그인 화면으로 변경된다. 서버에서 화면을 걸기전에 login 요구
    @GetMapping({"","/"})
    public String index(){

        return "index";
    }

    @GetMapping("/user")
    public @ResponseBody String user(){
        return "user";
    }

    @GetMapping("/admin")
    public @ResponseBody String admin(){
        return "admin";
    }

    @GetMapping("/manager")
    public @ResponseBody String manager(){
        return "manager";
    }

    @GetMapping("/loginForm")
    public String loginForm(){
        return "loginForm";
    }

    @GetMapping("/joinForm")
    public String joinForm(){
        return "joinForm";
    }

    @PostMapping("/join")
    public String join(User user){
        System.out.println(user.toString());
        user.setRole("ROLE_USER"); //기본적인 유저등급을 만든다.
//        userRepository.save(user); //이 경우 비밀번호가 그대로 노출된다. 이렇게되면 시큐리티로 로그인 할수없다.
        String rawPassword = user.getPassword();
        System.out.println("날비번"+rawPassword);
        String encPassword = bCryptPasswordEncoder.encode(rawPassword);
        System.out.println("암호비번"+encPassword);
        user.setPassword(encPassword);
        userRepository.save(user); //이 경우엔 비밀번호가 암호화 되어서 가능하다
        return "redirect:/loginForm";
    }


    @Secured("ROLE_ADMIN") //@EnableGlobalMethodSecurity(securedEnabled = true) 와 연동된다. 어드민 접근만가능. 1개의 권한만 체크
    @GetMapping("/info")
    public @ResponseBody String info(){
        return "개인정보";
    }

    @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')") //@EnableGlobalMethodSecurity(prePostEnabled = true) 와 연동된다.함수실행 이전에 처리하는것.
//    @PostAuthorize() @EnableGlobalMethodSecurity(prePostEnabled = true) 와 연동된다. 매서드 실행이후 처리한다. 이건 잘 안쓴다.
    @GetMapping("/data")
    public @ResponseBody String data(){
        return "데이터정보";
    }


}
