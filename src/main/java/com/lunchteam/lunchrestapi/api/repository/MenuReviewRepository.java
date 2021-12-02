package com.lunchteam.lunchrestapi.api.repository;

import com.lunchteam.lunchrestapi.api.entity.MenuReviewEntity;
import javax.annotation.Nonnull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuReviewRepository extends JpaRepository<MenuReviewEntity, Long> {

    boolean existsByIdAndUseYn(@Nonnull Long id, String useYn);
}
