package com.lunchteam.lunchrestapi.api.repository;

import com.lunchteam.lunchrestapi.api.entity.MenuLogEntity;
import javax.annotation.Nonnull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuLogRepository extends JpaRepository<MenuLogEntity, Long> {
    boolean existsById(@Nonnull Long id);
}
