package com.lunchteam.lunchrestapi.api.repository.yp;

import com.lunchteam.lunchrestapi.api.entity.yp.MenuEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("yp.MenuRepository")
public interface MenuRepository extends JpaRepository<MenuEntity, Long> {

}
