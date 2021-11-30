package com.lunchteam.lunchrestapi.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VerifyUtil {

    /**
     * 아이디 빈칸 및 정규식 검사
     *
     * @param pid 아이디
     * @return true / false
     */
    public static boolean isValidId(String pid) {
        final String regex = "^[a-zA-Z]{1}[a-zA-Z0-9_]{4,11}$";
        final String blankPt = "(\\s)";
        Matcher matcher;

        matcher = Pattern.compile(blankPt).matcher(pid);
        if (!matcher.find()) {
            matcher = Pattern.compile(regex).matcher(pid);
            return matcher.find();
        }

        return false;
    }
}
