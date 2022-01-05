package com.shoppingmall.web.Interceptor;

import com.shoppingmall.web.SessionConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Slf4j
public class AdminLoginCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();

        HttpSession session = request.getSession();

        if(session != null && session.getAttribute(SessionConst.LOGIN_USER) != null){
            response.sendRedirect("/");
        }

        if(session == null || session.getAttribute(SessionConst.LOGIN_ADMIN) == null){
            log.info("일반 사용자 요청");

            response.sendRedirect("/sign-in?redirectURL=" + requestURI);
            return false;
        }

        return true;
    }
}
