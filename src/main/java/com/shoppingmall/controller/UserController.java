package com.shoppingmall.controller;

import com.shoppingmall.exception.DuplicatedUserException;
import com.shoppingmall.exception.IncorrectLoginInfoException;
import com.shoppingmall.service.user.UserService;
import com.shoppingmall.web.SessionConst;
import com.shoppingmall.web.argumentresolver.UserLogin;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static com.shoppingmall.dto.UserRequestDto.*;
import static com.shoppingmall.dto.UserResponseDto.*;

@Slf4j
@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    //회원가입 form
    @GetMapping("/sign-up")
    public String signUpForm(@ModelAttribute("user") UserCreateForm userCreateForm){
        log.info("회원가입 form access");
        return "user/signUpForm";
    }
    //회원가입
    @PostMapping("/sign-up")
    public String signUp(@Validated @ModelAttribute("user") UserCreateForm userCreateForm, BindingResult bindingResult){

        //검증 실패시 입력 폼으로 돌아간다.
        if(bindingResult.hasErrors()){
            log.error("errors={}", bindingResult);
            return "user/signUpForm";
        }

        //중복 아이디 체크
        try{
            userService.userRegistration(userCreateForm);
        } catch (Exception e){
            if(e.getClass() == DuplicatedUserException.class){
                log.error("errors={}", e.getMessage());
                bindingResult.reject("duplicatedUserException", new Object[]{DuplicatedUserException.class}, null);
            }
            return "user/signUpForm";
        }

        log.info("회원가입 완료 identifier={}", userCreateForm.getIdentifier());
        return "redirect:/";
    }

    //로그인 form
    @GetMapping("/sign-in")
    public String signInForm(@ModelAttribute("signInForm") LoginUserForm loginUserForm){
        log.info("로그인 form access");
        return "user/signInForm";
    }

    //로그인
    @PostMapping("/sign-in")
    public String signIn(@Validated @ModelAttribute("signInForm") LoginUserForm loginUserForm,
                         BindingResult bindingResult, @RequestParam(defaultValue = "/") String redirectURL,
                         HttpServletRequest request){
        //검증 오류에 문제가 있다면
        if(bindingResult.hasErrors()){
            log.error("errors={}", bindingResult);
            return "user/signInForm";
        }

        try{
            //로그인 시도
            userService.login(loginUserForm);
            //세션이 있으면 세션 반환, 없으면 신규 세션 생성
            HttpSession session = request.getSession();
            //세션에 로그인 회원 정보를 보관한다.
            if(loginUserForm.getIdentifier().equals("admin123")){
                session.setAttribute(SessionConst.LOGIN_ADMIN, loginUserForm);
            } else{
                session.setAttribute(SessionConst.LOGIN_USER, loginUserForm);
            }
        } catch (Exception e) {
            log.error("error={}", e.getMessage());
            bindingResult.reject("incorrectLoginInfoException", new Object[]{IncorrectLoginInfoException.class}, null);
            return "user/signInForm";
        }

        log.info("로그인 identifier={}", loginUserForm.getIdentifier());
        return "redirect:" + redirectURL;
    }

    //로그아웃
    @UserLogin
    @PostMapping("/logout")
    public String logout(HttpServletRequest request){
        //세션을 불러온다. -> 없으면 생성하지 않는다.
        HttpSession session = request.getSession(false);

        //세션이 존재한다면 세션을 없앤다.
        if(session != null){
            session.invalidate();
        }

        log.info("로그아웃 session={}", session);
        return "redirect:/";
    }

    //프로필 정보
    @UserLogin
    @GetMapping("/profile")
    public String myProfile(HttpServletRequest request, Model model){
        HttpSession session = request.getSession(false);
        if(session.getAttribute(SessionConst.LOGIN_USER) != null){
            LoginUserForm loginUser = (LoginUserForm) session.getAttribute(SessionConst.LOGIN_USER);
            UserProfileInfo userProfileInfo = userService.searchProfiles(loginUser.getIdentifier());

            model.addAttribute("loginUser", loginUser);
            model.addAttribute("userProfileInfo", userProfileInfo);

            log.info("프로필 정보 열람 identifier={}", loginUser.getIdentifier());
        } else {
            LoginUserForm loginAdmin = (LoginUserForm) session.getAttribute(SessionConst.LOGIN_ADMIN);
            UserProfileInfo userProfileInfo = userService.searchProfiles(loginAdmin.getIdentifier());

            model.addAttribute("loginUser", loginAdmin);
            model.addAttribute("userProfileInfo", userProfileInfo);

            log.info("프로필 정보 열람 identifier={}", loginAdmin.getIdentifier());
        }

        return "user/profileList";
    }

    //프로필 수정 폼
    @UserLogin
    @GetMapping("/profile/{userId}/edit")
    public String myProfileEditForm(@PathVariable Long userId, Model model){

        UserUpdateForm userUpdateForm = userService.searchProfiles(userId);

        model.addAttribute("userUpdateForm", userUpdateForm);

        log.info("프로필 수정 form access");
        return "user/profileEditForm";
    }

    //프로필 수정
    @UserLogin
    @PostMapping("/profile/{userId}/edit")
    public String myProfileEdit(
            @PathVariable Long userId,
            @Validated @ModelAttribute("userUpdateForm") UserUpdateForm userUpdateForm,
            BindingResult bindingResult){

        //검증 실패시 입력 폼으로 돌아간다.
        if(bindingResult.hasErrors()){
            log.error("errors={}", bindingResult);
            return "user/profileEditForm";
        }

        userService.updateProfiles(userId, userUpdateForm);

        log.info("프로필 수정 완료");
        return "redirect:/profile";
    }

    //회원 탈퇴
    @UserLogin
    @GetMapping("/profile/{userId}/delete")
    public String deleteUser(@PathVariable Long userId, HttpServletRequest request){
        HttpSession session = request.getSession(false);
        //세션이 존재하지 않는다면 홈으로 리다이렉트
        if(session == null) return "redirect:/";

        userService.deleteUser(userId);
        session.invalidate();

        log.info("회원 탈퇴 id={}", userId);
        return "redirect:/";
    }
}
