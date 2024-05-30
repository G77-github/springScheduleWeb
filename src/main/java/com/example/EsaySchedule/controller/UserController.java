package com.example.EsaySchedule.controller;


import com.example.EsaySchedule.dto.AddUserRequest;
import com.example.EsaySchedule.entity.Bookmark;
import com.example.EsaySchedule.entity.UserProfile;
import com.example.EsaySchedule.service.TeamService;
import com.example.EsaySchedule.service.UserService;
import com.example.EsaySchedule.service.ValidationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;
    private final ValidationService validationService;
    private final TeamService teamService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/signup")
    public String signup(@Valid @ModelAttribute AddUserRequest addUserRequest, BindingResult result, Model model) {

        if (result.hasErrors()) {
            model.addAttribute("errors", result.getAllErrors());
            return "error";
        }

        boolean checkUserEmilaDuplicate = validationService.checkUserEmilaDuplicate(addUserRequest.getUserEmail());

        if (!checkUserEmilaDuplicate) {
            return "error";
        }

        Long userId = userService.saveUserProfile(addUserRequest);

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

    @GetMapping("/userDetail")
    public String viewUserDetail(Model model) {

        UserProfile currentUser = validationService.getUserData();

        if (currentUser == null) {
            return "error";
        }

        Long userId = currentUser.getUserId();
        String userName = currentUser.getUName();
        String userEmail = currentUser.getUserEmail();

        model.addAttribute("userId", userId);
        model.addAttribute("userName", userName);
        model.addAttribute("userEmail", userEmail);

        List<Bookmark> userBookMarks = teamService.findUserBookMarks(userId);
        model.addAttribute("userBookMarks", userBookMarks);

        return "viewUserDetail";
    }

    @PostMapping("/userDetail")
    public String updateUserName(@RequestParam("userName") String userName) {

        UserProfile currentUser = validationService.getUserData();
        Long userId = currentUser.getUserId();

        userService.updateUserName(userId, userName);

        validationService.updateSession(userId, userName);

        return "redirect:/userDetail";
    }

    @GetMapping("/verifyPassword")
    public String verifyPassword() {
        return "verifyPassword";
    }

    @PostMapping("/verifyPassword")
    @ResponseBody
    public ResponseEntity<?> verifyPassword(@RequestBody Map<String, String> request) {
        String currentPassword = request.get("currentPassword");

        UserProfile currentUser = validationService.getUserData();

        if (bCryptPasswordEncoder.matches(currentPassword, currentUser.getPassword())) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/changePassword")
    public String changePassword() {
        return "changePassword";
    }

    @PostMapping("changePassword")
    @ResponseBody
    public ResponseEntity<?> changePassword(@RequestBody Map<String, String> request) {
        String password = request.get("password");
        String confirmPassword = request.get("confirmPassword");

        if (!password.equals(confirmPassword)) {
            return ResponseEntity.badRequest().build();
        }

        UserProfile currentUser = validationService.getUserData();

        userService.updatePassword(currentUser.getUserId(), password);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/checkUserEmail")
    @ResponseBody
    public ResponseEntity<?> checkUserEmailDuplicate(@RequestBody Map<String, String> request) {
        String userEmail = request.get("userEmail");

        boolean checkUserEmilaDuplicate = validationService.checkUserEmilaDuplicate(userEmail);

        if (checkUserEmilaDuplicate) {
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/preUnregister")
    public String preUnregister(HttpSession httpSession) {

        UserProfile currentUser = validationService.getUserData();
        Long userId = currentUser.getUserId();

        boolean isTeamMaster = validationService.isTeamMaster(userId);
        if (isTeamMaster) {
            return "redirect:/main";
        }

        httpSession.setAttribute("accessUnregister", true);
        return "redirect:/unregister";
    }

    @GetMapping("/unregister")
    public String unregister(HttpSession httpSession) {
        Boolean accessUnregister = (Boolean) httpSession.getAttribute("accessUnregister");
        if (accessUnregister == null || !accessUnregister) {
            return "redirect:/main";
        }

        UserProfile currentUser = validationService.getUserData();
        Long userId = currentUser.getUserId();

        userService.unregister(userId);

        return "redirect:/logout";
    }

}
