package com.lunchteam.lunchrestapi.api.entity;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
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

    private String menuId;

    @CreationTimestamp
    private LocalDateTime insertDateTime;
}
