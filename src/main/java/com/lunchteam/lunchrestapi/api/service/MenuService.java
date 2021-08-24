package com.lunchteam.lunchrestapi.api.service;

import com.lunchteam.lunchrestapi.api.dto.MenuRequestDto;
import com.lunchteam.lunchrestapi.api.dto.MenuResponseDto;
import com.lunchteam.lunchrestapi.api.entity.MenuEntity;
import com.lunchteam.lunchrestapi.api.repository.MenuRepository;
import com.lunchteam.lunchrestapi.api.repository.MenuRepositorySupport;
import java.util.List;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
    public String getRandomMenu() {
        menuRepositorySupport.getRandomMenu(1);
        return null;
    }

    @Transactional
    public List<MenuEntity> getAllMenu() {
        return menuRepository.findAll();
    }
}
