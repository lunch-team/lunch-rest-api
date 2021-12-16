package com.lunchteam.lunchrestapi.api.repository;

import com.lunchteam.lunchrestapi.api.entity.MemberLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberLogRepository extends JpaRepository<MemberLogEntity, Long> {
    
}
