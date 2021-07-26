package com.lunchteam.lunchrestapi.api.repository;

import com.lunchteam.lunchrestapi.api.entity.MemberEntity;
import com.lunchteam.lunchrestapi.api.entity.QMemberEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Repository
public class MemberRepositorySupport extends QuerydslRepositorySupport {

    private final JPAQueryFactory queryFactory;

    QMemberEntity qMemberEntity = QMemberEntity.memberEntity;

    public MemberRepositorySupport(JPAQueryFactory queryFactory) {
        super(MemberEntity.class);
        this.queryFactory = queryFactory;
    }

    @Transactional
    public Optional<MemberEntity> findByEmail(String email) {
        List<MemberEntity> list = queryFactory.selectFrom(qMemberEntity)
            .where(
                qMemberEntity.delYn.eq("N")
                    .and(qMemberEntity.email.eq(email))
            )
            .fetch();
        return list.stream().findFirst();
    }

    public Optional<MemberEntity> findById(Long id) {
        List<MemberEntity> list = queryFactory.selectFrom(qMemberEntity)
            .where(
                qMemberEntity.delYn.eq("N")
                    .and(qMemberEntity.id.eq(id))
            )
            .fetch();
        return list.stream().findFirst();
    }
}
