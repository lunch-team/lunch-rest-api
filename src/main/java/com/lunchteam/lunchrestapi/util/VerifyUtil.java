package com.lunchteam.lunchrestapi.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.stereotype.Component;

@Component
public class VerifyUtil {

    private boolean validation(String param, String regex) {
        final String blankPt = "(\\s)";
        Matcher matcher;

        matcher = Pattern.compile(blankPt).matcher(param);
        if (!matcher.find()) {
            matcher = Pattern.compile(regex).matcher(param);
            return matcher.find();
        }
        return false;
    }

    /**
     * 아이디 빈칸 및 정규식 검사
     *
     * @param pid 아이디
     * @return true / false
     */
    public boolean isValidId(String pid) {
        final String regex = "^[a-zA-Z]{1}[a-zA-Z0-9_]{4,11}$";
        return validation(pid, regex);
    }

    /**
     * 비밀번호 정규식 검사
     *
     * @param pPw password
     * @return boolean
     */
    public boolean isValidPw(String pPw) {
        final String regex = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d~!@#$%^&*()+|=]{4,20}$";
        return validation(pPw, regex);
    }
}
