package com.lunchteam.lunchrestapi.api.controller;

import com.lunchteam.lunchrestapi.api.dto.MemberRequestDto;
import com.lunchteam.lunchrestapi.api.dto.MemberResponseDto;
import com.lunchteam.lunchrestapi.api.response.BasicResponse;
import com.lunchteam.lunchrestapi.api.response.CommonResponse;
import com.lunchteam.lunchrestapi.api.response.ErrorResponse;
import com.lunchteam.lunchrestapi.api.response.StatusEnum;
import com.lunchteam.lunchrestapi.api.service.AuthService;
import com.lunchteam.lunchrestapi.api.service.MemberService;
import com.lunchteam.lunchrestapi.security.dto.TokenDto;
import com.lunchteam.lunchrestapi.security.dto.TokenRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseEntity<? extends BasicResponse> signup(
        @RequestBody MemberRequestDto memberRequestDto) {
        String error;
        try {
            error = authService.signup(memberRequestDto);
        } catch (Exception e) {
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(StatusEnum.INTERNAL_SERVER_ERROR));
        }

        return error == null
            ? ResponseEntity.noContent().build()
            : ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(error));
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(
        @RequestBody MemberRequestDto memberRequestDto) {
        return ResponseEntity.ok(authService.login(memberRequestDto));
    }

    @PostMapping("/reissue")
    public ResponseEntity<TokenDto> reissue(
        @RequestBody TokenRequestDto tokenRequestDto) {
        return ResponseEntity.ok(authService.reissue(tokenRequestDto));
    }

    @PostMapping("/findId")
    public ResponseEntity<? extends BasicResponse> findId(
        @RequestBody MemberRequestDto memberRequestDto) {
        MemberResponseDto memberResponseDto = memberService.findId(memberRequestDto);
        if (memberResponseDto != null) {
            return ResponseEntity.ok().body(new CommonResponse<>(memberResponseDto));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Email과 LoginId로 비밀번호 찾기
     *
     * @param memberRequestDto email, loginId
     * @return ResponseEntity
     */
    @PostMapping("/findPw")
    public ResponseEntity<? extends BasicResponse> findPw(
        @RequestBody MemberRequestDto memberRequestDto) {
        String result = memberService.findPw(memberRequestDto);
        if (!"".equals(result)) {
            return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse<>(result));
        } else {
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(StatusEnum.INTERNAL_SERVER_ERROR));
        }
    }
}