package com.lunchteam.lunchrestapi.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Slf4j
@NoArgsConstructor
public class SecurityUtil {

    /**
     * SecurityContext 에 유저 정보가 저장되는 시점
     * <p>
     * Request 가 들어올 때 JwtFilter 의 doFilter 에서 저장
     *
     * @return getName parse long
     */
    public static Long getCurrentMemberId() {
        // API 요청이 들어오면 필터에서 Access Token 을 복호화해서 유저 정보를 꺼내 SecurityContext에 저장
        final Authentication authentication = SecurityContextHolder.getContext()
            .getAuthentication();

        if (authentication == null || authentication.getName() == null) {
            throw new RuntimeException("Security Context 에 인증 정보가 없습니다.");
        }

        return Long.parseLong(authentication.getName());
    }

    /**
     * Salt 값 생성
     *
     * @return Salt 값
     */
    public String getSalt() {

        byte[] bytes = new byte[20];
        try {
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            random.nextBytes(bytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return new String(java.util.Base64.getEncoder().encode(bytes));
    }

    public String encryptPassword(String password, String salt) {
        if (password == null) {
            return "";
        }

        byte[] hashValue; // 해쉬값
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            md.reset();
            md.update(salt.getBytes());
            hashValue = md.digest(password.getBytes());
            return new String(Base64.encodeBase64(hashValue));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }
}
