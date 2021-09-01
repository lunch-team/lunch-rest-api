package com.lunchteam.lunchrestapi.api.dto.menu;

import com.lunchteam.lunchrestapi.api.dto.DtoEnum;
import com.lunchteam.lunchrestapi.api.entity.MenuEntity;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MenuRequestDto {

    // 메뉴 id
    private Long id;
    // 정확한 주소
    private String location;
    // 상호명
    @NotBlank
    private String name;
    // 메뉴
    private String menuType;
    // 랜덤으로 가져올 메뉴 개수
    private int randomNumber;

    private DtoEnum order;

    /**
     * 메뉴 추가
     *
     * @return Menu Entity
     */
    public MenuEntity toMenu() {
        return MenuEntity.AddMenu()
            .location(location)
            .name(name)
            .menuType(menuType)
            .build();
    }
}