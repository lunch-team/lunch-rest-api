package com.lunchteam.lunchrestapi.api.repository;

import com.lunchteam.lunchrestapi.api.dto.MemberRequestDto;
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

    @Transactional
    public Optional<MemberEntity> findById(Long id) {
        List<MemberEntity> list = queryFactory.selectFrom(qMemberEntity)
            .where(
                qMemberEntity.delYn.eq("N")
       )
            .fetch();
        return list.stream().findFirst();
    }

    @Transactional
    public Optional<MemberEntity> findByEmailAndName(MemberRequestDto memberRequestDto) {
        List<MemberEntity> list = queryFactory.selectFrom(qMemberEntity)
            .where(
                qMemberEntity.delYn.eq("N")
                    .and(qMemberEntity.email.eq(memberRequestDto.getEmail()))
                    .and(qMemberEntity.name.eq(memberRequestDto.getName()))
            )
            .fetch();
        return list.stream().findFirst();
    }

    public Optional<MemberEntity> findByEmailAndLoginId(MemberRequestDto memberRequestDto) {
        List<MemberEntity> list = queryFactory.selectFrom(qMemberEntity)
            .where(
                qMemberEntity.delYn.eq("N")
                    .and(qMemberEntity.email.eq(memberRequestDto.getEmail()))
                    .and(qMemberEntity.loginId.eq(memberRequestDto.getName()))
            )
            .fetch();
        return list.stream().findFirst();
    }

    public Optional<MemberEntity> updatePasswordByLoginId(MemberRequestDto memberRequestDto) {
        return null;
    }
}
