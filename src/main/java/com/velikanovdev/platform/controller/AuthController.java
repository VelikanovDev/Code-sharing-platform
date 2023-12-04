package com.velikanovdev.platform.controller;

import com.velikanovdev.platform.entity.User;
import com.velikanovdev.platform.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Slf4j
public class AuthController {
    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login() {
        return "/security/login";
    }

    @GetMapping("/register")
    public String register() {
        log.info("AuthController: get register template");
        return "/security/register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("user") User user,
                               @RequestParam("confirmPassword") String confirmPassword,
                               Model model) {
        String validError = isUserValid(user, confirmPassword, model);

        if (validError.isBlank()) {
            userService.registerUser(user);
            log.info("User with username " + user.getUsername() + " has been successfully registered");
            return "redirect:/login";
        }

        return validError;
    }

    private String isUserValid(User user, String confirmPassword, Model model) {
        if (!StringUtils.equals(user.getPassword(), confirmPassword)) {
            model.addAttribute("passwordConfirmationError", true);
            log.error("AuthController: Passwords are not matching");
            return "/security/register";
        }

        String username = user.getUsername();
        if (userService.getUserByUsername(username) != null) {
            log.warn("User with username " + username + " already exists");
            model.addAttribute("userExistsError", true);
            return "/security/register";
        }

        if (user.getPassword().length() < 4) {
            model.addAttribute("passwordIsTooShort", true);
            log.warn("Password must contain at least 4 characters");
            return "/security/register";
        }

        return "";
    }

}
