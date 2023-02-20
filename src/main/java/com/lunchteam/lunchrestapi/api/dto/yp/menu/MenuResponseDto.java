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

    // 메뉴 대분류 명
    private String categoryName1;

    // 메뉴 중분류 명
    private String categoryName2;

    // 메뉴 소분류 명
    private String categoryName3;

    // 제품 이미지 url
    private String imgUrl;

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
            .categoryName1(menu.getCategoryName1())
            .categoryName2(menu.getCategoryName2())
            .categoryName3(menu.getCategoryName3())
            .imgUrl(menu.getImgUrl())
            .regDt(menu.getRegDt())
            .build();
    }
}
