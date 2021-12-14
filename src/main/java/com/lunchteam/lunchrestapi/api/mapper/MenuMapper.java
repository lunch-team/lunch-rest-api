package com.lunchteam.lunchrestapi.api.mapper;

import com.lunchteam.lunchrestapi.api.dto.menu.MenuResult;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface MenuMapper {

    /**
     * 랜덤 메뉴 조회
     *
     * @param params randomNumber, menuType(include 'all')
     * @return List
     */
    List<MenuResult> getRandomMenu(Map<String, Object> params);

    List<MenuResult> getVisitCountGroupByMenuName(Map<String, Object> params);
}
