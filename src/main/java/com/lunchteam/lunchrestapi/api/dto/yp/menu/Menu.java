package com.lunchteam.lunchrestapi.api.dto.yp.menu;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class Menu {

    // 메뉴 id
    private Long id;

    // 메뉴 그룹 id
    private Long grpId;

    // 메뉴 타입
    private String type;

    // 메뉴 명
    private String name;

    // 메뉴 순서
    private int order;

    // 메뉴 대분류
    private Long category1;
    private String categoryName1;

    // 메뉴 중분류
    private Long category2;
    private String categoryName2;

    // 메뉴 소분류
    private Long category3;
    private String categoryName3;

    private Long priceLarge;
    private Long priceMedium;

    // 제품 이미지 url
    private String imgUrl;

    private LocalDateTime regDt;
    private Long regId;

    private LocalDateTime uptDt;
    private Long uptId;

    @Getter
    @RequiredArgsConstructor
    public enum MenuType {

        etc("E"),
        dough("D"),
        pizza("P");

        private final String value;

        public boolean isEmpty() {
            return this.value.isEmpty();
        }
    }
}
