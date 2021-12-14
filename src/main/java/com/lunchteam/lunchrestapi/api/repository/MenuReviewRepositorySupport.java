package com.lunchteam.lunchrestapi.api.repository;

import com.lunchteam.lunchrestapi.api.dto.menu.MenuReviewRequestDto;
import com.lunchteam.lunchrestapi.api.dto.menu.MenuReviewResult;
import com.lunchteam.lunchrestapi.api.entity.MenuReviewEntity;
import com.lunchteam.lunchrestapi.api.entity.QMemberEntity;
import com.lunchteam.lunchrestapi.api.entity.QMenuReviewEntity;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.List;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class MenuReviewRepositorySupport extends QuerydslRepositorySupport {

    private final JPAQueryFactory queryFactory;

    QMenuReviewEntity qMenuReviewEntity = QMenuReviewEntity.menuReviewEntity;
    QMemberEntity qMemberEntity = QMemberEntity.memberEntity;

    public MenuReviewRepositorySupport(JPAQueryFactory queryFactory) {
        super(MenuReviewEntity.class);
        this.queryFactory = queryFactory;
    }

    @Transactional
    public List<MenuReviewResult> getReviewList(MenuReviewRequestDto menuDto) {
        StringPath memberName
            = Expressions.stringPath("memberName");
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
                    ExpressionUtils.as(
                        JPAExpressions.select(qMemberEntity.name)
                            .from(qMemberEntity)
                            .where(qMemberEntity.id.eq(qMenuReviewEntity.insertMemberId))
                        , memberName
                    ),
                    qMenuReviewEntity.star
                )
            ).from(qMenuReviewEntity)
            .where(qMenuReviewEntity.useYn.eq("Y"));
        if (!menuDto.isAllFlag()) {
            query.where(qMenuReviewEntity.menuId.eq(menuDto.getMenuId()));
        }
        query.orderBy(qMenuReviewEntity.insertDateTime.desc());
        return query.fetch();
    }

    @Transactional
    public long deleteMenuReview(MenuReviewRequestDto menuDto) {
        return queryFactory.update(qMenuReviewEntity)
            .set(qMenuReviewEntity.useYn, "N")
            .set(qMenuReviewEntity.updateDateTime, LocalDateTime.now())
            .where(qMenuReviewEntity.id.eq(menuDto.getId()))
            .execute();
    }
}
