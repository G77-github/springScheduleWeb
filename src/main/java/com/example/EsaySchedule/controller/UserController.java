package com.example.EsaySchedule.controller;


import com.example.EsaySchedule.dto.AddUserRequest;
import com.example.EsaySchedule.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    @GetMapping("/login")
    public String login() {
        return "login2";
    }

    @PostMapping("/signup")
    public String signup(AddUserRequest addUserRequest) {
        userService.saveUserProfile(addUserRequest);
        return "redirect:/login";
    }

    @GetMapping("/signup")
    public String signup() {
        return "register";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());
        return "redirect:/login";
    }
}
