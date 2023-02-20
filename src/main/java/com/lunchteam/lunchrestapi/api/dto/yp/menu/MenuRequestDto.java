package com.lunchteam.lunchrestapi.api.dto.yp.menu;

import com.lunchteam.lunchrestapi.api.dto.yp.menu.Menu.MenuType;
import com.lunchteam.lunchrestapi.api.entity.yp.MenuEntity;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MenuRequestDto {

    // 메뉴 id
    private Long id;

    // 메뉴 그룹 id
    private Long grpId;

    // 메뉴 종류
    private MenuType menuType;

    // 메뉴 명
    @NotBlank
    private String name;

    // 메뉴 대분류
    private Long category1;

    // 메뉴 중분류
    private Long category2;

    // 메뉴 소분류
    private Long category3;

    // 제품 이미지 url
    private String imgUrl;

    // 요청한 로그인 유저의 아이디
    @NotNull
    private Long userId;

    public MenuEntity toMenuByMenuType(MenuType menuType) {
        return MenuEntity.AddMenu()
            .grpId(grpId)
            .type(menuType.getValue())
            .name(name)
            .category1(category1)
            .category2(category2)
            .category3(category3)
            .imgUrl(imgUrl)
            .regId(userId)
            .build();
    }
}