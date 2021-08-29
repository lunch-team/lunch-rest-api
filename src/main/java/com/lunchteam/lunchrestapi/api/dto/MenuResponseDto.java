package com.lunchteam.lunchrestapi.api.dto;

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
public class MenuResponseDto extends BasicResponseDto {

    private Long id;
    private String location;
    private String name;
    private String menuType;
    private int visitCount;
    private LocalDateTime recentVisit;

    public static MenuResponseDto of(MenuEntity menu) {
        return new MenuResponseDto(
            menu.getId(),
            menu.getLocation(),
            menu.getName(),
            menu.getMenuType(),
            menu.getVisitCount(),
            menu.getRecentVisit()
        );
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
}
