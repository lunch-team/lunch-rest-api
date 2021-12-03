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

    private Long fileId;

    private Long insertMemberId;

    private int star;

    private String contents;

    private String useYn;

    @CreationTimestamp
    private LocalDateTime insertDateTime;

    @UpdateTimestamp
    private LocalDateTime updateDateTime;

    @Builder(builderClassName = "RegisterReview", builderMethodName = "RegisterReview")
    public MenuReviewEntity(
        Long menuId,
        Long insertMemberId,
        Long fileId,
        int star,
        String contents
    ) {
        this.menuId = menuId;
        this.insertMemberId = insertMemberId;
        this.fileId = fileId;
        this.star = star;
        this.contents = contents;
        this.useYn = "Y";
    }
}
