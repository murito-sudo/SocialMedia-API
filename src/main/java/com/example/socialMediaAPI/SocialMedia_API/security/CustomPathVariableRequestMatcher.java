package com.example.socialMediaAPI.SocialMedia_API.security;

import java.util.regex.Pattern;

import org.springframework.security.web.util.matcher.RequestMatcher;

import jakarta.servlet.http.HttpServletRequest;

public class CustomPathVariableRequestMatcher implements RequestMatcher {

    private static final Pattern REGISTER_PASSWORD_PATTERN = Pattern.compile("^/register/[^/]+$");

    @Override
    public boolean matches(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        return REGISTER_PASSWORD_PATTERN.matcher(requestURI).matches();
    }
}