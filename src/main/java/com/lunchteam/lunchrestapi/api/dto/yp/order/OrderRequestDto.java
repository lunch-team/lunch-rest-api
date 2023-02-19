package com.lunchteam.lunchrestapi.api.dto.yp.order;

import com.lunchteam.lunchrestapi.api.dto.yp.menu.Menu.MenuType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequestDto {

    // 주문 id
    private Long id;

    // 메뉴 그룹 id
    private Long grpId;

    // 메뉴 종류
    private MenuType menuType;

    // 메뉴 명
    private String name;

    // 메뉴 대분류
    private Long category1;

    // 메뉴 중분류
    private Long category2;

    // 메뉴 소분류
    private Long category3;
}