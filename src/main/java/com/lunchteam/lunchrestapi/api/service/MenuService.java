package com.lunchteam.lunchrestapi.api.service;

import com.lunchteam.lunchrestapi.api.dto.MenuModifyRequestDto;
import com.lunchteam.lunchrestapi.api.dto.MenuRequestDto;
import com.lunchteam.lunchrestapi.api.dto.MenuResponseDto;
import com.lunchteam.lunchrestapi.api.entity.MenuEntity;
import com.lunchteam.lunchrestapi.api.repository.MenuRepository;
import com.lunchteam.lunchrestapi.api.repository.MenuRepositorySupport;
import java.util.List;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuRepositorySupport menuRepositorySupport;

    @Transactional
    public String addMenu(MenuRequestDto menuRequestDto) {
        if (menuRepository.existsByNameAndLocation(
            menuRequestDto.getName(),
            menuRequestDto.getLocation()
        )) {
            return "exist_menu";
        }
        MenuEntity menu = menuRequestDto.toMenu();
        MenuResponseDto.of(menuRepository.save(menu));
        return null;
    }

    @Transactional
    public List<MenuEntity> getRandomMenu(int count) {
        return menuRepositorySupport.getRandomMenu(count);
    }

    @Transactional
    public List<MenuEntity> getAllMenu() {
        return menuRepositorySupport.getAllMenu();
    }

    public String modifyMenu(MenuModifyRequestDto menuModifyRequestDto) {
        if (!menuRepository.existsById(menuModifyRequestDto.getId())) {
            return "no_menu";
        }
        MenuEntity menu = MenuEntity.ModifyMenu()
            .id(menuModifyRequestDto.getId())
            .location(menuModifyRequestDto.getLocation())
            .name(menuModifyRequestDto.getName())
            .menuType(menuModifyRequestDto.getMenuType())
            .build();
        long result = menuRepositorySupport.modifyMenuById(menu);
        log.info("modifyMenuById result: " + result);
        return null;
    }
}
