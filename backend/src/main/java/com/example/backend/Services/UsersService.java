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
                .map(users -> new EditUsersRes(
                        users.getId(),
                        users.getName(),
                        users.getBirthday().format(DateTimeFormatter.ISO_DATE).toString(),
                        users.getEmail(),
                        users.getAddress(),
                        users.getUsername()))
                .orElseThrow(() -> new RuntimeException("找不到使用者"));
    }

    @Transactional
    public void save(SignUpReq req) {
        Users users = new Users();
        users.setUsername(req.username());
        users.setPassword(passwordEncoder.encode(req.password()));
        users.setName(req.name());
        users.setEmail(req.email());
        users.setBirthday(LocalDate.parse(req.birthday(), DateTimeFormatter.ISO_DATE));
        users.setAddress(req.address());
        usersRepository.save(users);
    }

    @Transactional
    public void update(EditUsersPostReq req) {
        usersRepository.findById(req.id()).ifPresent(users -> {
            users.setName(req.name());
            users.setEmail(req.email());
            users.setBirthday(LocalDate.parse(req.birthday(), DateTimeFormatter.ISO_DATE));
            users.setAddress(req.address());
            usersRepository.save(users);
        });
    }
}
