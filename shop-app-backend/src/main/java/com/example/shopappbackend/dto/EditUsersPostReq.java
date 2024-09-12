package com.example.shopappbackend.dto;

import java.io.Serializable;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class EditUsersPostReq implements Serializable {
    private static final long serialVersionUID = 1L;
    String id;
    @NotBlank(message = "請輸入姓名")
    @Length(max = 255, message = "姓名長度過長")
    String name;
    @NotBlank(message = "請輸入生日")
    String birthday;
    @NotBlank(message = "請輸入電子信箱")
    @Email(message = "電子信箱格式錯誤")
    @Length(max = 255, message = "電子信箱長度過長")
    String email;
    @NotBlank(message = "請輸入地址")
    @Length(max = 255, message = "地址長度過長")
    String address;
}
