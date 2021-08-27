package com.lunchteam.lunchrestapi.api.entity;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Getter
@NoArgsConstructor
@Table(name = "lunch_menu_type",
    uniqueConstraints = {
        @UniqueConstraint(
            columnNames = {"menu_type"}
        )
    })
@Entity
public class MenuTypeEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "menu_type")
    private String menuType;

    private String menuName;

    @Column(columnDefinition = "varchar(1) default 'Y'")
    private String useYn;

    @CreationTimestamp
    private LocalDateTime insertDateTime;

    @UpdateTimestamp
    private LocalDateTime updateDateTime;

    @Builder(builderClassName = "AddMenuType", builderMethodName = "AddMenuType")
    public MenuTypeEntity(
        String menuType,
        String menuName
    ) {
        this.menuName = menuName;
        this.menuType = menuType;
        this.useYn = "Y";
    }
}
