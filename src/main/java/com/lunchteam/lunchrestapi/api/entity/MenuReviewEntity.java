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
import org.hibernate.annotations.UpdateTimestamp;

@Getter
@NoArgsConstructor
@Table(name = "lunch_menu_review")
@Entity
public class MenuReviewEntity {

    @Id
    @GeneratedValue
    private Long id;

    private Long menuId;

    private String insertMemberId;

    private int star;

    private String contents;

    private String fileId;

    private String useYn;

    @CreationTimestamp
    private LocalDateTime insertDateTime;

    @UpdateTimestamp
    private LocalDateTime updateDateTime;

}
