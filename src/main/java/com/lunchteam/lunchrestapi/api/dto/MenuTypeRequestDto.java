package com.lunchteam.lunchrestapi.api.dto;

import com.lunchteam.lunchrestapi.api.entity.MenuTypeEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MenuTypeRequestDto {

    private Long id;
    private String menuType;
    private String menuName;

    public MenuTypeEntity toMenuTypeEntity() {
        return MenuTypeEntity.AddMenuType()
            .menuType(menuType)
            .menuName(menuName)
            .build();
    }
}
