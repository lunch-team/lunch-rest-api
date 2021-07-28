package com.lunchteam.lunchrestapi.api.service;

import com.lunchteam.lunchrestapi.api.dto.MemberRequestDto;
import com.lunchteam.lunchrestapi.api.dto.MemberResponseDto;
import com.lunchteam.lunchrestapi.api.repository.MemberRepositorySupport;
import com.lunchteam.lunchrestapi.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepositorySupport memberRepositorySupport;

    @Transactional(readOnly = true)
    public MemberResponseDto getMemberInfo(String email) {
        return memberRepositorySupport.findByEmail(email)
            .map(MemberResponseDto::of)
            .orElseThrow(() -> new RuntimeException("유저 정보가 없습니다."));
    }

    // 현재 SecurityContext 에 있는 유저 정보 가져오기
    @Transactional(readOnly = true)
    public MemberResponseDto getMyInfo() {
        // 내 정보를 가져올 때는 SecurityUtil.getCurrentMemberId() 사용
        return memberRepositorySupport.findById(SecurityUtil.getCurrentMemberId())
            .map(MemberResponseDto::of)
            .orElseThrow(() -> new RuntimeException("로그인 유저 정보가 없습니다."));
    }

    @Transactional
    public MemberResponseDto findId(MemberRequestDto memberRequestDto) {

        return memberRepositorySupport.findByEmailAndName(memberRequestDto)
            .map(MemberResponseDto::of)
            .orElseThrow(() -> new RuntimeException("유저 정보가 없습니다."));
    }

    @Transactional
    public String findPw(MemberRequestDto memberRequestDto) {

        MemberResponseDto memberResponseDto = memberRepositorySupport
            .findByEmailAndLoginId(memberRequestDto)
            .map(MemberResponseDto::of)
            .orElse(null);

        if(memberRequestDto == null) {
            return "no_member_info";
        }

        assert memberResponseDto != null;
        return memberResponseDto.getLoginId();
    }

    @Transactional
    public String resetPassword(MemberRequestDto memberRequestDto) {

        MemberResponseDto memberResponseDto = memberRepositorySupport
            .updatePasswordByLoginId(memberRequestDto)
            .map(MemberResponseDto::of)
            .orElse(null);

        // TODO: password reset
        assert memberResponseDto != null;
        return memberResponseDto.getEmail();
    }
}
