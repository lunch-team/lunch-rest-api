package com.lunchteam.lunchrestapi.util;

import java.util.Random;

public class RandomUtil {

    /**
     * 랜덤 숫자 배열
     *
     * @param start  시작 숫자
     * @param end    마지막 숫자
     * @param number 배열의 크기
     * @return 정수 배열
     */
    public static int[] getRandomNumberArray(int start, int end, int number) {
        int[] result = new int[number];

        Random random = new Random();
        for (int i = 0; i < number; i++) {
            result[i] = random.nextInt(end - start + 1) + start;
            for (int j = 0; j < i; j++) {
                if (result[i] == result[j]) {
                    i--;
                }
            }
        }

        return result;
    }
}
