package com.example.servicea.controller;

import com.example.servicea.service.ServiceAService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/handle")
public class ServiceAController {

    @Autowired
    private ServiceAService serviceAService;

    @RequestMapping("/byserviceb")
    public String handleA() {
        serviceAService.handerByServiceB();
        return "handle service-a by service-b success";
    }
}
