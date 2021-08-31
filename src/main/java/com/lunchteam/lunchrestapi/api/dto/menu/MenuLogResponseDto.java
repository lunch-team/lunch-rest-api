package com.lunchteam.lunchrestapi.api.dto.menu;

import com.lunchteam.lunchrestapi.api.dto.BasicResponseDto;
import com.lunchteam.lunchrestapi.api.entity.MenuEntity;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MenuLogResponseDto extends BasicResponseDto {

    private Long id;
    private String location;
    private String name;
    private String menuType;
    private LocalDateTime insertDateTime;

    public static MenuLogResponseDto of(MenuEntity menu) {
        return new MenuLogResponseDto(
            menu.getId(),
            menu.getLocation(),
            menu.getName(),
            menu.getMenuType(),
            menu.getInsertDateTime()
        );
    }

    public static List<MenuLogResponseDto> listOf(List<MenuEntity> menus) {
        if (menus.isEmpty()) {
            return null;
        } else {
            List<MenuLogResponseDto> result = new ArrayList<>();
            for (MenuEntity menu : menus) {
                result.add(MenuLogResponseDto.of(menu));
            }
            return result;
        }
    }

    public static MenuLogResponseDto of(MenuResult menu) {
        return new MenuLogResponseDto(
            menu.getId(),
            menu.getLocation(),
            menu.getName(),
            menu.getMenuType(),
            menu.getInsertDateTime()
        );
    }

    public static List<MenuLogResponseDto> listOfResult(List<MenuResult> menus) {
        if (menus.isEmpty()) {
            return null;
        } else {
            List<MenuLogResponseDto> result = new ArrayList<>();
            for (MenuResult menu : menus) {
                result.add(MenuLogResponseDto.of(menu));
            }
            return result;
        }
    }
}
