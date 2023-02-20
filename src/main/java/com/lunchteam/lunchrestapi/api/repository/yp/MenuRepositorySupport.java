package com.lunchteam.lunchrestapi.api.repository.yp;

import com.lunchteam.lunchrestapi.api.dto.yp.menu.Menu;
import com.lunchteam.lunchrestapi.api.dto.yp.menu.MenuRequestDto;
import com.lunchteam.lunchrestapi.api.entity.yp.MenuEntity;
import com.lunchteam.lunchrestapi.api.entity.yp.QMenuCategoryEntity;
import com.lunchteam.lunchrestapi.api.entity.yp.QMenuEntity;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository("yp.MenuRepositorySupport")
public class MenuRepositorySupport extends QuerydslRepositorySupport {

    private final JPAQueryFactory queryFactory;

    QMenuEntity qMenu = QMenuEntity.menuEntity;
    QMenuCategoryEntity qMenuCategory = QMenuCategoryEntity.menuCategoryEntity;

    public MenuRepositorySupport(JPAQueryFactory queryFactory) {
        super(MenuEntity.class);
        this.queryFactory = queryFactory;
    }

    @Transactional
    public List<Menu> selectAllMenuListByType(MenuRequestDto dto) {
        StringPath categoryName1 = Expressions.stringPath("categoryName1");
        StringPath categoryName2 = Expressions.stringPath("categoryName2");
        StringPath categoryName3 = Expressions.stringPath("categoryName3");
        JPAQuery<Menu> query = queryFactory
            .select(Projections.fields(Menu.class,
                    qMenu.id,
                    qMenu.grpId,
                    qMenu.name,
                    qMenu.type,
                    qMenu.category1,
                    qMenu.category2,
                    qMenu.category3,
                    Expressions.as(
                        JPAExpressions.select(qMenuCategory.name)
                            .from(qMenuCategory)
                            .where(qMenuCategory.id.eq(qMenu.category1)),
                        categoryName1
                    ),
                    Expressions.as(
                        JPAExpressions.select(qMenuCategory.name)
                            .from(qMenuCategory)
                            .where(qMenuCategory.id.eq(qMenu.category2)),
                        categoryName2
                    ),
                    Expressions.as(
                        JPAExpressions.select(qMenuCategory.name)
                            .from(qMenuCategory)
                            .where(qMenuCategory.id.eq(qMenu.category3)),
                        categoryName3
                    ),
                    qMenu.imgUrl,
                    qMenu.order,
                    qMenu.regDt
                )
            ).from(qMenu)
            .where(
                qMenu.type.eq(dto.getMenuType().getValue())
            );
        query.orderBy(qMenu.order.asc());

        return query.fetch();
    }

    @Transactional
    public Menu selectMenuByTypeAndId(MenuRequestDto dto) {
        StringPath categoryName1 = Expressions.stringPath("categoryName1");
        StringPath categoryName2 = Expressions.stringPath("categoryName2");
        StringPath categoryName3 = Expressions.stringPath("categoryName3");
        JPAQuery<Menu> query = queryFactory
            .select(Projections.fields(Menu.class,
                    qMenu.id,
                    qMenu.grpId,
                    qMenu.name,
                    qMenu.type,
                    qMenu.category1,
                    qMenu.category2,
                    qMenu.category3,
                    Expressions.as(
                        JPAExpressions.select(qMenuCategory.name)
                            .from(qMenuCategory)
                            .where(qMenuCategory.id.eq(qMenu.category1)),
                        categoryName1
                    ),
                    Expressions.as(
                        JPAExpressions.select(qMenuCategory.name)
                            .from(qMenuCategory)
                            .where(qMenuCategory.id.eq(qMenu.category2)),
                        categoryName2
                    ),
                    Expressions.as(
                        JPAExpressions.select(qMenuCategory.name)
                            .from(qMenuCategory)
                            .where(qMenuCategory.id.eq(qMenu.category3)),
                        categoryName3
                    ),
                    qMenu.imgUrl,
                    qMenu.order,
                    qMenu.regDt
                )
            ).from(qMenu)
            .where(
                qMenu.type.eq(dto.getMenuType().getValue()),
                qMenu.id.eq(dto.getId())
            );
        query.orderBy(qMenu.order.asc());

        Menu result = query.fetchFirst();
        log.debug(result.toString());
        return query.fetchFirst();
    }
}
