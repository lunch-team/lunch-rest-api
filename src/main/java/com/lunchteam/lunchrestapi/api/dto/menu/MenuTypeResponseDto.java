package com.lunchteam.lunchrestapi.api.dto.menu;

import com.lunchteam.lunchrestapi.api.dto.BasicResponseDto;
import com.lunchteam.lunchrestapi.api.entity.MenuTypeEntity;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MenuTypeResponseDto extends BasicResponseDto {

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

    public static List<MenuTypeResponseDto> listOf(List<MenuTypeEntity> menus) {
        if (!menus.isEmpty()) {
            List<MenuTypeResponseDto> result = new ArrayList<>();

            for (MenuTypeEntity entity : menus) {
                result.add(MenuTypeResponseDto.of(entity));
            }

            return result;
        } else {
            return null;
        }
    }
}
