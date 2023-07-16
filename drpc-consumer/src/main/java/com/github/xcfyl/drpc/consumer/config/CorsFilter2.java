package com.github.xcfyl.drpc.consumer.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author 西城风雨楼
 */
@WebFilter
@Component
@Order(0)
@Slf4j
public class CorsFilter2 implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        String origin = request.getHeader("Origin");
        if (origin == null || "".equals(origin)) {
            origin = request.getHeader("Referer");
        }

        response.setHeader("Access-Control-Allow-Origin", origin);
        if (log.isDebugEnabled()) {
            log.debug("设置origin: {}", origin);
        }

        response.setHeader("Access-Control-Allow-Credentials", "true");
        if (RequestMethod.OPTIONS.toString().equals(request.getMethod())) {
            String allowMethod = request.getHeader("Access-Control-Request-Method");
            String allowHeaders = request.getHeader("Access-Control-Request-Headers");
            response.setHeader("Access-Control-Max-Age", "86400");
            response.setHeader("Access-Control-Allow-Methods", allowMethod);
            response.setHeader("Access-Control-Allow-Headers", allowHeaders);
            return;
        }

        filterChain.doFilter(request, response);
    }
}
