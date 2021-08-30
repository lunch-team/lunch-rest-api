package com.lunchteam.lunchrestapi.api.repository;

import com.lunchteam.lunchrestapi.api.dto.MenuRequestDto;
import com.lunchteam.lunchrestapi.api.entity.MenuEntity;
import com.lunchteam.lunchrestapi.api.entity.MenuTypeEntity;
import com.lunchteam.lunchrestapi.api.entity.QMenuEntity;
import com.lunchteam.lunchrestapi.api.entity.QMenuLogEntity;
import com.lunchteam.lunchrestapi.api.entity.QMenuTypeEntity;
import com.lunchteam.lunchrestapi.util.RandomUtil;
import com.querydsl.core.types.Projections;
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

    public MenuRepositorySupport(JPAQueryFactory queryFactory) {
        super(MenuEntity.class);
        this.queryFactory = queryFactory;
    }

    @Transactional
    public List<MenuEntity> getRandomMenu(int count) {
        long totalCnt = queryFactory.selectFrom(qMenuEntity)
            .where(qMenuEntity.useYn.eq("Y")).fetchCount();
        log.debug("total count: " + totalCnt + ", random number: " + count);
        if (count < totalCnt || count > 0) {
            List<MenuEntity> tmp = new ArrayList<>();
            List<MenuEntity> list = queryFactory.selectFrom(qMenuEntity)
                .where(qMenuEntity.useYn.eq("Y")).fetch();

            int[] randomInt = RandomUtil.getRandomNumberArray(0, (int) totalCnt - 1, count);
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
    public List<MenuEntity> getAllMenu() {
        return queryFactory.selectFrom(qMenuEntity)
            .where(qMenuEntity.useYn.eq("Y")).fetch();
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
    public long addVisitCountById(Long id) {
        long result = queryFactory.update(qMenuEntity)
            .set(qMenuEntity.visitCount, qMenuEntity.visitCount.add(1))
            .set(qMenuEntity.recentVisit, LocalDateTime.now())
            .where(qMenuEntity.id.eq(id), qMenuEntity.useYn.eq("Y"))
            .execute();
        if (result < 0) {
            return queryFactory.insert(qMenuLogEntity)
                .set(qMenuLogEntity.menuId, id)
                .execute();
        } else {
            return result;
        }
    }

    @Transactional
    public boolean existsByMenuType(String menuType) {
        return queryFactory.selectFrom(qMenuTypeEntity)
            .where(qMenuTypeEntity.menuType.eq(menuType), qMenuTypeEntity.useYn.eq("Y"))
            .fetchCount() > 0;
    }

    @Transactional
    public List<MenuTypeEntity> getMenuType() {
        return queryFactory.selectFrom(qMenuTypeEntity)
            .where(qMenuTypeEntity.useYn.eq("Y"))
            .fetch();
    }

    @Transactional
    public List<MenuEntity> getVisitMenuList(MenuRequestDto menuRequestDto) {
        return queryFactory.select(
                Projections.fields(
                    MenuEntity.class,
                    qMenuLogEntity.id,
                    qMenuEntity.location,
                    qMenuEntity.name,
                    qMenuEntity.visitCount,
                    qMenuLogEntity.insertDateTime))
            .from(qMenuEntity)
            .join(qMenuLogEntity)
            .on(qMenuLogEntity.menuId.eq(qMenuEntity.id))
            .orderBy(qMenuLogEntity.insertDateTime.asc())
            .fetch();
    }

}
