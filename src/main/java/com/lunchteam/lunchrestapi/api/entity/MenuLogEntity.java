package com.lunchteam.lunchrestapi.api.entity;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

@Getter
@NoArgsConstructor
@Table(name = "lunch_log")
@Entity
public class MenuLogEntity {

    @Id
    @GeneratedValue
    private Long id;

    private Long menuId;

    @CreationTimestamp
    private LocalDateTime insertDateTime;

    @Builder(builderClassName = "AddMenuLog", builderMethodName = "AddMenuLog")
    public MenuLogEntity(
        Long menuId
    ) {
        this.menuId = menuId;
    }

    @Builder(builderClassName = "UpdateMenuLog", builderMethodName = "UpdateMenuLog")
    public MenuLogEntity(Long menuId, LocalDateTime insertDateTime) {
        this.menuId = menuId;
        this.insertDateTime = insertDateTime;
    }
}
