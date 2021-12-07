package com.lunchteam.lunchrestapi.api.repository;

import com.lunchteam.lunchrestapi.api.dto.member.MemberRequestDto;
import com.lunchteam.lunchrestapi.api.entity.MemberEntity;
import com.lunchteam.lunchrestapi.api.entity.QMemberEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Repository
public class MemberRepositorySupport extends QuerydslRepositorySupport {

    private final JPAQueryFactory queryFactory;
    private final PasswordEncoder passwordEncoder;

    QMemberEntity qMemberEntity = QMemberEntity.memberEntity;

    public MemberRepositorySupport(JPAQueryFactory queryFactory,
        PasswordEncoder passwordEncoder) {
        super(MemberEntity.class);
        this.queryFactory = queryFactory;
        this.passwordEncoder = passwordEncoder;
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
                    .and(qMemberEntity.id.eq(id))
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

    @Transactional
    public Optional<MemberEntity> findByEmailAndLoginId(MemberRequestDto memberRequestDto) {
        List<MemberEntity> list = queryFactory.selectFrom(qMemberEntity)
            .where(
                qMemberEntity.delYn.eq("N")
                    .and(qMemberEntity.email.eq(memberRequestDto.getEmail()))
                    .and(qMemberEntity.loginId.eq(memberRequestDto.getLoginId()))
            )
            .fetch();
        return list.stream().findFirst();
    }

    @Transactional
    public Long updatePasswordByLoginId(MemberRequestDto memberRequestDto) {
        return queryFactory.update(qMemberEntity)
            .set(qMemberEntity.password,
                passwordEncoder.encode(memberRequestDto.getNewPassword()))
            .set(qMemberEntity.updateDateTime, LocalDateTime.now())
            .where(
                qMemberEntity.loginId.eq(memberRequestDto.getLoginId())
                    .and(qMemberEntity.email.eq(memberRequestDto.getEmail()))
                    .and(qMemberEntity.id.eq(memberRequestDto.getMemberId()))
                    .and(qMemberEntity.delYn.eq("N"))
            ).execute();
    }
}
