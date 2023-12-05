package com.example.backend.Services;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.backend.dto.EditUsersPostReq;
import com.example.backend.dto.EditUsersRes;
import com.example.backend.dto.SignUpReq;
import com.example.backend.model.Users;
import com.example.backend.repository.UsersRepository;
import com.example.backend.utils.JwtTokenUtils;

import jakarta.transaction.Transactional;

@Service
public class UsersService implements UserDetailsService {
    private UsersRepository usersRepository;
    private JwtTokenUtils jwtTokenUtils;
    private PasswordEncoder passwordEncoder;

    public UsersService(UsersRepository usersRepository, JwtTokenUtils jwtTokenUtils, PasswordEncoder passwordEncoder) {
        this.usersRepository = usersRepository;
        this.jwtTokenUtils = jwtTokenUtils;
        this.passwordEncoder = passwordEncoder;
    }

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
