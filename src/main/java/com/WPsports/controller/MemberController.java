package com.WPsports.controller;


import com.WPsports.entity.Member;
import com.WPsports.dto.MemberForm;
import com.WPsports.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;


@Slf4j
@Controller
public class MemberController {

    @Autowired
    MemberService memberService;

    @GetMapping("/")
    public String signupForm(){
        return "members/login";
    }

    @PostMapping("/signup")
    public String saveMember(@Valid MemberForm memberForm, Errors errors, Model model){
        if(errors.hasErrors()){
//            회원가입 실패시 입력 데이터 값을 유지
            model.addAttribute("memberForm",memberForm);
//            유효성 통과 못한 필드와 메세지를 핸들링
            Map<String,String> validatorResult = memberService.validateHandling(errors);
            for(String key : validatorResult.keySet()){
                model.addAttribute(key,validatorResult.get(key));
            }
//            회원가입 페이지로 리턴
            return "members/login";
        }
        model.addAttribute("MemberForm",memberForm);
        Member saveMember=memberService.save(memberForm);
        log.info("Entity id={} name={}",saveMember.getId(),saveMember.getName());
        return "members/welcomemember";
    }

    @PostMapping("/signup/checkID")
    @ResponseBody
    public int checkId(String id){
        if(id.length()==0){
            return 3;
        }
        if(id.length()<2) {
            return 2;
        }
        if(memberService.checkId(id)) {
            return 1;
        }else{
            return 0;
        }
    }

    @GetMapping("/login")
    public String loginForm(){
        return "loginForm";
    }

    @PostMapping("/login")
    public String loginDO(String id,String pw,Model model){
        log.info("id={},pw={}",id,pw);
        if(memberService.loginDO(id,pw,model)){
            return "loginSuccess";
        }else {
            return "loginForm";
        }

    }

}

