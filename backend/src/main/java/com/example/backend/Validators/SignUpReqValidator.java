package com.example.backend.Validators;

import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import com.example.backend.Services.UsersService;
import com.example.backend.dto.SignUpReq;
import com.example.backend.exception.CustomArgumentNotValidException;

@Component
public class SignUpReqValidator implements CustomArgumentValidator<SignUpReq> {
    private UsersService usersService;

    public SignUpReqValidator(UsersService usersService) {
        super();
        this.usersService = usersService;
    }

    @Override
    public void validate(SignUpReq target) {
        BindingResult result = getBindingResult(target);
        if (!target.getPassword().equals(target.getChkPassword())) {
            result.rejectValue("chkPassword", "", "密碼驗證不一致");
        }

        usersService.findByUsername(target.getUsername()).ifPresent(e -> {
            result.rejectValue("username", "", "帳號已存在");
        });

        // 統一交由handleCustomArgumentNotValid處理
        if (result.hasErrors()) {
            throw new CustomArgumentNotValidException("驗證失敗", result);
        }
    }

}
