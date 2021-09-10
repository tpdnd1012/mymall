package com.megait.mymall.controller;

import com.google.gson.JsonObject;
import com.megait.mymall.domain.Album;
import com.megait.mymall.domain.Book;
import com.megait.mymall.domain.Item;
import com.megait.mymall.domain.Member;
import com.megait.mymall.repository.ItemRepository;
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
    private final ItemRepository itemRepository;

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

    @GetMapping("/item/detail/{id}")
    public String itemdetail(Model model, @PathVariable Long id){

        Item item = itemService.findItem(id);

        model.addAttribute("item", item); // "item", item

        return "/item/detail";

    }

    @GetMapping("/item/like/{id}")
    @ResponseBody  // 리턴값을 뷰이름으로 인식하지 말고(포워드 하지 말라!),
    // 리턴값 자체를 response body에 넣어서 응답하라!
    public String itemLike(@PathVariable Long id,
                           @CurrentMember Member member,
                           Model model) {
        String resultCode = "";
        String message = "";

        // 현재 인증된 사용자의 likes에 해당 상품을 추가한다.
        // 예외상황
        //   - 로그인을 안했을 때.
        //      (resultCode: "error.auth"  message: "로그인이 필요한 서비스입니다.")
        //   - 미등록 상품일 때.
        //      (resultCode: "error.invalid"  message: "잘못된 상품 번호입니다.")
        //   - 이미 찜한 상품일 때.
        //      (resultCode: "error.duplicate"  message: "이미 찜한 상품입니다.")
        switch (itemService.addLike(member, id)){

            case ERROR_AUTH:
                resultCode = "error.auth";
                message = "로그인이 필요한 서비스입니다.";
                break;

            case ERROR_INVALID:
                resultCode = "error.invalid";
                message = "잘못된 상품 번호입니다.";
                break;

            case ERROR_DUPLICATE:
                resultCode = "error.duplicate";
                message = "이미 찜한 상품입니다.";
                break;

            case OK:
                resultCode = "ok";
                message = "찜목록에 추가하였습니다.";
                break;

        }

        // 응답해줄 JSON 객체 생성 및 설정
        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("resultCode", resultCode);
        jsonObject.addProperty("message", message);
        log.info("jsonObject.toString() : {}", jsonObject.toString());

        // JSON 객체를 String 형태로 리턴.
        return jsonObject.toString();
    }

    @GetMapping("/item/like-list")
    public String likeList(@CurrentMember Member member, Model model) {

        List<Item> list = itemService.getLikeList(member);

        model.addAttribute("likeList", list);

        return "item/like";

    }

}
