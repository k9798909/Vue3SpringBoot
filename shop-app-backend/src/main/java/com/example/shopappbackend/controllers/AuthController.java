package com.example.shopappbackend.controllers;

import com.example.shopappbackend.exception.LogicRuntimeException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.shopappbackend.services.UsersService;
import com.example.shopappbackend.dto.LoginReq;
import com.example.shopappbackend.dto.LoginRes;
import com.example.shopappbackend.dto.TokenReq;
import com.example.shopappbackend.model.Users;
import com.example.shopappbackend.utils.JwtTokenUtils;

import jakarta.validation.Valid;

@RestController
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtils jwtTokenUtils;
    private final UsersService usersService;

    @PostMapping("/login")
    public ResponseEntity<LoginRes> login(@Valid @RequestBody LoginReq req) {
        try {
            // 用ProviderManager進行認證
            Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword()));
            // principal 認證前:username 認證後:Users object、credentials 認證前:密碼
            // 認證後:null、authorities 認証前:null，認証後:權限
            Users users = (Users) authenticate.getPrincipal();
            String token = jwtTokenUtils.generateToken(users.getUsername());
            LoginRes res = new LoginRes();
            res.setUsername(users.getUsername());
            res.setName(users.getName());
            res.setToken(token);
            return ResponseEntity.ok().body(res);
        } catch (BadCredentialsException | UsernameNotFoundException e) {
            throw new LogicRuntimeException("帳號或密碼錯誤");
        }
    }

    @PostMapping("/public/tokenVerify")
    public ResponseEntity<Boolean> tokenVerify(@RequestBody TokenReq req) {
        return ResponseEntity.ok(usersService.tokenVerify(req.getToken()));
    }

}
