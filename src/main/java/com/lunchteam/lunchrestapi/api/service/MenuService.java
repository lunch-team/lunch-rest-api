package com.lunchteam.lunchrestapi.api.service;

import com.lunchteam.lunchrestapi.api.dto.OrderEnum;
import com.lunchteam.lunchrestapi.api.dto.file.FileRequestDto;
import com.lunchteam.lunchrestapi.api.dto.file.FileResult;
import com.lunchteam.lunchrestapi.api.dto.menu.MenuModifyRequestDto;
import com.lunchteam.lunchrestapi.api.dto.menu.MenuRequestDto;
import com.lunchteam.lunchrestapi.api.dto.menu.MenuResponseDto;
import com.lunchteam.lunchrestapi.api.dto.menu.MenuResult;
import com.lunchteam.lunchrestapi.api.dto.menu.MenuReviewRequestDto;
import com.lunchteam.lunchrestapi.api.dto.menu.MenuReviewResult;
import com.lunchteam.lunchrestapi.api.dto.menu.MenuTypeRequestDto;
import com.lunchteam.lunchrestapi.api.entity.MenuEntity;
import com.lunchteam.lunchrestapi.api.entity.MenuLogEntity;
import com.lunchteam.lunchrestapi.api.entity.MenuReviewEntity;
import com.lunchteam.lunchrestapi.api.exception.MenuException;
import com.lunchteam.lunchrestapi.api.mapper.MenuMapper;
import com.lunchteam.lunchrestapi.api.repository.FileRepositorySupport;
import com.lunchteam.lunchrestapi.api.repository.MenuLogRepository;
import com.lunchteam.lunchrestapi.api.repository.MenuRepository;
import com.lunchteam.lunchrestapi.api.repository.MenuRepositorySupport;
import com.lunchteam.lunchrestapi.api.repository.MenuReviewRepository;
import com.lunchteam.lunchrestapi.api.repository.MenuReviewRepositorySupport;
import com.lunchteam.lunchrestapi.api.repository.MenuTypeRepository;
import com.lunchteam.lunchrestapi.api.response.StatusEnum;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private final MenuReviewRepository menuReviewRepository;
    private final MenuReviewRepositorySupport menuReviewRepositorySupport;
    private final FileRepositorySupport fileRepositorySupport;
    private final MenuMapper menuMapper;

    /**
     * 메뉴 추가
     *
     * @param menuRequestDto location, name, menuType
     * @return StatusEnum 200
     */
    @Transactional
    public StatusEnum addMenu(MenuRequestDto menuRequestDto) {
        if (menuRepository.existsByNameAndLocation(
            menuRequestDto.getName(),
            menuRequestDto.getLocation()
        )) {
            return StatusEnum.EXISTS_MENU;
        }
        MenuEntity menu = menuRequestDto.toMenu();
        MenuResponseDto.ofMenuEntity(menuRepository.save(menu));
        return StatusEnum.SUCCESS;
    }

    /**
     * 랜덤 메뉴 조회
     *
     * @param menuRequestDto randomNumber, menuType(include 'all')
     * @return List
     */
    @Transactional
    public List<MenuResult> getRandomMenu(MenuRequestDto menuRequestDto) {
        Map<String, Object> params = new HashMap<>();
        params.put("number", menuRequestDto.getRandomNumber());
        params.put("menuType", menuRequestDto.getMenuType());

        return menuMapper.getRandomMenu(params);
    }

    /**
     * 모든 메뉴 조회
     *
     * @param menuRequestDto menuType, order
     * @return List
     */
    @Transactional
    public List<MenuResult> getAllMenu(MenuRequestDto menuRequestDto) {
        return menuRepositorySupport.getAllMenu(menuRequestDto);
    }

    /**
     * 메뉴 수정
     *
     * @param menuModifyRequestDto id, name, location, menuType
     * @return StatusEnum 200
     */
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

    /**
     * 메뉴 삭제
     *
     * @param menuModifyRequestDto id
     * @return StatusEnum
     */
    @Transactional
    public StatusEnum deleteMenu(MenuModifyRequestDto menuModifyRequestDto) {
        Long id = menuModifyRequestDto.getId();
        if (id == null) {
            log.warn("No Id");
            return StatusEnum.BAD_REQUEST;
        }
        if (!menuRepository.existsByIdAndUseYn(id, "Y")) {
            return StatusEnum.NO_MENU;
        }
        long result = menuRepositorySupport.deleteMenuById(id);
        log.info("deleteMenuById result: " + result);
        return result > 0 ? StatusEnum.SUCCESS : StatusEnum.NO_MENU;
    }

    /**
     * 메뉴 방문 추가
     *
     * @param menuModifyRequestDto id
     * @return Menu
     */
    @Transactional
    public MenuEntity visitMenu(MenuModifyRequestDto menuModifyRequestDto) {
        if (!menuRepository.existsByIdAndUseYn(menuModifyRequestDto.getId(), "Y")) {
            return null;
        }
        MenuLogEntity result = menuLogRepository.save(menuModifyRequestDto.toMenuLog());
        return menuRepository.findById(result.getMenuId()).orElse(null);
    }

    /**
     * 메뉴 타입 추가
     *
     * @param menuTypeRequestDto menuName, menuType
     * @return StatusEnum 200
     */
    @Transactional
    public StatusEnum addMenuType(MenuTypeRequestDto menuTypeRequestDto) {
        if (menuRepositorySupport.existsByMenuType(menuTypeRequestDto.getMenuType())) {
            return StatusEnum.EXISTS_MENU_TYPE;
        }
        menuTypeRepository.save(menuTypeRequestDto.toMenuTypeEntity());
        return StatusEnum.SUCCESS;
    }

    /**
     * 메뉴 타입 조회
     *
     * @return List
     */
    @Transactional
    public List<MenuResult> getMenuType() {
        return menuRepositorySupport.getMenuType();
    }

    /**
     * 방문 로그 조회
     *
     * @param menuRequestDto order(ASC, DESC)
     * @return List
     */
    @Transactional
    public List<MenuResult> getVisitMenuList(MenuRequestDto menuRequestDto) {
        if (menuRequestDto.getOrder() == OrderEnum.ASC) {
            log.info("order: " + menuRequestDto.getOrder());
        }
        return menuRepositorySupport.getVisitMenuList(menuRequestDto);
    }

    /**
     * 방문 로그 삭제
     *
     * @param menuModifyRequestDto id
     * @return StatusEnum
     */
    @Transactional
    public StatusEnum deleteMenuLog(MenuModifyRequestDto menuModifyRequestDto) {
        Long id = menuModifyRequestDto.getId();
        if (id == null) {
            log.warn("No Id");
            return StatusEnum.BAD_REQUEST;
        }
        if (!menuLogRepository.existsById(id)) {
            return StatusEnum.NO_MENU;
        }
        long result = menuRepositorySupport.deleteMenuLogById(id);
        log.info("deleteMenuLogById result: " + result);
        return result > 0 ? StatusEnum.SUCCESS : StatusEnum.NO_MENU;
    }

    /**
     * 방문 로그 삭제
     *
     * @param menuModifyRequestDto id
     * @return 200
     */
    @Transactional
    public StatusEnum updateMenuLog(MenuModifyRequestDto menuModifyRequestDto) {
        if (!menuLogRepository.existsById(menuModifyRequestDto.getId())) {
            return StatusEnum.NO_MENU;
        }
        MenuLogEntity menuLog = MenuLogEntity.UpdateMenuLog()
            .id(menuModifyRequestDto.getId())
            .insertDateTime(menuModifyRequestDto.getInsertDateTime())
            .build();
        long result = menuRepositorySupport.updateMenuLogById(menuLog);
        log.info("updateMenuLogById result: " + result);
        return result > 0 ? StatusEnum.SUCCESS : StatusEnum.NO_MENU;
    }

    /**
     * 리뷰 등록
     *
     * @param menuDto contents, menuId, memberId, star
     * @return 200
     */
    @Transactional
    public StatusEnum registerReview(MenuReviewRequestDto menuDto) {
        if (!menuRepository.existsById(menuDto.getMenuId())) {
            return StatusEnum.NO_MENU;
        } else if (menuDto.getStar() > 10 || menuDto.getStar() < 1) {
            log.warn("Invalid Star Range.");
            return StatusEnum.BAD_REQUEST;
        }

        MenuReviewEntity menuReview = MenuReviewEntity.RegisterReview()
            .menuId(menuDto.getMenuId())
            .insertMemberId(menuDto.getMemberId())
            .fileId(menuDto.getFileId())
            .star(menuDto.getStar())
            .contents(menuDto.getContents())
            .build();
        MenuReviewEntity reviewResult = menuReviewRepository.save(menuReview);
        log.info("registerReview result: " + reviewResult.getId());
        return reviewResult.getId() > 0 ? StatusEnum.SUCCESS : StatusEnum.BAD_REQUEST;
    }

    /**
     * 리뷰 조회
     *
     * @param menuDto menuId
     * @return list
     */
    @Transactional
    public List<MenuReviewResult> getReviewList(MenuReviewRequestDto menuDto) {
        if (!menuRepository.existsById(menuDto.getMenuId())) {
            throw new MenuException(menuDto.getMenuId());
        }
        List<MenuReviewResult> reviewResults = menuReviewRepositorySupport.getReviewList(menuDto);
        FileRequestDto fileDto = new FileRequestDto();
        fileDto.setTargetId(menuDto.getMenuId());
        List<FileResult> fileResults = fileRepositorySupport.getFileList(fileDto);

        log.info(reviewResults.toString());
        log.info(fileResults.toString());

        for (MenuReviewResult menuReviewResult : reviewResults) {
            Long fileId = menuReviewResult.getFileId();
            if (fileId != null) {
                List<FileResult> tmpFileList = new ArrayList<>();
                for (FileResult file : fileResults) {
                    if (fileId.equals(file.getGroupId())) {
                        tmpFileList.add(file);
                    }
                }
                if (!tmpFileList.isEmpty()) {
                    log.debug("set file");
                    menuReviewResult.setFiles(tmpFileList);
                    log.debug(reviewResults.toString());
                }
            }
        }
        return reviewResults;
    }

    /**
     * 리뷰 삭제
     *
     * @param menuDto id
     * @return 200
     */
    @Transactional
    public StatusEnum removeReview(MenuReviewRequestDto menuDto) {
        if (!menuReviewRepository.existsById(menuDto.getId())) {
            return StatusEnum.NO_MENU;
        }
        long result = menuReviewRepositorySupport.deleteMenuReview(menuDto);
        log.info("deleteMenuReview result: " + result);
        return result > 0 ? StatusEnum.SUCCESS : StatusEnum.NO_MENU;
    }

    @Transactional
    public MenuResult getMenuDetail(MenuRequestDto menuDto) {
        if (!menuRepository.existsById(menuDto.getId())) {
            throw new MenuException(StatusEnum.NO_MENU);
        }
        return menuRepositorySupport.getMenuDetail(menuDto);
    }
}
