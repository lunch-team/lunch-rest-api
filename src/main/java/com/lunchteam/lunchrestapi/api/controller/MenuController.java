package com.lunchteam.lunchrestapi.api.controller;

import com.lunchteam.lunchrestapi.api.dto.menu.MenuModifyRequestDto;
import com.lunchteam.lunchrestapi.api.dto.menu.MenuRequestDto;
import com.lunchteam.lunchrestapi.api.dto.menu.MenuResponseDto;
import com.lunchteam.lunchrestapi.api.dto.menu.MenuResult;
import com.lunchteam.lunchrestapi.api.dto.menu.MenuReviewRequestDto;
import com.lunchteam.lunchrestapi.api.dto.menu.MenuReviewResult;
import com.lunchteam.lunchrestapi.api.dto.menu.MenuTypeRequestDto;
import com.lunchteam.lunchrestapi.api.exception.AuthenticationException;
import com.lunchteam.lunchrestapi.api.exception.MenuException;
import com.lunchteam.lunchrestapi.api.response.BasicResponse;
import com.lunchteam.lunchrestapi.api.response.ErrorResponse;
import com.lunchteam.lunchrestapi.api.response.StatusEnum;
import com.lunchteam.lunchrestapi.api.service.MenuService;
import com.lunchteam.lunchrestapi.handler.ErrorHandler;
import com.lunchteam.lunchrestapi.handler.ResultHandler;
import java.util.HashMap;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/menu")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    /**
     * 메뉴 추가
     *
     * @param menuRequestDto location, name, menuType
     * @param errors         Valid
     * @return 200
     */
    @PostMapping("/addMenu")
    public ResponseEntity<? extends BasicResponse> addMenu(
        @Valid @RequestBody MenuRequestDto menuRequestDto, Errors errors) {

        try {
            ResponseEntity<ErrorResponse> errorResponse
                = ErrorHandler.getError(errors, new ErrorResponse());
            if (errorResponse != null) {
                log.warn("Wrong Request.");
                return errorResponse;
            }
            StatusEnum result = menuService.addMenu(menuRequestDto);
            return ResultHandler.setResult(result, HttpStatus.CONFLICT);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 랜덤 메뉴 조회
     *
     * @param menuRequestDto randomNumber, menuType(include 'all')
     * @return List
     */
    @PostMapping("/getRandomMenu")
    public ResponseEntity<? extends BasicResponse> getRandomMenu(
        @RequestBody MenuRequestDto menuRequestDto
    ) {
        try {
            List<MenuResponseDto> result
                = MenuResponseDto.listOfMenuResult(menuService.getRandomMenu(menuRequestDto));
            return ResultHandler.setResult(result, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    /**
     * 모든 메뉴 조회
     *
     * @param menuRequestDto menuType, order
     * @return List
     */
    @PostMapping("/getAllMenu")
    public ResponseEntity<? extends BasicResponse> getAllMenu(
        @RequestBody MenuRequestDto menuRequestDto
    ) {
        try {
            List<MenuResponseDto> result =
                MenuResponseDto.listOfMenuResult(menuService.getAllMenu(menuRequestDto));
            return ResultHandler.setResult(result, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 메뉴 상세 정보 조회
     *
     * @param menuDto id (menuId)
     * @return Map
     */
    @PostMapping("/getMenuDetail")
    public ResponseEntity<? extends BasicResponse> getMenuDetail(
        @RequestBody MenuRequestDto menuDto
    ) {
        try {
            HashMap<String, Object> result = new HashMap<>();
            MenuResponseDto menuDetail = MenuResponseDto.ofOne(menuService.getMenuDetail(menuDto));

            // review 정보를 조회하기 위한 id 세팅
            MenuReviewRequestDto reviewDto = new MenuReviewRequestDto();
            reviewDto.setMenuId(menuDto.getId());

            List<MenuReviewResult> menuReview = menuService.getReviewList(reviewDto);

            result.put("menuDetail", menuDetail);
            result.put("menuReview", menuReview);
            return ResultHandler.setResult(result, HttpStatus.NOT_FOUND);
        } catch (MenuException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse((e).getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 메뉴 수정
     *
     * @param menuModifyRequestDto id, name, location, menuType
     * @param errors               Valid
     * @return 200
     */
    @PostMapping("/modifyMenu")
    public ResponseEntity<? extends BasicResponse> modifyMenu(
        @Valid @RequestBody MenuModifyRequestDto menuModifyRequestDto,
        Errors errors
    ) {
        try {
            ResponseEntity<ErrorResponse> errorResponse
                = ErrorHandler.getError(errors, new ErrorResponse());
            if (errorResponse != null) {
                log.warn("Wrong Request.");
                return errorResponse;
            }

            StatusEnum result = menuService.modifyMenu(menuModifyRequestDto);
            return ResultHandler.setResult(result, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 메뉴 삭제
     *
     * @param menuModifyRequestDto id
     * @return 200
     */
    @PostMapping("/deleteMenu")
    public ResponseEntity<? extends BasicResponse> deleteMenu(
        @RequestBody MenuModifyRequestDto menuModifyRequestDto) {

        try {
            StatusEnum result = menuService.deleteMenu(menuModifyRequestDto);
            return ResultHandler.setResult(result, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 메뉴 방문 추가
     *
     * @param menuModifyRequestDto id
     * @return Menu
     */
    @PostMapping("/visitMenu")
    public ResponseEntity<? extends BasicResponse> visitMenu(
        @RequestBody MenuModifyRequestDto menuModifyRequestDto) {
        try {
            if (menuModifyRequestDto.getId() == null) {
                log.warn("No Id");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            return ResultHandler.setResult(
                MenuResponseDto.ofMenuEntity(menuService.visitMenu(menuModifyRequestDto)),
                HttpStatus.NOT_FOUND
            );
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 방문한 메뉴 리스트 조회
     *
     * @param menuRequestDto order(ASC, DESC)
     * @return List
     */
    @PostMapping("/getVisitMenuList")
    public ResponseEntity<? extends BasicResponse> getVisitMenuList(
        @RequestBody MenuRequestDto menuRequestDto
    ) {
        try {
            List<MenuResponseDto> result
                = MenuResponseDto.listOfLogResult(menuService.getVisitMenuList(menuRequestDto));
            return ResultHandler.setResult(result, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 메뉴 타입 추가
     *
     * @param menuTypeRequestDto menuName, menuType
     * @return 200
     */
    @PostMapping("/addMenuType")
    public ResponseEntity<? extends BasicResponse> addMenuType(
        @RequestBody MenuTypeRequestDto menuTypeRequestDto
    ) {
        try {
            StatusEnum result = menuService.addMenuType(menuTypeRequestDto);
            return ResultHandler.setResult(result, HttpStatus.CONFLICT);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 메뉴 타입 조회
     *
     * @return List
     */
    @GetMapping("/getMenuType")
    public ResponseEntity<? extends BasicResponse> getMenuType() {
        try {
            List<MenuResponseDto> result
                = MenuResponseDto.listOfMenuTypeResult(menuService.getMenuType());
            return ResultHandler.setResult(result, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 방문 로그 삭제
     *
     * @param menuModifyRequestDto id
     * @return 200
     */
    @PostMapping("/deleteMenuLog")
    public ResponseEntity<? extends BasicResponse> deleteMenuLog(
        @RequestBody MenuModifyRequestDto menuModifyRequestDto
    ) {
        try {
            StatusEnum result = menuService.deleteMenuLog(menuModifyRequestDto);
            return ResultHandler.setResult(result, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 방문 로그 삭제
     *
     * @param menuModifyRequestDto id
     * @return 200
     */
    @PostMapping("/updateMenuLog")
    public ResponseEntity<? extends BasicResponse> updateMenuLog(
        @RequestBody MenuModifyRequestDto menuModifyRequestDto
    ) {
        try {
            StatusEnum result = menuService.updateMenuLog(menuModifyRequestDto);
            return ResultHandler.setResult(result, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 리뷰 등록
     *
     * @param menuDto contents, menuId, memberId, star
     * @return 200
     */
    @PostMapping("/registerReview")
    public ResponseEntity<? extends BasicResponse> registerReview(
        @RequestBody MenuReviewRequestDto menuDto
    ) {
        try {
            StatusEnum result = menuService.registerReview(menuDto);
            return ResultHandler.setResult(result, HttpStatus.NOT_FOUND);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 리뷰 조회
     *
     * @param menuDto menuId or allFlag is true
     * @return list
     */
    @PostMapping("/getReviewList")
    public ResponseEntity<? extends BasicResponse> getReviewList(
        @RequestBody MenuReviewRequestDto menuDto
    ) {
        try {
            List<MenuReviewResult> results = menuService.getReviewList(menuDto);
            return ResultHandler.setResult(results, HttpStatus.NOT_FOUND);
        } catch (MenuException me) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 메뉴 리뷰 삭제
     *
     * @param menuDto id
     * @return 200
     */
    @PostMapping("/removeReview")
    public ResponseEntity<? extends BasicResponse> removeReview(
        @RequestBody MenuReviewRequestDto menuDto
    ) {
        try {
            StatusEnum result = menuService.removeReview(menuDto);
            return ResultHandler.setResult(result, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 메뉴 타입 별 방문수 통계
     *
     * @return List
     */
    @GetMapping("/statistics/getVisitCount")
    public ResponseEntity<? extends BasicResponse> getVisitCountGroupByMenuName() {
        try {
            List<MenuResult> result = menuService.getVisitCountGroupByMenuName();
            return ResultHandler.setResult(result, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
