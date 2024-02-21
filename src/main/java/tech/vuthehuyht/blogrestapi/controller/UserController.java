package tech.vuthehuyht.blogrestapi.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tech.vuthehuyht.blogrestapi.dto.request.UserRequest;
import tech.vuthehuyht.blogrestapi.services.UserService;

@RestController
@Slf4j
@RequestMapping(path = "/api/v1/user")
@RequiredArgsConstructor
@Validated
public class UserController {
    private final UserService userService;

    @PostMapping(path = "/register")
    public ResponseEntity<Object> register(@Valid @RequestBody UserRequest userRequest) {
        log.info("Intercept registration new user");
        return new ResponseEntity<>(userService.createUser(userRequest), HttpStatus.CREATED);
    }
}
