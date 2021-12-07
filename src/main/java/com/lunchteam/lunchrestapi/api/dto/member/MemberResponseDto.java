package com.lunchteam.lunchrestapi.api.dto.member;

import com.lunchteam.lunchrestapi.api.dto.BasicResponseDto;
import com.lunchteam.lunchrestapi.api.entity.MemberEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MemberResponseDto extends BasicResponseDto {

    private Long id;
    private String email;
    private String name;
    private String loginId;
    private String groupId;
    private int useCount;

    private void setIdToNull() {
        this.id = null;
    }

    public MemberResponseDto removeId(MemberResponseDto dto) {
        dto.setIdToNull();
        return dto;
    }

    public static MemberResponseDto of(MemberEntity member) {
        return new MemberResponseDto(
            member.getId(),
            member.getEmail(),
            member.getName(),
            member.getLoginId(),
            member.getGroupId(),
            member.getUseCount()
        );
    }
}