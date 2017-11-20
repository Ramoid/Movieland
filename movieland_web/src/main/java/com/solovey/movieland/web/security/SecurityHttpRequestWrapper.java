package com.solovey.movieland.web.security;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.security.Principal;


public class SecurityHttpRequestWrapper extends HttpServletRequestWrapper {

    private Principal principal;

    SecurityHttpRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    @Override
    public Principal getUserPrincipal() {
        return principal;
    }

    public void setUserPrincipal(Principal userPrincipal){
        principal=userPrincipal;
    }
}
