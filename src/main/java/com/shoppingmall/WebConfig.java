package com.shoppingmall;

import com.shoppingmall.web.Interceptor.AdminLoginCheckInterceptor;
import com.shoppingmall.web.Interceptor.LoginCheckInterceptor;
import com.shoppingmall.web.argumentresolver.LoginAdminArgumentResolver;
import com.shoppingmall.web.argumentresolver.LoginUserArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    //로그인 쿠키, 세션 처리
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginAdminArgumentResolver());
        resolvers.add(new LoginUserArgumentResolver());
    }
    //미인증(로그인 하지 않은) 사용자 처리
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginCheckInterceptor())
                .order(1)
                .addPathPatterns("/cart/**", "/order/**", "/orders/**")
                .excludePathPatterns("/", "/sign-in", "/sign-up", "/logout", "/search", "/shop/**",
                        "/profile/**","/css/**", "/fonts/**", "/img/**", "/js/**", "/scss/**",
                        "/*.ico", "/error");
        registry.addInterceptor(new AdminLoginCheckInterceptor())
                .order(2)
                .addPathPatterns("/category/**", "/item/**")
                .excludePathPatterns("/", "/sign-in", "/sign-up", "/logout", "/search", "/shop/**",
                        "/profile/**", "/css/**", "/fonts/**",
                        "/img/**", "/js/**", "/scss/**", "/*.ico", "/error");
    }
}
