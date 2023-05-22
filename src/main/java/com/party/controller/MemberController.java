package com.party.controller;

import com.party.dto.MemberDto;
import com.party.dto.MemberFormDto;
import com.party.entity.Member;
import com.party.repository.MemberRepository;
import com.party.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;









    @GetMapping(value = "/update/{id}")
    public String getUpdateForm(@PathVariable("id")Long id, Model model){
        Member member = memberService.SelectOne(id);
        model.addAttribute("member", member);
        return "/member/memberUpdate";
    }


    @RequestMapping(value = "/detail/{id}")
    public String SelectOne(@PathVariable("id") Long id, Model model) {  // 상세 보기
        Member member = memberService.SelectOne(id);
        model.addAttribute("member", member);
        return "member/memberDetail";

    }

    @GetMapping(value = "/list") // 회원목록
    public String SelectAll(Model model){
        List<Member> memberList = memberService.SelectAll();
        model.addAttribute("list", memberList) ;
        return "member/memberList" ;
    }

    @GetMapping("/new")
    public String memberForm(Model model){
        //타임 리프에서 사용할 객체 memberFormDto를 바인딩 합니다.
        model.addAttribute("memberFormDto",new MemberFormDto());
        return "member/memberForm";

    }
    @PostMapping("/new")
    public String newMember(@Valid MemberFormDto memberFormDto, BindingResult bindingResult,Model model){
        if(bindingResult.hasErrors()){
            return "member/memberForm";
        }
        try {
            Member member =Member.createMember(memberFormDto,passwordEncoder);
            memberService.saveMember(member);
        }catch (IllegalStateException e){
            model.addAttribute("errMessage",e.getMessage());

            return "/member/memberForm";

        }
        System.out.println("포스트 방식 요청 들어옴");
        return "redirect:/"; //메인 페이지로 가주세요
    }
    // form 태그와 SecurityConfig.java파일에 정의되어 있습니다.
    @GetMapping(value = "/login")
    public String loginMember(Model model){
        return "/member/memberForm";

    }
    @GetMapping(value = "/login/error")
    public String loginError(Model model){
        model.addAttribute("loginErrorMsg","이메일 또는 비밀번호를 확인해 주세요");
        model.addAttribute("memberFormDto",new MemberFormDto());
        return "/member/memberForm";

    }

	@PostMapping(value = "/update") // 회원 정보 수정
    public String doPostUpdate(Member member){

        int cnt = -999;
        cnt = memberService.Update(member);
        System.out.println("cnt : " + cnt);

        if (cnt==1){
            return "redirect:/";
        }else{
            return "/member/memberUpdate";

        }
    }


    @PostMapping(value = "/delete/{id}") // 회원 탈퇴
    public String Delete(@PathVariable("id")Long id){ // 삭제 하기
        int cnt = -999;
        cnt = memberService.Delete(id);
        return  "redirect:/members/logout";
    }



    @GetMapping(value = "/hostlist/{category}") // 호스트 등업신청 리스트
    public String findByCategoryEquals(@PathVariable("category") String category, Model model) {
        List<MemberDto> memberList = memberRepository.findByCategoryAndRole(category);
        model.addAttribute("list", memberList);
        return "member/memberHostList";
    }

    //호스트 승인 요청 메뉴에서 승인하는 코딩
    @GetMapping(value = "/approve/{id}")
    public String approveHost(@PathVariable("id") Long id) {
        try {
            memberService.updateHostRole(id);
            // 업데이트 작업 수행 후 적절한 응답 처리
            return "redirect:/members/hostlist/host"; //호스트 등업신청 리스트
        } catch (Exception e) {
            // 예외가 발생한 경우 오류 처리 로직을 구현합니다.
            return "errorPage"; // 오류 페이지로 이동하거나 오류 메시지를 표시할 수 있습니다.
        }


    }

}


