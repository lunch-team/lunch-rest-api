package com.lunchteam.lunchrestapi.api.dto;

import com.lunchteam.lunchrestapi.api.entity.MemberEntity;
import com.lunchteam.lunchrestapi.security.dto.Authority;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MemberRequestDto {

    private String email;
    private String name;
    private String password;

    public MemberEntity toMember(PasswordEncoder passwordEncoder) {
        return MemberEntity.UserSignUp()
            .email(email)
            .name(name)
            .password(passwordEncoder.encode(password))
            .delYn("N")
            .useCount(0)
            .authority(Authority.ROLE_USER)
            .build();
    }

    public UsernamePasswordAuthenticationToken toAuthentication() {
        return new UsernamePasswordAuthenticationToken(email, password);
    }
}