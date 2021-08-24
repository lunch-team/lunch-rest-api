package com.lunchteam.lunchrestapi.api.dto;

import com.lunchteam.lunchrestapi.api.entity.MenuEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MenuResponseDto {

    private String location;
    private String name;
    private String menuType;

    public static MenuResponseDto of(MenuEntity menu) {
        return new MenuResponseDto(
            menu.getLocation(),
            menu.getName(),
            menu.getMenuType()
        );
    }
}
