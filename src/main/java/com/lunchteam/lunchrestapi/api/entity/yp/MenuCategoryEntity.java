package com.lunchteam.lunchrestapi.api.entity.yp;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Table(name = "yp_menu_category")
@Entity
public class MenuCategoryEntity {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private String type;

    private int grade;

    private int order;

    private LocalDateTime regDt;
    private Long regId;

    private LocalDateTime uptDt;
    private Long uptId;
}
