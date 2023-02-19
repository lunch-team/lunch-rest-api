package com.lunchteam.lunchrestapi.api.entity.yp;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Getter
@NoArgsConstructor
@Table(name = "yp_menu")
@Entity(name = "yp.MenuEntity")
public class MenuEntity {

    @Id
    @GeneratedValue
    private Long id;

    private Long grpId;

    private String name;

    private String type;

    @Column(name = "category_1")
    private Long category1;
    @Column(name = "category_2")
    private Long category2;
    @Column(name = "category_3")
    private Long category3;

    @Column(name = "`order`")
    private int order;

    @CreationTimestamp
    private LocalDateTime regDt;
    private Long regId;

    @UpdateTimestamp
    private LocalDateTime uptDt;
    private Long uptId;

    @Builder(builderClassName = "AddMenu", builderMethodName = "AddMenu")
    public MenuEntity(
        Long grpId,
        String type,
        String name,
        Long category1,
        Long category2,
        Long category3,
        Long regId
    ) {
        this.grpId = grpId;
        this.type = type;
        this.name = name;
        this.category1 = category1;
        this.category2 = category2;
        this.category3 = category3;
        this.regId = regId;
        this.uptId = regId;
    }
}
