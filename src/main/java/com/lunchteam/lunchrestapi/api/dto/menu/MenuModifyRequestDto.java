package com.lunchteam.lunchrestapi.api.dto.menu;

import com.lunchteam.lunchrestapi.api.entity.MenuEntity;
import com.lunchteam.lunchrestapi.api.entity.MenuLogEntity;
import java.time.LocalDateTime;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MenuModifyRequestDto {

    @NotNull(message = "no_id")
    private Long id;
    // 정확한 주소
    @NotBlank(message = "no_location")
    private String location;
    // 상호명
    @NotBlank(message = "no_name")
    private String name;
    // 메뉴
    @NotBlank(message = "no_menuType")
    private String menuType;

    private LocalDateTime insertDateTime;

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

    public MenuLogEntity toMenuLog() {
        return MenuLogEntity.AddMenuLog()
            .menuId(id)
            .build();
    }
}
