package com.lunchteam.lunchrestapi.api.service.yp;

import com.lunchteam.lunchrestapi.api.dto.yp.menu.Menu;
import com.lunchteam.lunchrestapi.api.dto.yp.menu.Menu.MenuType;
import com.lunchteam.lunchrestapi.api.dto.yp.menu.MenuRequestDto;
import com.lunchteam.lunchrestapi.api.entity.yp.MenuEntity;
import com.lunchteam.lunchrestapi.api.repository.yp.MenuRepository;
import com.lunchteam.lunchrestapi.api.repository.yp.MenuRepositorySupport;
import java.util.List;
import javax.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service("yp.MenuService")
@RequiredArgsConstructor
public class MenuService {

    @Resource(name = "yp.MenuRepositorySupport")
    private final MenuRepositorySupport menuRepositorySupport;

    @Resource(name = "yp.MenuRepository")
    private final MenuRepository menuRepository;

    public List<Menu> getAllMenuList(MenuRequestDto dto) {
        return menuRepositorySupport.selectAllMenuListByType(dto);
    }

    public Menu getMenu(MenuRequestDto dto) {
        return menuRepositorySupport.selectMenuByTypeAndId(dto);
    }

    public void addMenu(MenuType menuType, MenuRequestDto dto) {
        MenuEntity menu = dto.toMenuByMenuType(menuType);
        menuRepository.save(menu);
    }
}
