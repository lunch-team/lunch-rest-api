package com.lunchteam.lunchrestapi.api.dto;

import com.lunchteam.lunchrestapi.api.entity.MenuTypeEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MenuTypeResponseDto {

    private Long id;
    private String menuType;
    private String menuName;

    public static MenuTypeResponseDto of(MenuTypeEntity menu) {
        return new MenuTypeResponseDto(
            menu.getId(),
            menu.getMenuName(),
            menu.getMenuType()
        );
    }
}
