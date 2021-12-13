package com.lunchteam.lunchrestapi.api.controller;

import com.lunchteam.lunchrestapi.api.dto.member.MemberRequestDto;
import com.lunchteam.lunchrestapi.api.dto.member.MemberResponseDto;
import com.lunchteam.lunchrestapi.api.entity.MemberEntity;
import com.lunchteam.lunchrestapi.api.exception.AuthenticationException;
import com.lunchteam.lunchrestapi.api.response.BasicResponse;
import com.lunchteam.lunchrestapi.api.response.CommonResponse;
import com.lunchteam.lunchrestapi.api.response.ErrorResponse;
import com.lunchteam.lunchrestapi.api.response.StatusEnum;
import com.lunchteam.lunchrestapi.api.service.AuthService;
import com.lunchteam.lunchrestapi.api.service.MemberService;
import com.lunchteam.lunchrestapi.security.dto.TokenDto;
import com.lunchteam.lunchrestapi.security.dto.TokenRequestDto;
import java.util.HashMap;
import java.util.Map;
import javax.validation.Valid;
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
        @Valid @RequestBody MemberRequestDto memberRequestDto) {

        try {
            String error = authService.signup(memberRequestDto);
            return error == null
                ? ResponseEntity.ok().build()
                : ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(error));
        } catch (Exception e) {
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(StatusEnum.INTERNAL_SERVER_ERROR));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<HashMap<String, Object>> login(
        @RequestBody MemberRequestDto memberRequestDto) {
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("token", authService.login(memberRequestDto));

        MemberEntity member = authService.getMemberInfo(memberRequestDto);
        resultMap.put("memberInfo",
            MemberEntity.UserLoginInfo()
                .id(member.getId())
                .loginId(member.getLoginId())
                .name(member.getName())
                .build());
        return ResponseEntity.ok(resultMap);
    }

    @PostMapping("/reissue")
    public ResponseEntity<TokenDto> reissue(
        @RequestBody TokenRequestDto tokenRequestDto) {
        return ResponseEntity.ok(authService.reissue(tokenRequestDto));
    }

    @PostMapping("/findId")
    public ResponseEntity<? extends BasicResponse> findId(
        @RequestBody MemberRequestDto memberRequestDto) {
        try {
            MemberResponseDto member = memberService.findId(memberRequestDto);
            if (member != null) {
                return ResponseEntity
                    .ok()
                    .body(new CommonResponse<>(member.removeId(member)));
            } else {
                throw new AuthenticationException("error");
            }
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(e.getMessage()));
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

        try {
            Map<String, Object> result = memberService.findPw(memberRequestDto);
            if (result.containsKey("memberId")) {
                return ResponseEntity.status(HttpStatus.OK)
                    .body(new CommonResponse<>(result));
            } else {
                throw new AuthenticationException((String) result.get("errMsg"));
            }
        } catch (AuthenticationException e) {
            return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(e.getMessage()));
        }
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<? extends BasicResponse> resetPassword(
        @RequestBody MemberRequestDto memberRequestDto) {

        try {
            Map<String, Object> result = memberService.resetPassword(memberRequestDto);
            if (result.containsKey("loginId")) {
                return ResponseEntity.status(HttpStatus.OK)
                    .body(new CommonResponse<>(result));
            } else {
                throw new AuthenticationException((String) result.get("errMsg"));
            }
        } catch (AuthenticationException e) {
            return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(e.getMessage()));
        }
    }
}