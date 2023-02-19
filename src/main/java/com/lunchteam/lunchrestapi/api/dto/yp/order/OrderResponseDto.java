package com.lunchteam.lunchrestapi.api.dto.yp.order;

import com.lunchteam.lunchrestapi.api.dto.BasicResponseDto;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponseDto extends BasicResponseDto {

    // 메뉴 id
    private Long id;

    // 메뉴 그룹 id
    private Long grpId;

    // 메뉴 명
    private String name;

    // 메뉴 순서
    private int order;

    // 메뉴 대분류
    private Long category1;

    // 메뉴 중분류
    private Long category2;

    // 메뉴 소분류
    private Long category3;
    private LocalDateTime regDt;

    public static OrderResponseDto of(Order order) {
        return OrderResponseDto.builder()
            .id(order.getId())
            .grpId(order.getGrpId())
            .name(order.getName())
            .order(order.getOrder())
            .category1(order.getCategory1())
            .category2(order.getCategory2())
            .category3(order.getCategory3())
            .regDt(order.getRegDt())
            .build();
    }
}
