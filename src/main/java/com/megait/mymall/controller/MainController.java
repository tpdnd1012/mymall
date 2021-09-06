package com.megait.mymall.controller;

import com.megait.mymall.domain.Album;
import com.megait.mymall.domain.Book;
import com.megait.mymall.domain.Member;
import com.megait.mymall.repository.MemberRepository;
import com.megait.mymall.service.ItemService;
import com.megait.mymall.service.MemberService;
import com.megait.mymall.util.CurrentMember;
import com.megait.mymall.validation.JoinFormValidator;
import com.megait.mymall.validation.JoinFormVo;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class MainController {


    private final Logger log = LoggerFactory.getLogger(getClass());
    private final MemberService memberService;
    private final MemberRepository memberRepository;

    private final ItemService itemService;

    @InitBinder("joinFormVo") // 요청 전에 추가할 설정들 (Controller 에서 사용)
    protected void initBinder(WebDataBinder dataBinder){
        dataBinder.addValidators(new JoinFormValidator(memberRepository));
    }


    @RequestMapping("/")
    public String index(Model model, @CurrentMember Member member){
        String message = "안녕하세요, 손님!";
        if(member != null){
            message = "안녕하세요, " + member.getName() + "님!";
        }
        model.addAttribute("member", member);
        model.addAttribute("msg", message);

        // 앨범, 도서 상품 목록을 attribute로 추가
        // 이름 : bookList, albumList

        List<Book> bookList = itemService.getBookList();
        List<Album> albumList = itemService.getAlbumList();

        model.addAttribute("bookList", bookList);
        model.addAttribute("albumList", albumList);

        return "index";
        // index.html 에서 thymeleaf 사용해서 상품의 이름, 이미지, 가격을 모두 출력
    }

    @GetMapping("/login")
    public String login(){
        return "member/login";
    }


    /*@GetMapping("/mypage")
    public String mypage(Model model, Principal principal) {
        Member member = memberRepository.findByEmail(principal.getName()).orElseThrow();
        model.addAttribute("member", member);
        return "member/mypage";
    }*/


    /*
    @GetMapping("/mypage2")
    public String mypage(Model model, @AuthenticationPrincipal User user){
        if(user != null) {
            Member member = memberRepository.findByEmail(user.getUsername()).orElseThrow();
            model.addAttribute("member", member);
        }
        return "member/mypage";
    }*/


    @RequestMapping("/mypage/{email}")
    public String mypage(Model model,
                         @CurrentMember Member member,
                         @PathVariable String email){

        if(member == null || !member.getEmail().equals(email)) {
            return "redirect:/";
        }

        model.addAttribute("member", member);
        return "member/mypage";
    }

    @GetMapping("/signup")
    public String signupForm(Model model){
        model.addAttribute("joinFormVo", new JoinFormVo());
        return "member/signup";
    }

    @PostMapping("/signup")
    public String signupSubmit(@Valid JoinFormVo joinFormVo, Errors errors){
        log.info("joinFormVo : {}", joinFormVo);
        if(errors.hasErrors()){
            log.info("회원가입 에러 : {}", errors.getAllErrors());
            return "member/signup";
        }

        log.info("회원가입 정상!");

        memberService.processNewMember(joinFormVo);

        return "redirect:/"; // "/" 로 리다이렉트
    }

    @Transactional
    @GetMapping("/email-check")
    public String emailCheck(String email, String token, Model model){

        Optional<Member> optional = memberRepository.findByEmail(email);
        boolean result;

        if(optional.isEmpty()){
            result = false;
        }
        else if(! optional.get().getEmailCheckToken().equals(token)){
            result = false;
        }
        else {
            result = true;
            optional.get().setEmailVerified(true);
        }

        model.addAttribute("email", email);
        model.addAttribute("result", result);

        return "member/email-check-result";
    }
}
