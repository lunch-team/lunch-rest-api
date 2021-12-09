package com.lunchteam.lunchrestapi.api.dto.menu;

import com.lunchteam.lunchrestapi.api.dto.OrderEnum;
import com.lunchteam.lunchrestapi.api.dto.OrderTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MenuReviewRequestDto {

    private Long id;
    private String contents;
    private Long menuId;
    private Long memberId;
    private Long fileId;
    private int star;

    // 정렬 타입 (recent, star)
    private OrderTypeEnum orderType;
    private OrderEnum order;

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }
}
