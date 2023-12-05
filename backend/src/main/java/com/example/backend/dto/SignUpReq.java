package com.example.backend.dto;

import java.io.Serializable;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SignUpReq implements Serializable  {
	@NotBlank(message = "請輸入姓名")
	@Length(max = 255, message = "姓名長度過長")
	private String name;
	@NotBlank(message = "請輸入生日")
	private String birthday;
	@NotBlank(message = "請輸入電子信箱")
	@Email(message = "電子信格式錯誤")
	@Length(max = 255, message = "長度過長")
	private String email;
	@NotBlank(message = "請輸入地址")
	@Length(max = 255, message = "地址長度過長")
	private String address;
	@Pattern(regexp = "^[a-zA-Z0-9]*$", message = "帳號格式需為英文或數字")
	@NotBlank(message = "請輸入帳號")
	private String username;
	@Pattern(regexp = "^[a-zA-Z0-9]*$", message = "密碼格式需為英文或數字")
	@NotBlank(message = "請輸入密碼")
	private String password;
	@Pattern(regexp = "^[a-zA-Z0-9]*$", message = "密碼複驗格式需為英文或數字")
	@NotBlank(message = "請輸入密碼複驗") String chkPassword;
}
