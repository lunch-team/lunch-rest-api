package com.lunchteam.lunchrestapi.api.dto;

import com.lunchteam.lunchrestapi.api.entity.MemberEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MemberResponseDto {

    private String email;

    public static MemberResponseDto of(MemberEntity member) {
        return new MemberResponseDto(member.getEmail());
    }
}