package com.lunchteam.lunchrestapi.api.repository;

import com.lunchteam.lunchrestapi.api.dto.menu.MenuResult;
import com.lunchteam.lunchrestapi.api.dto.menu.MenuReviewRequestDto;
import com.lunchteam.lunchrestapi.api.dto.menu.MenuReviewResult;
import com.lunchteam.lunchrestapi.api.entity.MenuReviewEntity;
import com.lunchteam.lunchrestapi.api.entity.QMenuEntity;
import com.lunchteam.lunchrestapi.api.entity.QMenuLogEntity;
import com.lunchteam.lunchrestapi.api.entity.QMenuReviewEntity;
import com.lunchteam.lunchrestapi.api.entity.QMenuTypeEntity;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class MenuReviewRepositorySupport extends QuerydslRepositorySupport {

    private final JPAQueryFactory queryFactory;

    QMenuEntity qMenuEntity = QMenuEntity.menuEntity;
    QMenuTypeEntity qMenuTypeEntity = QMenuTypeEntity.menuTypeEntity;
    QMenuLogEntity qMenuLogEntity = QMenuLogEntity.menuLogEntity;
    QMenuReviewEntity qMenuReviewEntity = QMenuReviewEntity.menuReviewEntity;

    public MenuReviewRepositorySupport(JPAQueryFactory queryFactory) {
        super(MenuReviewEntity.class);
        this.queryFactory = queryFactory;
    }

    @Transactional
    public List<MenuReviewResult> getReviewList(MenuReviewRequestDto menuDto) {
        JPAQuery<MenuReviewResult> query = queryFactory
            .select(
                Projections.fields(
                    MenuReviewResult.class,
                    qMenuReviewEntity.id,
                    qMenuReviewEntity.menuId,
                    qMenuReviewEntity.fileId,
                    qMenuReviewEntity.contents,
                    qMenuReviewEntity.insertDateTime,
                    qMenuReviewEntity.insertMemberId,
                    qMenuReviewEntity.star
                )
            ).from(qMenuReviewEntity)
            .where(qMenuReviewEntity.useYn.eq("Y"))
            .where(qMenuReviewEntity.menuId.eq(menuDto.getMenuId()))
            .orderBy(qMenuReviewEntity.insertDateTime.desc());
        return query.fetch();
    }
}
