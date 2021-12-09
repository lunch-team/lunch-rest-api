package com.lunchteam.lunchrestapi.api.repository;

import com.lunchteam.lunchrestapi.api.entity.MemberEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, Long> {

    /**
     * Email: Login ID
     *
     * @param email email
     * @return member
     */
    Optional<MemberEntity> findByEmailAndDelYn(String email, String delYn);

    /**
     * LoginId로 유저 정보 조회
     *
     * @param loginId login id
     * @return member info
     */
    Optional<MemberEntity> findByLoginId(String loginId);
    Optional<MemberEntity> findByLoginIdAndDelYn(String loginId, String delYn);

    /**
     * 중복가입방지
     *
     * @param email email
     * @return bool
     */
    boolean existsByEmailAndDelYn(String email, String delYn);

    /**
     * login id 중복 체크
     *
     * @param loginId loginId
     * @return bool
     */
    boolean existsByLoginIdAndDelYn(String loginId, String delYn);
}
