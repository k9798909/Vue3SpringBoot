package com.example.backend.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.Services.UsersService;
import com.example.backend.Validators.SignUpReqValidator;
import com.example.backend.dto.EditUsersPostReq;
import com.example.backend.dto.EditUsersRes;
import com.example.backend.dto.SignUpReq;

import jakarta.validation.Valid;

@RestController
public class UsersController {
    private UsersService usersService;
    private SignUpReqValidator signUpReqValidator;

    public UsersController(UsersService usersService, SignUpReqValidator signUpReqValidator) {
        super();
        this.usersService = usersService;
        this.signUpReqValidator = signUpReqValidator;
    }

    @GetMapping("/public/users/checkUsername")
    public ResponseEntity<Boolean> checkUsername(@RequestParam String username) {
        return ResponseEntity.ok(usersService.findByUsername(username).isEmpty());
    }

    @PostMapping("/public/users/signUp")
    public ResponseEntity<Void> signUp(@Valid @RequestBody SignUpReq req) {
        signUpReqValidator.validate(req);
        usersService.save(req);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/users/edit")
    public ResponseEntity<EditUsersRes> findEditUsersBy(Authentication authentication) {
        return ResponseEntity.ok(usersService.findEditUsers(authentication.getName()));
    }

    @PostMapping("/users/edit")
    public ResponseEntity<Void> edit(@Valid @RequestBody EditUsersPostReq req) {
        usersService.update(req);
        return ResponseEntity.ok().build();
    }
}
