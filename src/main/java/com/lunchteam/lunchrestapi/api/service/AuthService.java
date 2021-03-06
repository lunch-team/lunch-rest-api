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
        // 1. Login ID/PW ??? ???????????? AuthenticationToken ??????
        UsernamePasswordAuthenticationToken authenticationToken = memberRequestDto
            .toAuthentication();

        // 2. ????????? ?????? (????????? ???????????? ??????) ??? ??????????????? ??????
        //    authenticate ???????????? ????????? ??? ??? CustomUserDetailsService ?????? ???????????? loadUserByUsername ???????????? ?????????
        Authentication authentication = authenticationManagerBuilder.getObject()
            .authenticate(authenticationToken);

        // 3. ?????? ????????? ???????????? JWT ?????? ??????
        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        // 4. RefreshToken ??????
        RefreshTokenDto refreshToken = RefreshTokenDto.builder()
            .key(authentication.getName())
            .value(tokenDto.getRefreshToken())
            .build();

        refreshTokenRepository.save(refreshToken);

        // 5. ?????? ??????
        return tokenDto;
    }

    @Transactional
    public TokenDto reissue(TokenRequestDto tokenRequestDto) {
        // 1. Refresh Token ??????
        if (!tokenProvider.validateToken(tokenRequestDto.getRefreshToken())) {
            throw new RuntimeException("Refresh Token ??? ???????????? ????????????.");
        }

        // 2. Access Token ?????? Member ID ??????
        Authentication authentication = tokenProvider
            .getAuthentication(tokenRequestDto.getAccessToken());

        // 3. ??????????????? Member ID ??? ???????????? Refresh Token ??? ?????????
        RefreshTokenDto refreshToken = refreshTokenRepository
            .findByKeyToken(authentication.getName())
            .orElseThrow(() -> new RuntimeException("???????????? ??? ??????????????????."));

        // 4. Refresh Token ??????????????? ??????
        if (!refreshToken.getValue().equals(tokenRequestDto.getRefreshToken())) {
            throw new RuntimeException("????????? ?????? ????????? ???????????? ????????????.");
        }

        // 5. ????????? ?????? ??????
        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        // 6. ????????? ?????? ????????????
        RefreshTokenDto newRefreshToken = refreshToken.updateValue(tokenDto.getRefreshToken());
        refreshTokenRepository.save(newRefreshToken);

        // ?????? ??????
        return tokenDto;
    }

    @Transactional
    public MemberEntity getMemberInfo(MemberRequestDto memberRequestDto) {
        return memberRepository
            .findByLoginIdAndDelYn(memberRequestDto.getLoginId(), DEL_YN)
            .orElse(null);
    }
}