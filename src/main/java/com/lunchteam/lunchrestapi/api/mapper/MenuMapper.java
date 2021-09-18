package com.lunchteam.lunchrestapi.api.mapper;

import com.lunchteam.lunchrestapi.api.dto.menu.MenuResult;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface MenuMapper {

    List<MenuResult> getRandomMenu(Map<String, Object> params);
}
