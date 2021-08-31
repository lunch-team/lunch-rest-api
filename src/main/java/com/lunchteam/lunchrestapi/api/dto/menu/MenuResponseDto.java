package com.lunchteam.lunchrestapi.api.dto.menu;

import com.lunchteam.lunchrestapi.api.dto.BasicResponseDto;
import com.lunchteam.lunchrestapi.api.entity.MenuEntity;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MenuResponseDto extends BasicResponseDto {

    private Long id;
    private String location;
    private String name;
    private String menuType;
    private String menuName;
    private LocalDateTime recentVisit;

    public static MenuResponseDto of(MenuEntity menu) {
        return MenuResponseDto.builder()
            .id(menu.getId())
            .name(menu.getName())
            .menuType(menu.getMenuType())
            .build();
    }

    public static List<MenuResponseDto> listOf(List<MenuEntity> menus) {
        if (menus.isEmpty()) {
            return null;
        } else {
            List<MenuResponseDto> result = new ArrayList<>();
            for (MenuEntity menu : menus) {
                result.add(MenuResponseDto.of(menu));
            }
            return result;
        }
    }

    public static MenuResponseDto of(MenuResult menu) {
        return MenuResponseDto.builder()
            .id(menu.getId())
            .location(menu.getLocation())
            .name(menu.getName())
            .menuType(menu.getMenuType())
            .menuName(menu.getMenuName())
            .recentVisit(menu.getRecentVisit())
            .build();
    }

    public static List<MenuResponseDto> listOfResult(List<MenuResult> menus) {
        if (menus.isEmpty()) {
            return null;
        } else {
            List<MenuResponseDto> result = new ArrayList<>();
            for (MenuResult menu : menus) {
                result.add(MenuResponseDto.of(menu));
            }
            return result;
        }
    }
}
