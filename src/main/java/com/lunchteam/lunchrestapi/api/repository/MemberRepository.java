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
    Optional<MemberEntity> findByEmail(String email);

    /**
     * 중복가입방지
     *
     * @param email email
     * @return bool
     */
    boolean existsByEmail(String email);
}
