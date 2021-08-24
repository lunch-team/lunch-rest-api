package com.lunchteam.lunchrestapi.api.dto;

import com.lunchteam.lunchrestapi.api.entity.MemberEntity;
import com.lunchteam.lunchrestapi.security.dto.Authority;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MemberRequestDto {

    @Email(message = "err_email_format")
    private String email;
    @NotBlank(message = "err_login_id_blank")
    @Size(min = 3, max = 20, message = "err_login_id_size")
    private String loginId;
    @NotBlank(message = "err_name_blank")
    private String name;
    @NotBlank(message = "err_password_blank")
    private String password;

    private String newPassword;

    public MemberEntity toMember(PasswordEncoder passwordEncoder) {
        return MemberEntity.UserSignUp()
            .email(email)
            .name(name)
            .loginId(loginId)
            .password(passwordEncoder.encode(password))
            .delYn("N")
            .useCount(0)
            .authority(Authority.ROLE_USER)
            .build();
    }

    public UsernamePasswordAuthenticationToken toAuthentication() {
        return new UsernamePasswordAuthenticationToken(loginId, password);
    }
}