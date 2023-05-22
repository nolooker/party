package com.party.controller;

import com.party.dto.MemberDto;
import com.party.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final MemberRepository memberRepository ;

    @RequestMapping(value = "/")
    public String main(HttpSession session) {
        String sessionId = "User";
        if (session.getAttribute(sessionId) == null) { // 세션 영역에 데이터가 없을 때만 세션에 바인딩 합니다.
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication != null && authentication.isAuthenticated()) { // 유저 정보가 null일 때 대비
                Object principal = authentication.getPrincipal();

                if (principal instanceof User) {
                    User user = (User) principal;
                    String email = user.getUsername(); // 로그인한 유저의 id 값을 가져옵니다.


                    MemberDto result = memberRepository.getMemberInfo(email);
                    // 유저 정보를 데이터베이스와 연동하여 필요한 값만 가져옵니다.

                    if (result != null) {
                        System.out.println("세션 영역에 데이터를 바인딩합니다.");
                        session.setAttribute(sessionId, result);
                    }
                }
            }
        }
        return "main";
    }
}
