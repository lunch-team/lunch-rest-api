package com.lunchteam.lunchrestapi.api.service;

import com.lunchteam.lunchrestapi.api.dto.DtoEnum;
import com.lunchteam.lunchrestapi.api.dto.MenuModifyRequestDto;
import com.lunchteam.lunchrestapi.api.dto.MenuRequestDto;
import com.lunchteam.lunchrestapi.api.dto.MenuResponseDto;
import com.lunchteam.lunchrestapi.api.dto.MenuTypeRequestDto;
import com.lunchteam.lunchrestapi.api.dto.MenuTypeResponseDto;
import com.lunchteam.lunchrestapi.api.entity.MenuEntity;
import com.lunchteam.lunchrestapi.api.entity.MenuLogEntity;
import com.lunchteam.lunchrestapi.api.entity.MenuTypeEntity;
import com.lunchteam.lunchrestapi.api.repository.MenuLogRepository;
import com.lunchteam.lunchrestapi.api.repository.MenuRepository;
import com.lunchteam.lunchrestapi.api.repository.MenuRepositorySupport;
import com.lunchteam.lunchrestapi.api.repository.MenuTypeRepository;
import com.lunchteam.lunchrestapi.api.response.StatusEnum;
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
    private final MenuTypeRepository menuTypeRepository;
    private final MenuLogRepository menuLogRepository;
    private final MenuRepositorySupport menuRepositorySupport;

    @Transactional
    public StatusEnum addMenu(MenuRequestDto menuRequestDto) {
        if (menuRepository.existsByNameAndLocation(
            menuRequestDto.getName(),
            menuRequestDto.getLocation()
        )) {
            return StatusEnum.EXISTS_MENU;
        }
        MenuEntity menu = menuRequestDto.toMenu();
        MenuResponseDto.of(menuRepository.save(menu));
        return StatusEnum.SUCCESS;
    }

    @Transactional
    public List<MenuEntity> getRandomMenu(int count) {
        return menuRepositorySupport.getRandomMenu(count);
    }

    @Transactional
    public List<MenuEntity> getAllMenu() {
        return menuRepositorySupport.getAllMenu();
    }

    @Transactional
    public StatusEnum modifyMenu(MenuModifyRequestDto menuModifyRequestDto) {
        if (!menuRepository.existsByIdAndUseYn(menuModifyRequestDto.getId(), "Y")) {
            return StatusEnum.NO_MENU;
        }
        MenuEntity menu = MenuEntity.ModifyMenu()
            .id(menuModifyRequestDto.getId())
            .location(menuModifyRequestDto.getLocation())
            .name(menuModifyRequestDto.getName())
            .menuType(menuModifyRequestDto.getMenuType())
            .build();
        long result = menuRepositorySupport.modifyMenuById(menu);
        log.info("modifyMenuById result: " + result);
        return result > 0 ? StatusEnum.SUCCESS : StatusEnum.NO_MENU;
    }

    @Transactional
    public StatusEnum deleteMenu(Long id) {
        if (!menuRepository.existsByIdAndUseYn(id, "Y")) {
            return StatusEnum.NO_MENU;
        }
        long result = menuRepositorySupport.deleteMenuById(id);
        log.info("deleteMenuById result: " + result);
        return result > 0 ? StatusEnum.SUCCESS : StatusEnum.NO_MENU;
    }

    @Transactional
    public MenuEntity visitMenu(MenuModifyRequestDto menuModifyRequestDto) {
        if (!menuRepository.existsByIdAndUseYn(menuModifyRequestDto.getId(), "Y")) {
            return null;
        }
        long updateResult = menuRepositorySupport.addVisitCountById(menuModifyRequestDto.getId());
        log.info("visit menu result: " + updateResult);
        if (updateResult > 0) {
            MenuLogEntity result = menuLogRepository.save(menuModifyRequestDto.toMenuLog());
            return menuRepository.findById(result.getMenuId()).orElse(null);
        } else {
            return null;
        }
    }

    @Transactional
    public StatusEnum addMenuType(MenuTypeRequestDto menuTypeRequestDto) {
        if (menuRepositorySupport.existsByMenuType(menuTypeRequestDto.getMenuType())) {
            return StatusEnum.EXISTS_MENU_TYPE;
        }
        MenuTypeResponseDto.of(menuTypeRepository.save(menuTypeRequestDto.toMenuTypeEntity()));
        return StatusEnum.SUCCESS;
    }

    @Transactional
    public List<MenuTypeEntity> getMenuType() {
        return menuRepositorySupport.getMenuType();
    }

    @Transactional
    public List<MenuEntity> getVisitMenuList(MenuRequestDto menuRequestDto) {
        if (menuRequestDto.getOrder() == DtoEnum.ASC) {
            log.info("order: " + menuRequestDto.getOrder());
        }
        return menuRepositorySupport.getVisitMenuList(menuRequestDto);
    }
}
