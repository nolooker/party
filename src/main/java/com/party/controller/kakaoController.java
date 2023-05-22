package com.party.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class kakaoController {

    // 1번 카카오톡에 사용자 코드 받기(jsp의 a태그 href에 경로 있음)
    @RequestMapping(value = "/kakaoLogin", method = RequestMethod.GET)
    public ModelAndView kakaoLogin(@RequestParam(value = "code", required = false) String code) throws Throwable {

        // 1번
        System.out.println("code:" + code);
        return null;
        // return에 페이지를 해도 되고, 여기서는 코드가 넘어오는지만 확인할거기 때문에 따로 return 값을 두지는 않았음

    }
}