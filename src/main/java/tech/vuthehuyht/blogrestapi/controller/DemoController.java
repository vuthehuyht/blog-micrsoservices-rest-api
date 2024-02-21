package tech.vuthehuyht.blogrestapi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/")
@RequiredArgsConstructor
public class DemoController {
    @GetMapping(path = "/demo")
    public String demo() {
        return "Demo API";
    }

    @GetMapping(path = "/test")
    public String test() {
        return "Test";
    }
}
