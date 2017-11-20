package com.solovey.movieland.web.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;

@WebFilter("/v1/*")
public class SecurityFilter implements Filter {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) {

        HttpServletRequestWrapper requestWrapper = new SecurityHttpRequestWrapper((HttpServletRequest) request);

        try {
            chain.doFilter((requestWrapper.getHeader("uuid") != null) ? requestWrapper : request, response);
        } catch (ServletException | IOException e) {
            log.error("Error processing Filter {}", e);
            throw new RuntimeException();
        }

    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {
    }


    @Override
    public void destroy() {
    }
}