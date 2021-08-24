package com.lunchteam.lunchrestapi.api.repository;

import com.lunchteam.lunchrestapi.api.entity.MenuEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuRepository extends JpaRepository<MenuEntity, Long> {

    /**
     * 등록된 메뉴 찾기
     *
     * @param name     menu name
     * @param location location
     * @return bool
     */
    boolean existsByNameAndLocation(String name, String location);
}
