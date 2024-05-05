package com.project.shopApp.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.prefix}/homes")
public class HomeController {
    @GetMapping("")
    public String getAll(){
        return "Hello World";
    }

}
