package com.lunchteam.lunchrestapi.api.dto.menu;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MenuReviewRequestDto {

    private String contents;
    private Long menuId;
    private Long memberId;
    private int star;
}
