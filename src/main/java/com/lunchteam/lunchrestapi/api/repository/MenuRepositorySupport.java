package com.lunchteam.lunchrestapi.api.repository;

import com.lunchteam.lunchrestapi.api.dto.DtoEnum;
import com.lunchteam.lunchrestapi.api.dto.menu.MenuRequestDto;
import com.lunchteam.lunchrestapi.api.dto.menu.MenuResult;
import com.lunchteam.lunchrestapi.api.entity.MenuEntity;
import com.lunchteam.lunchrestapi.api.entity.QMenuEntity;
import com.lunchteam.lunchrestapi.api.entity.QMenuLogEntity;
import com.lunchteam.lunchrestapi.api.entity.QMenuTypeEntity;
import com.lunchteam.lunchrestapi.util.RandomUtil;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
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

    public MenuRepositorySupport(JPAQueryFactory queryFactory) {
        super(MenuEntity.class);
        this.queryFactory = queryFactory;
    }

    @Transactional
    public List<MenuResult> getRandomMenu(int count) {
        long totalCnt = queryFactory.selectFrom(qMenuEntity)
            .where(qMenuEntity.useYn.eq("Y")).fetchCount();
        log.debug("total count: " + totalCnt + ", random number: " + count);
        if (count < totalCnt || count > 0) {
            List<MenuResult> tmp = new ArrayList<>();
            List<MenuResult> list
                = queryFactory
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
                        .and(
                            qMenuEntity.id.notIn(
                                JPAExpressions
                                    .select(qMenuLogEntity.menuId)
                                    .from(qMenuLogEntity)
                                    .orderBy(qMenuLogEntity.insertDateTime.desc())
                                    .limit(5)

                            )
                        )
                )
                .join(qMenuTypeEntity)
                .on(qMenuEntity.menuType.eq(qMenuTypeEntity.menuType))
                .fetch();

            int[] randomInt = RandomUtil.getRandomNumberArray(0, list.size() - 1, count);
            log.debug(Arrays.toString(randomInt));
            for (int i = 0; i < count; i++) {
                log.debug(String.valueOf(list.get(randomInt[i])));
                tmp.add(list.get(randomInt[i]));
            }
            return tmp;
        } else {
            return null;
        }
    }

    @Transactional
    public List<MenuResult> getAllMenu() {
        return queryFactory
            .select(
                Projections.fields(
                    MenuResult.class,
                    qMenuEntity.id,
                    qMenuEntity.location,
                    qMenuEntity.name,
                    qMenuEntity.menuType,
                    qMenuTypeEntity.menuName,
                    ExpressionUtils.as(
                        JPAExpressions.select(qMenuLogEntity.insertDateTime.max())
                            .from(qMenuLogEntity)
                            .where(qMenuLogEntity.menuId.eq(qMenuEntity.id)
                            ), "recentVisit"
                    ),
                    ExpressionUtils.as(
                        JPAExpressions.select(qMenuLogEntity.id.count())
                            .from(qMenuLogEntity)
                            .where(qMenuLogEntity.menuId.eq(qMenuEntity.id)
                            ), "visitCount"
                    ),
                    qMenuEntity.insertDateTime
                )
            ).from(qMenuEntity)
            .where(qMenuEntity.useYn.eq("Y"))
            .leftJoin(qMenuTypeEntity)
            .on(qMenuEntity.menuType.eq(qMenuTypeEntity.menuType))
            .fetch();
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
        if (menuRequestDto.getOrder() == DtoEnum.DESC) {
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
}
