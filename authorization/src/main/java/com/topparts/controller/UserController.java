package com.topparts.controller;

import com.topparts.model.entity.User;
import com.topparts.model.service.DefaultUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {
    DefaultUserDetailsService userDetailsService;

    public UserController(DefaultUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userDetailsService.getUserById(id);
    }
}
