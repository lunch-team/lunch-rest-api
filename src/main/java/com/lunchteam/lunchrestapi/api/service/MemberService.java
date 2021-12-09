package com.lunchteam.lunchrestapi.api.service;

import com.lunchteam.lunchrestapi.api.dto.member.MemberRequestDto;
import com.lunchteam.lunchrestapi.api.dto.member.MemberResponseDto;
import com.lunchteam.lunchrestapi.api.repository.MemberRepositorySupport;
import com.lunchteam.lunchrestapi.security.SecurityUtil;
import java.util.HashMap;
import java.util.Map;
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

    // 현재 SecurityContext 에 있는 유저 정보 조회
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
    public Map<String, Object> findPw(MemberRequestDto memberRequestDto) {

        MemberResponseDto memberResponseDto = memberRepositorySupport
            .findByEmailAndLoginId(memberRequestDto)
            .map(MemberResponseDto::of)
            .orElseGet(this::returnNull);

        Map<String, Object> resultMap = new HashMap<>();
        if(memberResponseDto == null) {
            log.warn("no member info");
            resultMap.put("errMsg", "no_member_info");
        } else {
            resultMap.put("memberId", memberResponseDto.getId());
        }

        return resultMap;
    }

    @Transactional
    public Map<String, Object> resetPassword(MemberRequestDto memberRequestDto) {

        Long result = memberRepositorySupport
            .updatePasswordByLoginId(memberRequestDto);

        Map<String, Object> resultMap = new HashMap<>();
        if(result > 0) {
            resultMap.put("loginId", memberRequestDto.getLoginId());
        } else {
            log.warn("no member info");
            resultMap.put("errMsg", "no_member_info");
        }

        return resultMap;
    }

    private MemberResponseDto returnNull() {
        return null;
    }
}
