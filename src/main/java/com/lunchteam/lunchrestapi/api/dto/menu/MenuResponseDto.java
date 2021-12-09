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
    private Long visitCount;
    private Double star;
    private LocalDateTime recentVisit;
    private LocalDateTime insertDateTime;

    private static MenuResponseDto of(MenuResult menu) {
        Double star = menu.getStar();
        if (star == null) {
            star = 0.0;
        }
        return MenuResponseDto.builder()
            .id(menu.getId())
            .location(menu.getLocation())
            .name(menu.getName())
            .menuType(menu.getMenuType())
            .menuName(menu.getMenuName())
            .recentVisit(menu.getRecentVisit())
            .visitCount(menu.getVisitCount())
            .star(Math.round(star * 10) / 10.0)
            .build();
    }

    private static MenuResponseDto ofMenuLog(MenuResult menu) {
        return MenuResponseDto.builder()
            .id(menu.getId())
            .location(menu.getLocation())
            .name(menu.getName())
            .menuType(menu.getMenuType())
            .menuName(menu.getMenuName())
            .insertDateTime(menu.getInsertDateTime())
            .build();
    }

    private static MenuResponseDto ofMenuType(MenuResult menu) {
        return MenuResponseDto.builder()
            .id(menu.getId())
            .menuName(menu.getMenuName())
            .menuType(menu.getMenuType())
            .build();
    }

    public static MenuResponseDto ofOne(MenuResult menu) {
        return of(menu);
    }

    /**
     * JPA Repository 를 위한 MenuEntity to ResponseDto
     *
     * @param menu MenuEntity
     * @return id, name, menuType
     */
    public static MenuResponseDto ofMenuEntity(MenuEntity menu) {
        return MenuResponseDto.builder()
            .id(menu.getId())
            .name(menu.getName())
            .menuType(menu.getMenuType())
            .build();
    }

    public static List<MenuResponseDto> listOfMenuResult(List<MenuResult> menus) {
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

    public static List<MenuResponseDto> listOfMenuTypeResult(List<MenuResult> menus) {
        if (!menus.isEmpty()) {
            List<MenuResponseDto> result = new ArrayList<>();

            for (MenuResult entity : menus) {
                result.add(MenuResponseDto.ofMenuType(entity));
            }

            return result;
        } else {
            return null;
        }
    }

    public static List<MenuResponseDto> listOfLogResult(List<MenuResult> menus) {
        if (menus.isEmpty()) {
            return null;
        } else {
            List<MenuResponseDto> result = new ArrayList<>();
            for (MenuResult menu : menus) {
                result.add(MenuResponseDto.ofMenuLog(menu));
            }
            return result;
        }
    }

}
