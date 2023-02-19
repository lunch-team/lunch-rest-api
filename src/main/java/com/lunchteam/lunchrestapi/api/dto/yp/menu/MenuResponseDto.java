package com.lunchteam.lunchrestapi.api.dto.yp.menu;

import com.lunchteam.lunchrestapi.api.dto.BasicResponseDto;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MenuResponseDto extends BasicResponseDto {

    // 메뉴 id
    private Long id;

    // 메뉴 그룹 id
    private Long grpId;

    // 메뉴 명
    private String name;

    // 메뉴 순서
    private int order;

    // 메뉴 대분류
    private Long category1;

    // 메뉴 중분류
    private Long category2;

    // 메뉴 소분류
    private Long category3;
    private LocalDateTime regDt;

    public static MenuResponseDto of(Menu menu) {
        return MenuResponseDto.builder()
            .id(menu.getId())
            .grpId(menu.getGrpId())
            .name(menu.getName())
            .order(menu.getOrder())
            .category1(menu.getCategory1())
            .category2(menu.getCategory2())
            .category3(menu.getCategory3())
            .regDt(menu.getRegDt())
            .build();
    }
}
