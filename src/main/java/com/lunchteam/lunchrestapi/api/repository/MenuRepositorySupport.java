package com.lunchteam.lunchrestapi.api.repository;

import com.lunchteam.lunchrestapi.api.dto.OrderEnum;
import com.lunchteam.lunchrestapi.api.dto.menu.MenuRequestDto;
import com.lunchteam.lunchrestapi.api.dto.menu.MenuResult;
import com.lunchteam.lunchrestapi.api.entity.MenuEntity;
import com.lunchteam.lunchrestapi.api.entity.MenuLogEntity;
import com.lunchteam.lunchrestapi.api.entity.QMenuEntity;
import com.lunchteam.lunchrestapi.api.entity.QMenuLogEntity;
import com.lunchteam.lunchrestapi.api.entity.QMenuReviewEntity;
import com.lunchteam.lunchrestapi.api.entity.QMenuTypeEntity;
import com.lunchteam.lunchrestapi.util.RandomUtil;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class MenuRepositorySupport extends QuerydslRepositorySupport {

    private final JPAQueryFactory queryFactory;

    QMenuEntity qMenuEntity = QMenuEntity.menuEntity;
    QMenuTypeEntity qMenuTypeEntity = QMenuTypeEntity.menuTypeEntity;
    QMenuLogEntity qMenuLogEntity = QMenuLogEntity.menuLogEntity;
    QMenuReviewEntity qMenuReviewEntity = QMenuReviewEntity.menuReviewEntity;

    public MenuRepositorySupport(JPAQueryFactory queryFactory) {
        super(MenuEntity.class);
        this.queryFactory = queryFactory;
    }

    @Transactional
    public List<MenuResult> getRandomMenu(int count) {
        final int EXCEPT_NUM = 5;
        long totalCnt = queryFactory.selectFrom(qMenuEntity)
            .where(qMenuEntity.useYn.eq("Y")).fetchCount();
        log.debug("total count: " + totalCnt + ", random number: " + count);
        if (count < totalCnt || count > 0) {
            List<MenuResult> tmp = new ArrayList<>();

            JPAQuery<MenuResult> query = queryFactory
                .select(
                    Projections.fields(
                        MenuResult.class,
                        qMenuEntity.id,
                        qMenuEntity.name,
                        qMenuEntity.menuType,
                        qMenuTypeEntity.menuName,
                        qMenuEntity.location
                    )
                ).from(qMenuEntity)
                .where(
                    qMenuEntity.useYn.eq("Y")
                );

            // fetchJoin에서 Limit 적용안되는 이슈로 인해 limit 조건 주석처리
//            if (totalCnt > EXCEPT_NUM) {
//                query.where(
//                    qMenuEntity.id.notIn(
//                        JPAExpressions
//                            .select(qMenuLogEntity.menuId)
//                            .from(qMenuLogEntity)
//                            .orderBy(qMenuLogEntity.insertDateTime.desc())
//                            .limit(EXCEPT_NUM)
//                    )
//                );
//            } else {
//                log.debug("totalCnt <= EXCEPT_NUM");
//            }

            query.join(qMenuTypeEntity)
                .on(qMenuEntity.menuType.eq(qMenuTypeEntity.menuType));

            List<MenuResult> list = query.fetch();

            int[] randomInt = RandomUtil.getRandomNumberArray(0, list.size() - 1, count);
            log.debug(Arrays.toString(randomInt));
            for (int i = 0; i < count; i++) {
                log.debug(String.valueOf(list.get(randomInt[i])));
                tmp.add(list.get(randomInt[i]));
            }
            return tmp;
        } else {
            log.debug("No MenuList");
            return null;
        }
    }

    @Transactional
    public List<MenuResult> getAllMenu(MenuRequestDto menuRequestDto) {
        DateTimePath<LocalDateTime> recentVisit
            = Expressions.dateTimePath(LocalDateTime.class, "recentVisit");
        NumberPath<Long> visitCount
            = Expressions.numberPath(Long.class, "visitCount");
        NumberPath<Double> star
            = Expressions.numberPath(Double.class, "star");
        JPAQuery<MenuResult> query = queryFactory
            .select(
                Projections.fields(
                    MenuResult.class,
                    qMenuEntity.id,
                    qMenuEntity.location,
                    qMenuEntity.name,
                    qMenuEntity.menuType,
                    qMenuTypeEntity.menuName,
                    Expressions.as(
                      JPAExpressions.select(qMenuReviewEntity.star.avg())
                          .from(qMenuReviewEntity)
                          .where(qMenuReviewEntity.menuId.eq(qMenuEntity.id)), star
                    ),
                    ExpressionUtils.as(
                        JPAExpressions.select(qMenuLogEntity.insertDateTime.max())
                            .from(qMenuLogEntity)
                            .where(qMenuLogEntity.menuId.eq(qMenuEntity.id)), recentVisit
                    ),
                    ExpressionUtils.as(
                        JPAExpressions.select(qMenuLogEntity.id.count())
                            .from(qMenuLogEntity)
                            .where(qMenuLogEntity.menuId.eq(qMenuEntity.id)), visitCount
                    ),
                    qMenuEntity.insertDateTime
                )
            ).from(qMenuEntity)
            .where(qMenuEntity.useYn.eq("Y"));
        if (!"".equals(menuRequestDto.getMenuType())
            && menuRequestDto.getMenuType() != null
            && !"all".equals(menuRequestDto.getMenuType())
        ) {
            query.where(qMenuEntity.menuType.eq(menuRequestDto.getMenuType()));
        }
        query.leftJoin(qMenuTypeEntity)
            .on(qMenuEntity.menuType.eq(qMenuTypeEntity.menuType));

        // 정렬 타입
        if ("recent".equalsIgnoreCase(menuRequestDto.getOrderType().getValue())) {
            // 정렬 순서
            if ("DESC".equalsIgnoreCase(menuRequestDto.getOrder().getValue())) {
                query.orderBy(recentVisit.desc());
            } else {
                query.orderBy(recentVisit.asc());
            }
        } else if ("count".equalsIgnoreCase(menuRequestDto.getOrderType().getValue())) {
            if ("DESC".equalsIgnoreCase(menuRequestDto.getOrder().getValue())) {
                query.orderBy(visitCount.desc());
            } else {
                query.orderBy(visitCount.asc());
            }
        }
        // 항상 ABC 순
        query.orderBy(qMenuEntity.name.asc());

        return query.fetch();
    }

    @Transactional
    public long modifyMenuById(MenuEntity menu) {
        return queryFactory.update(qMenuEntity)
            .set(qMenuEntity.name, menu.getName())
            .set(qMenuEntity.menuType, menu.getMenuType())
            .set(qMenuEntity.location, menu.getLocation())
            .set(qMenuEntity.updateDateTime, menu.getUpdateDateTime())
            .where(qMenuEntity.id.eq(menu.getId()), qMenuEntity.useYn.eq("Y"))
            .execute();
    }

    @Transactional
    public long deleteMenuById(Long id) {
        return queryFactory.update(qMenuEntity)
            .set(qMenuEntity.useYn, "N")
            .set(qMenuEntity.updateDateTime, LocalDateTime.now())
            .where(qMenuEntity.id.eq(id), qMenuEntity.useYn.eq("Y"))
            .execute();
    }

    @Transactional
    public boolean existsByMenuType(String menuType) {
        return queryFactory.selectFrom(qMenuTypeEntity)
            .where(qMenuTypeEntity.menuType.eq(menuType), qMenuTypeEntity.useYn.eq("Y"))
            .fetchCount() > 0;
    }

    @Transactional
    public List<MenuResult> getMenuType() {
        return queryFactory.select(Projections.fields(MenuResult.class,
                qMenuTypeEntity.id,
                qMenuTypeEntity.menuName,
                qMenuTypeEntity.menuType))
            .from(qMenuTypeEntity)
            .where(qMenuTypeEntity.useYn.eq("Y"))
            .fetch();
    }

    @Transactional
    public List<MenuResult> getVisitMenuList(MenuRequestDto menuRequestDto) {
        JPAQuery<MenuResult> query = queryFactory.select(Projections.fields(
                MenuResult.class,
                qMenuLogEntity.id,
                qMenuEntity.location,
                qMenuEntity.name,
                qMenuEntity.menuType,
                qMenuTypeEntity.menuName,
                qMenuLogEntity.insertDateTime))
            .from(qMenuEntity)
            .join(qMenuLogEntity)
            .on(qMenuLogEntity.menuId.eq(qMenuEntity.id))
            .join(qMenuTypeEntity)
            .on(qMenuEntity.menuType.eq(qMenuTypeEntity.menuType));
        if (menuRequestDto.getOrder() == OrderEnum.DESC) {
            query.orderBy(qMenuLogEntity.insertDateTime.desc());
        } else {
            query.orderBy(qMenuLogEntity.insertDateTime.asc());
        }

        return query.fetch();
    }

    @Transactional
    public long deleteMenuLogById(Long id) {
        return queryFactory.delete(qMenuLogEntity)
            .where(qMenuLogEntity.id.eq(id))
            .execute();
    }

    @Transactional
    public long updateMenuLogById(MenuLogEntity menuLog) {
        return queryFactory.update(qMenuLogEntity)
            .set(qMenuLogEntity.insertDateTime, menuLog.getInsertDateTime())
            .where(qMenuLogEntity.id.eq(menuLog.getId()))
            .execute();
    }
}
