package com.lunchteam.lunchrestapi.api.service;

import com.lunchteam.lunchrestapi.api.dto.member.MemberRequestDto;
import com.lunchteam.lunchrestapi.api.dto.member.MemberResponseDto;
import com.lunchteam.lunchrestapi.api.entity.MemberEntity;
import com.lunchteam.lunchrestapi.api.repository.MemberRepository;
import com.lunchteam.lunchrestapi.api.repository.RefreshTokenRepository;
import com.lunchteam.lunchrestapi.security.TokenProvider;
import com.lunchteam.lunchrestapi.security.dto.RefreshTokenDto;
import com.lunchteam.lunchrestapi.security.dto.TokenDto;
import com.lunchteam.lunchrestapi.security.dto.TokenRequestDto;
import com.lunchteam.lunchrestapi.util.VerifyUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final String DEL_YN = "N";
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final VerifyUtil verifyUtil;

    @Transactional
    public String signup(MemberRequestDto memberRequestDto) {
        if (!verifyUtil.isValidId(memberRequestDto.getLoginId())) {
            return "invalid_login_id";
        }
        if (!verifyUtil.isValidPw(memberRequestDto.getPassword())) {
            return "invalid_password";
        }

        if (memberRepository.existsByEmailAndDelYn(memberRequestDto.getEmail(), DEL_YN)) {
            return "exist_email";
        }

        if (memberRepository.existsByLoginIdAndDelYn(memberRequestDto.getLoginId(), DEL_YN)) {
            return "exist_login_id";
        }

        MemberEntity member = memberRequestDto.toMember(passwordEncoder);
        MemberResponseDto.of(memberRepository.save(member));
        return null;
    }

    @Transactional
    public TokenDto login(MemberRequestDto memberRequestDto) {
        // 1. Login ID/PW 를 기반으로 AuthenticationToken 생성
        UsernamePasswordAuthenticationToken authenticationToken = memberRequestDto
            .toAuthentication();

        // 2. 실제로 검증 (사용자 비밀번호 체크) 이 이루어지는 부분
        //    authenticate 메서드가 실행이 될 때 CustomUserDetailsService 에서 만들었던 loadUserByUsername 메서드가 실행됨
        Authentication authentication = authenticationManagerBuilder.getObject()
            .authenticate(authenticationToken);

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        // 4. RefreshToken 저장
        RefreshTokenDto refreshToken = RefreshTokenDto.builder()
            .key(authentication.getName())
            .value(tokenDto.getRefreshToken())
            .build();

        refreshTokenRepository.save(refreshToken);

        // 5. 토큰 발급
        return tokenDto;
    }

    @Transactional
    public TokenDto reissue(TokenRequestDto tokenRequestDto) {
        // 1. Refresh Token 검증
        if (!tokenProvider.validateToken(tokenRequestDto.getRefreshToken())) {
            throw new RuntimeException("Refresh Token 이 유효하지 않습니다.");
        }

        // 2. Access Token 에서 Member ID 조회
        Authentication authentication = tokenProvider
            .getAuthentication(tokenRequestDto.getAccessToken());

        // 3. 저장소에서 Member ID 를 기반으로 Refresh Token 값 가져옴
        RefreshTokenDto refreshToken = refreshTokenRepository
            .findByKeyToken(authentication.getName())
            .orElseThrow(() -> new RuntimeException("로그아웃 된 사용자입니다."));

        // 4. Refresh Token 일치하는지 검사
        if (!refreshToken.getValue().equals(tokenRequestDto.getRefreshToken())) {
            throw new RuntimeException("토큰의 유저 정보가 일치하지 않습니다.");
        }

        // 5. 새로운 토큰 생성
        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        // 6. 저장소 정보 업데이트
        RefreshTokenDto newRefreshToken = refreshToken.updateValue(tokenDto.getRefreshToken());
        refreshTokenRepository.save(newRefreshToken);

        // 토큰 발급
        return tokenDto;
    }

    @Transactional
    public MemberEntity getMemberInfo(MemberRequestDto memberRequestDto) {
        return memberRepository
            .findByLoginIdAndDelYn(memberRequestDto.getLoginId(), DEL_YN)
            .orElse(null);
    }
}