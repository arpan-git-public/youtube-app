package com.stream.app.youtubeapp.controller;

import com.stream.app.youtubeapp.service.UserService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/register")
    @ResponseStatus(HttpStatus.OK)
    public String register(Authentication authentication){
        Jwt jwt = (Jwt)authentication.getPrincipal();
        userService.register(jwt.getTokenValue());
        return "User Registration successful";
    }


    @PostMapping("subscribe/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public boolean subscribeUser(@PathVariable String userId) {
        userService.subScribeUser(userId);
        return true;
    }


    @PostMapping("unsubscribe/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public boolean unsubscribeUser(@PathVariable String userId) {
        userService.unsubScribeUser(userId);
        return true;
    }

    @GetMapping("{userId}/history")
    @ResponseStatus(HttpStatus.OK)
    public Set<String> userHistory(@PathVariable String userId) {
        return userService.userHistory(userId);
    }
}
