package com.lunchteam.lunchrestapi.api.dto.menu;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MenuResult {

    private Long id;
    private String location;
    private String name;
    private String menuId;
    private String menuType;
    private String menuName;
    private String day;
    private Long visitCount;
    private Double star;
    private LocalDateTime recentVisit;
    private LocalDateTime insertDateTime;
}
