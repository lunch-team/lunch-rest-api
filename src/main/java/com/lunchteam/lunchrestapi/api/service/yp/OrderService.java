package com.lunchteam.lunchrestapi.api.service.yp;

import com.lunchteam.lunchrestapi.api.dto.yp.menu.Menu;
import com.lunchteam.lunchrestapi.api.dto.yp.menu.MenuRequestDto;
import com.lunchteam.lunchrestapi.api.repository.yp.MenuRepositorySupport;
import java.util.List;
import javax.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service("yp.OrderService")
@RequiredArgsConstructor
public class OrderService {

    @Resource(name = "yp.MenuRepositorySupport")
    private final MenuRepositorySupport menuRepositorySupport;

    public List<Menu> getAllOrderList(MenuRequestDto dto) {
        return menuRepositorySupport.selectAllMenuListByType(dto);
    }

    public Menu getMenu(MenuRequestDto dto) {
        return menuRepositorySupport.selectMenuByTypeAndId(dto);
    }
}
