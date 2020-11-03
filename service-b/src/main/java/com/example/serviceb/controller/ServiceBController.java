package com.example.serviceb.controller;

import com.example.serviceb.service.ServiceBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/handle")
public class ServiceBController {
    @Autowired
    private ServiceBService serviceBService;

    @RequestMapping("/dosomething")
    public String handleA() {
        serviceBService.doSomething();
        return "handle service-b success";
    }
}