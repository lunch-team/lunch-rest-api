package com.lunchteam.lunchrestapi.api.service;

import com.lunchteam.lunchrestapi.api.dto.MemberResponseDto;
import com.lunchteam.lunchrestapi.api.repository.MemberRepository;
import com.lunchteam.lunchrestapi.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public MemberResponseDto getMemberInfo(String email) {
        return memberRepository.findByEmail(email)
            .map(MemberResponseDto::of)
            .orElseThrow(() -> new RuntimeException("유저 정보가 없습니다."));
    }

    // 현재 SecurityContext 에 있는 유저 정보 가져오기
    @Transactional(readOnly = true)
    public MemberResponseDto getMyInfo() {
        // 내 정보를 가져올 때는 SecurityUtil.getCurrentMemberId() 사용
        return memberRepository.findById(SecurityUtil.getCurrentMemberId())
            .map(MemberResponseDto::of)
            .orElseThrow(() -> new RuntimeException("로그인 유저 정보가 없습니다."));
    }
}
