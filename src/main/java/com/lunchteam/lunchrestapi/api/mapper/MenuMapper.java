package com.lunchteam.lunchrestapi.api.mapper;

import com.lunchteam.lunchrestapi.api.dto.menu.MenuResult;
import java.util.HashMap;
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


    /**
     * 메뉴 타입 별 방문수 통계
     *
     * @return List
     */
    List<Object> getVisitCountGroupByMenuName(Map<String, Object> params);


    /**
     * 요일별 메뉴 타입 선호도 통계
     *
     * @return List
     */
    List<Object> getVisitCountGroupByDayOfWeek(HashMap<String, Object> params);

    /**
     * 요일별 메뉴 선호도
     *
     * @return List
     */
    List<Object> getMenuListByDayOfWeek(HashMap<String, Object> params);
}
