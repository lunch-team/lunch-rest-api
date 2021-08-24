package com.lunchteam.lunchrestapi.api.dto;

import com.lunchteam.lunchrestapi.api.entity.MenuEntity;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MenuRequestDto {

    private String location;
    @NotBlank
    private String name;
    private String menuType;

    public MenuEntity toMenu() {
        return MenuEntity.AddMenu()
            .location(location)
            .name(name)
            .menuType(menuType)
            .build();
    }
}
