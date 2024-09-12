package com.example.shopappbackend.controllers;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.shopappbackend.Services.UsersService;
import com.example.shopappbackend.dto.EditUsersPostReq;
import com.example.shopappbackend.dto.EditUsersRes;
import com.example.shopappbackend.dto.SignUpReq;
import com.example.shopappbackend.exception.CustomArgumentNotValidException;

import jakarta.validation.Valid;

@RestController
public class UsersController {
    private UsersService usersService;

    public UsersController(UsersService usersService) {
        super();
        this.usersService = usersService;
    }

    @GetMapping("/public/users/checkUsername")
    public ResponseEntity<Boolean> checkUsername(@RequestParam String username) {
        return ResponseEntity.ok(usersService.findByUsername(username).isEmpty());
    }

    @PostMapping("/public/users/signUp")
    public ResponseEntity<Void> signUp(@Valid @RequestBody SignUpReq req, BindingResult result) {
        Optional.ofNullable(req.getPassword()).ifPresent(pwd -> {
            if (!pwd.equals(req.getChkPassword())) {
                result.rejectValue("chkPassword", "", "密碼驗證不一致");
            }
        });

        usersService.findByUsername(req.getUsername()).ifPresent(e -> {
            result.rejectValue("username", "", "帳號已存在");
        });
        
        if (result.hasErrors()) {
            throw new CustomArgumentNotValidException("驗證失敗", result);
        }

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
