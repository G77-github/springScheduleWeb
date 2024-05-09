package com.example.EsaySchedule.config;

import com.example.EsaySchedule.entity.UserProfile;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        UserProfile userProfile = (UserProfile) authentication.getPrincipal();

        HttpSession session = request.getSession();
        session.setAttribute("userId", userProfile.getUserId());
        session.setAttribute("userName", userProfile.getUName());

        response.sendRedirect("/main");
    }
}
