package com.lunchteam.lunchrestapi.api.service;

import com.lunchteam.lunchrestapi.api.dto.member.MemberRequestDto;
import com.lunchteam.lunchrestapi.api.dto.member.MemberResponseDto;
import com.lunchteam.lunchrestapi.api.exception.AuthenticationException;
import com.lunchteam.lunchrestapi.api.repository.MemberRepository;
import com.lunchteam.lunchrestapi.api.repository.MemberRepositorySupport;
import com.lunchteam.lunchrestapi.security.SecurityUtil;
import com.lunchteam.lunchrestapi.util.VerifyUtil;
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

    private final VerifyUtil verifyUtil;
    private final MemberRepositorySupport memberRepositorySupport;
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public MemberResponseDto getMemberInfo(String email) {
        return memberRepositorySupport.findByEmail(email)
            .map(MemberResponseDto::of)
            .orElseThrow(() -> new AuthenticationException("유저 정보가 없습니다."));
    }

    // 현재 SecurityContext 에 있는 유저 정보 조회
    @Transactional(readOnly = true)
    public MemberResponseDto getMyInfo() {
        // 내 정보를 가져올 때는 SecurityUtil.getCurrentMemberId() 사용
        return memberRepositorySupport.findById(SecurityUtil.getCurrentMemberId())
            .map(MemberResponseDto::of)
            .orElseThrow(() -> new AuthenticationException("로그인 유저 정보가 없습니다."));
    }

    @Transactional
    public MemberResponseDto findId(MemberRequestDto memberRequestDto) {

        return memberRepositorySupport.findByEmailAndName(memberRequestDto)
            .map(MemberResponseDto::of)
            .orElseThrow(() -> new AuthenticationException("유저 정보가 없습니다."));
    }

    @Transactional
    public Map<String, Object> findPw(MemberRequestDto memberRequestDto) {

        MemberResponseDto memberResponseDto = memberRepositorySupport
            .findByEmailAndLoginId(memberRequestDto)
            .map(MemberResponseDto::of)
            .orElseGet(this::returnNull);

        Map<String, Object> resultMap = new HashMap<>();
        if (memberResponseDto == null) {
            log.warn("no member info");
            resultMap.put("errMsg", "no_member_info");
        } else {
            resultMap.put("memberId", memberResponseDto.getId());
        }

        return resultMap;
    }

    @Transactional
    public Map<String, Object> resetPassword(MemberRequestDto memberRequestDto) {
        Map<String, Object> resultMap = new HashMap<>();

        if (!verifyUtil.isValidPw(memberRequestDto.getPassword())) {
            resultMap.put("errMsg", "invalid_password");
        } else {
            Long result = memberRepositorySupport
                .updatePasswordByLoginId(memberRequestDto);

            if (result > 0) {
                resultMap.put("loginId", memberRequestDto.getLoginId());
            } else {
                log.warn("no member info");
                resultMap.put("errMsg", "no_member_info");
            }
        }
        
        return resultMap;
    }

    /**
     * loginId 중복 체크
     *
     * @param memberRequestDto loginId
     * @return boolean
     */
    @Transactional
    public boolean checkLoginId(MemberRequestDto memberRequestDto) {
        return memberRepository.findByLoginId(memberRequestDto.getLoginId()).isPresent();
    }

    private MemberResponseDto returnNull() {
        return null;
    }
}
