package com.booking.identityservice.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



import java.security.Principal;

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/user")
    public Principal user(Principal user) {
        return user;
    }

}