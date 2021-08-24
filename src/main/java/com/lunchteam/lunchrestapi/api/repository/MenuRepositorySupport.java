package com.lunchteam.lunchrestapi.api.repository;

import com.lunchteam.lunchrestapi.api.entity.MenuEntity;
import com.lunchteam.lunchrestapi.api.entity.QMenuEntity;
import com.lunchteam.lunchrestapi.util.RandomUtil;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class MenuRepositorySupport extends QuerydslRepositorySupport {

    private final JPAQueryFactory queryFactory;

    QMenuEntity qMenuEntity = QMenuEntity.menuEntity;

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
}
