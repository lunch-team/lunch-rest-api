package com.lunchteam.lunchrestapi.api.controller;

import com.lunchteam.lunchrestapi.api.dto.MenuLogResponseDto;
import com.lunchteam.lunchrestapi.api.dto.MenuModifyRequestDto;
import com.lunchteam.lunchrestapi.api.dto.MenuRequestDto;
import com.lunchteam.lunchrestapi.api.dto.MenuResponseDto;
import com.lunchteam.lunchrestapi.api.dto.MenuTypeRequestDto;
import com.lunchteam.lunchrestapi.api.dto.MenuTypeResponseDto;
import com.lunchteam.lunchrestapi.api.response.BasicResponse;
import com.lunchteam.lunchrestapi.api.response.ErrorResponse;
import com.lunchteam.lunchrestapi.api.response.StatusEnum;
import com.lunchteam.lunchrestapi.api.service.MenuService;
import com.lunchteam.lunchrestapi.handler.ErrorHandler;
import com.lunchteam.lunchrestapi.handler.ResultHandler;
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
     * @return 204
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
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * 랜덤 메뉴 가져오기
     *
     * @param menuRequestDto randomNumber
     * @return List
     */
    @PostMapping("/getRandomMenu")
    public ResponseEntity<? extends BasicResponse> getRandomMenu(
        @RequestBody MenuRequestDto menuRequestDto
    ) {
        try {
            List<MenuResponseDto> result
                = MenuResponseDto.listOf(
                menuService.getRandomMenu(menuRequestDto.getRandomNumber()));
            return ResultHandler.setResult(result, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }

    }

    /**
     * 모든 메뉴 가져오기
     *
     * @return List
     */
    @GetMapping("/getAllMenu")
    public ResponseEntity<? extends BasicResponse> getAllMenu() {
        try {
            List<MenuResponseDto> result =
                MenuResponseDto.listOf(menuService.getAllMenu());
            return ResultHandler.setResult(result, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * 메뉴 수정
     *
     * @param menuModifyRequestDto id, name, location, menuType
     * @param errors               Valid
     * @return 204
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
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * 메뉴 삭제
     *
     * @param menuModifyRequestDto id
     * @return 204
     */
    @PostMapping("/deleteMenu")
    public ResponseEntity<? extends BasicResponse> deleteMenu(
        @RequestBody MenuModifyRequestDto menuModifyRequestDto) {

        try {
            if (menuModifyRequestDto.getId() == null) {
                log.warn("No Id");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }

            StatusEnum result = menuService.deleteMenu(menuModifyRequestDto.getId());
            return ResultHandler.setResult(result, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
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
                MenuResponseDto.of(menuService.visitMenu(menuModifyRequestDto)),
                HttpStatus.NOT_FOUND
            );
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * 방문한 메뉴 리스트 가져오기
     *
     * @param menuRequestDto order(ASC, DESC)
     * @return List
     */
    @PostMapping("/getVisitMenuList")
    public ResponseEntity<? extends BasicResponse> getVisitMenuList(
        @RequestBody MenuRequestDto menuRequestDto
    ) {
        try {
            List<MenuLogResponseDto> result
                = MenuLogResponseDto.listOf(menuService.getVisitMenuList(menuRequestDto));
            return ResultHandler.setResult(result, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * 메뉴 타입 추가
     *
     * @param menuTypeRequestDto menuName, menuType
     * @return 204
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
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * 메뉴 타입 가져오기
     *
     * @return List
     */
    @GetMapping("/getMenuType")
    public ResponseEntity<? extends BasicResponse> getMenuType() {
        try {
            List<MenuTypeResponseDto> result
                = MenuTypeResponseDto.listOf(menuService.getMenuType());
            return ResultHandler.setResult(result, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

}
