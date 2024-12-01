package com.example.teamproject.controller;

import com.example.teamproject.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
}
