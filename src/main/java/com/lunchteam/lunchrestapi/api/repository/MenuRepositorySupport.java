package com.lunchteam.lunchrestapi.api.repository;

import com.lunchteam.lunchrestapi.api.entity.MenuEntity;
import com.lunchteam.lunchrestapi.api.entity.QMenuEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;
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
    public Optional<MenuEntity> getRandomMenu(int count) {
        List<?> totalCnt = queryFactory.selectFrom(qMenuEntity)
            .where(qMenuEntity.useYn.eq("Y")).fetch();
        log.debug(String.valueOf(totalCnt.get(0)));
        List<MenuEntity> list = queryFactory.selectFrom(qMenuEntity)
            .where(qMenuEntity.useYn.eq("Y")).fetch();

        return list.stream().findAny();
    }
}
