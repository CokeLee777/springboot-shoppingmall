package com.shoppingmall.web.argumentresolver;

import com.shoppingmall.web.SessionConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static com.shoppingmall.dto.UserRequestDto.*;

@Slf4j
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        log.info("supportsParameter 실행");
        //@Login 어노테이션이 붙어있는가?
        boolean hasLoginAnnotation = parameter.hasParameterAnnotation(Login.class);
        //LoginUserRequestDto 타입인가?
        boolean hasUserType = LoginUserForm.class.isAssignableFrom(parameter.getParameterType());

        return hasLoginAnnotation && hasUserType;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        log.info("resolverArgument 실행");

        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        HttpSession session = request.getSession(false);
        //세션이 null 이면 LoginUserRequestDto 에 null이 들어간다.
        if(session == null) return null;

        return session.getAttribute(SessionConst.LOGIN_USER);
    }
}
