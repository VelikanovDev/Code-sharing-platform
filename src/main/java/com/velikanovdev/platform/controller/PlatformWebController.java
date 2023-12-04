package com.velikanovdev.platform.controller;

import com.velikanovdev.platform.entity.Snippet;
import com.velikanovdev.platform.entity.User;
import com.velikanovdev.platform.service.PlatformService;
import com.velikanovdev.platform.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Slf4j
@Controller
@RequestMapping("/code")
public class PlatformWebController {
    private final PlatformService platformService;
    private final UserService userService;

    @Autowired
    public PlatformWebController(PlatformService platformService, UserService userService) {
        this.platformService = platformService;
        this.userService = userService;
    }

    @GetMapping(path = "/{id}", produces = MediaType.TEXT_HTML_VALUE)
    public String getCode(@PathVariable Long id, Model model) {
        model.addAttribute("codeEntity", platformService.getSnippet(id));
        return "snippet";
    }

    @GetMapping(path = "/new", produces = MediaType.TEXT_HTML_VALUE)
    public String getForm() {
        return "form";
    }

    @PostMapping(path = "/new", produces = MediaType.TEXT_HTML_VALUE)
    public String addSnippet(@RequestBody Snippet snippet, @AuthenticationPrincipal UserDetails userDetails) {
        log.info("Username: " + userDetails.getUsername());

        snippet.setAuthor(userService.getUserByUsername(userDetails.getUsername()));
        snippet.setDate(LocalDateTime.now());

        platformService.addOrUpdateSnippet(snippet);
        return "redirect:/code/latest";
    }

    @GetMapping(path = "/latest", produces = MediaType.TEXT_HTML_VALUE)
    public String getLatest(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.getUserByUsername(userDetails.getUsername());
        if (user == null) {
            return "/security/login";
        }

        String username = user.getUsername();

        log.info("User with username " + username + " has been authenticated");

        model.addAttribute("username", username);
        model.addAttribute("role", user.getRole());
        model.addAttribute("latest", platformService.getLatest());

        return "latest";
    }

    @GetMapping(path = "/edit/{id}", produces = MediaType.TEXT_HTML_VALUE)
    public String updateSnippet(Model model, @PathVariable Long id) {
        log.info("Editing snippet");

        Snippet snippet = platformService.getSnippet(id);
        if (snippet == null) {
            return "latest";
        }
        log.info("Snippet id: " + snippet.getId().toString());
        model.addAttribute("snippet", snippet);
        return "edit";
    }

    @PostMapping("/edit/{id}")
    public String updateSnippetSubmit(@PathVariable Long id, @ModelAttribute Snippet updatedSnippet) {
        Snippet existingSnippet = platformService.getSnippet(id);
        if (existingSnippet != null) {
            existingSnippet.setCode(updatedSnippet.getCode());
            existingSnippet.setEditDate(LocalDateTime.now());
            platformService.addOrUpdateSnippet(existingSnippet);
        }
        return "redirect:/code/latest";
    }

    @GetMapping("/users")
    public String showUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "users";
    }

    @DeleteMapping(path = "/delete/{id}", produces = "application/json")
    public ResponseEntity<String> deleteSnippet(@PathVariable Long id) {
        log.info("Deleting snippet with id " + id);
        platformService.deleteSnippet(id);
        return ResponseEntity.ok("Snippet has been deleted");
    }

    @DeleteMapping(path = "/delete", produces = "application/json")
    public ResponseEntity<String> deleteAllSnippets() {
        log.info("Deleting all snippets");
        platformService.deleteAllSnippets();
        return ResponseEntity.ok("Snippets have been deleted");
    }
}
