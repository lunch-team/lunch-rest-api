package com.lunchteam.lunchrestapi.api.entity;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Getter
@NoArgsConstructor
@Table(name = "lunch_menu")
@Entity
public class MenuEntity {

    @Id
    @GeneratedValue
    private Long id;

    private String location;

    private String menuType;

    private String name;

    private String useYn;

    @CreationTimestamp
    private LocalDateTime insertDateTime;

    @UpdateTimestamp
    private LocalDateTime updateDateTime;

    /**
     * 메뉴 추가
     *
     * @param name     상호명
     * @param location 도로명 주소
     * @param menuType 메뉴 타입
     */
    @Builder(builderClassName = "AddMenu", builderMethodName = "AddMenu")
    public MenuEntity(
        String name,
        String location,
        String menuType) {
        this.name = name;
        this.location = location;
        this.menuType = menuType;
        this.useYn = "Y";
    }

    @Builder(builderClassName = "ModifyMenu", builderMethodName = "ModifyMenu")
    public MenuEntity(
        Long id,
        String name,
        String location,
        String menuType
    ) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.menuType = menuType;
        this.updateDateTime = LocalDateTime.now();
    }
}
