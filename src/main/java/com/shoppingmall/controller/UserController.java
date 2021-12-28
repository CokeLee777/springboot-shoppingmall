package com.shoppingmall.controller;

import com.shoppingmall.dto.UserRequestDto;
import com.shoppingmall.dto.UserResponseDto;
import com.shoppingmall.exception.DuplicatedUserException;
import com.shoppingmall.exception.IncorrectLoginInfoException;
import com.shoppingmall.service.UserService;
import com.shoppingmall.web.SessionConst;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static com.shoppingmall.dto.UserRequestDto.*;

@Slf4j
@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    //회원가입 form
    @GetMapping("/sign-up")
    public String signUpForm(@ModelAttribute("user") UserRequestDto userRequestDto){
        return "user/signUpForm";
    }
    //회원가입
    @PostMapping("/sign-up")
    public String signUp(@Validated @ModelAttribute("user") UserRequestDto userRequestDto, BindingResult bindingResult){

        //검증 실패시 입력 폼으로 돌아간다.
        if(bindingResult.hasErrors()){
            log.error("errors={}", bindingResult);
            return "user/signUpForm";
        }

        //중복 아이디 체크
        try{
            userService.userRegistration(userRequestDto.toEntity());
        } catch (Exception e){
            if(e.getClass() == DuplicatedUserException.class){
                log.error("errors={}", e.getMessage());
                bindingResult.reject("duplicatedUserException", new Object[]{DuplicatedUserException.class}, null);
            }
            return "user/signUpForm";
        }

        return "home";
    }

    //로그인 form
    @GetMapping("/sign-in")
    public String signInForm(@ModelAttribute("signInForm") LoginRequestDto loginRequestDto){
        return "user/signInForm";
    }

    //로그인
    @PostMapping("/sign-in")
    public String signIn(@Validated @ModelAttribute("signInForm") LoginRequestDto loginRequestDto,
                         BindingResult bindingResult, @RequestParam(defaultValue = "/") String redirectURL,
                         HttpServletRequest request){
        //검증 오류에 문제가 있다면
        if(bindingResult.hasErrors()){
            log.error("errors={}", bindingResult);
            return "user/signInForm";
        }

        try{
            userService.login(loginRequestDto.toEntity());

        } catch (Exception e) {
            log.error("error={}", e.getMessage());
            bindingResult.reject("incorrectLoginInfoException", new Object[]{IncorrectLoginInfoException.class}, null);
            return "user/signInForm";
        }

        //세션이 있으면 세션 반환, 없으면 신규 세션 생성
        HttpSession session = request.getSession();
        //세션에 로그인 회원 정보를 보관한다.
        session.setAttribute(SessionConst.LOGIN_USER, loginRequestDto);

        return "redirect:" + redirectURL;
    }

    //로그아웃
    @PostMapping("/logout")
    public String logout(HttpServletRequest request){
        //세션을 불러온다. -> 없으면 생성하지 않는다.
        HttpSession session = request.getSession(false);
        //세션이 존재한다면 세션을 없앤다.
        if(session != null){
            session.invalidate();
        }

        return "redirect:/";
    }

    //프로필 정보
    @GetMapping("/profile")
    public String myProfile(HttpServletRequest request, Model model){
        HttpSession session = request.getSession(false);
        //세션이 존재하지 않는다면 홈으로 돌아간다.
        if(session == null) return "redirect:/";
        //세션이 존재한다면 현재 로그인 한 회원의 프로필을 모델에 담는다.
        LoginRequestDto loginRequestDto = (LoginRequestDto) session.getAttribute(SessionConst.LOGIN_USER);

        UserResponseDto userResponseDto = userService.searchProfiles(loginRequestDto.getIdentifier());

        model.addAttribute("user", userResponseDto);

        return "user/profileList";
    }

    //프로필 수정 폼
    @GetMapping("/profile/{userId}/edit")
    public String myProfileEditForm(@PathVariable Long userId, Model model){
        UserResponseDto userResponseDto = userService.searchProfiles(userId);
        UserRequestDto userRequestDto = userResponseDto.toUserRequestDto();

        model.addAttribute("user", userRequestDto);

        return "user/profileEditForm";
    }

    //프로필 수정
    @PostMapping("/profile/{userId}/edit")
    public String myProfileEdit(
            @PathVariable Long userId,
            @Validated @ModelAttribute("user") UserRequestDto userRequestDto,
            BindingResult bindingResult){

        //검증 실패시 입력 폼으로 돌아간다.
        if(bindingResult.hasErrors()){
            log.error("errors={}", bindingResult);
            return "user/profileEditForm";
        }

        userService.updateProfiles(userId, userRequestDto);

        return "redirect:/profile";
    }
}
