package com.example.shopappbackend.services;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.shopappbackend.dto.EditUsersPostReq;
import com.example.shopappbackend.dto.EditUsersRes;
import com.example.shopappbackend.dto.SignUpReq;
import com.example.shopappbackend.model.Users;
import com.example.shopappbackend.repository.UsersRepository;
import com.example.shopappbackend.utils.JwtTokenUtils;

import jakarta.transaction.Transactional;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UsersService implements UserDetailsService {
    private final UsersRepository usersRepository;
    private final JwtTokenUtils jwtTokenUtils;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usersRepository
                .findByUsername(username)
                .stream().findAny()
                .orElseThrow(() -> new UsernameNotFoundException("username " + username + " is not found"));
    }

    public boolean tokenVerify(String token) {
        return jwtTokenUtils.validateToken(token);
    }

    public Optional<Users> findByUsername(String username) {
        return usersRepository.findByUsername(username).stream().findAny();
    }

    public EditUsersRes findEditUsers(String usersname) {
        return findByUsername(usersname)
                .map(users -> {
                    EditUsersRes res = new EditUsersRes();
                    res.setId(users.getId());
                    res.setName(users.getName());
                    res.setBirthday(users.getBirthday().format(DateTimeFormatter.ISO_DATE).toString());
                    res.setEmail(users.getEmail());
                    res.setAddress(users.getAddress());
                    res.setUsername(users.getUsername());
                    return res;
                })
                .orElseThrow(() -> new RuntimeException("找不到使用者"));
    }

    @Transactional
    public void save(SignUpReq req) {
        Users users = new Users();
        users.setUsername(req.getUsername());
        users.setPassword(passwordEncoder.encode(req.getPassword()));
        users.setName(req.getName());
        users.setEmail(req.getEmail());
        users.setBirthday(LocalDate.parse(req.getBirthday(), DateTimeFormatter.ISO_DATE));
        users.setAddress(req.getAddress());
        usersRepository.save(users);
    }

    @Transactional
    public void update(EditUsersPostReq req) {
        usersRepository.findById(req.getId()).ifPresent(users -> {
            users.setName(req.getName());
            users.setEmail(req.getEmail());
            users.setBirthday(LocalDate.parse(req.getBirthday(), DateTimeFormatter.ISO_DATE));
            users.setAddress(req.getAddress());
            usersRepository.save(users);
        });
    }
}
