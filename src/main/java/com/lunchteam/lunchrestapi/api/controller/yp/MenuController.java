package com.lunchteam.lunchrestapi.api.controller.yp;

import com.lunchteam.lunchrestapi.api.dto.yp.menu.Menu;
import com.lunchteam.lunchrestapi.api.dto.yp.menu.Menu.MenuType;
import com.lunchteam.lunchrestapi.api.dto.yp.menu.MenuRequestDto;
import com.lunchteam.lunchrestapi.api.dto.yp.menu.MenuResponseDto;
import com.lunchteam.lunchrestapi.api.response.BasicResponse;
import com.lunchteam.lunchrestapi.api.response.ErrorResponse;
import com.lunchteam.lunchrestapi.api.service.yp.MenuService;
import com.lunchteam.lunchrestapi.handler.ErrorHandler;
import com.lunchteam.lunchrestapi.handler.ResultHandler;
import java.util.List;
import javax.annotation.Resource;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController("yp.MenuController")
@RequestMapping("/yp/menu")
@RequiredArgsConstructor
public class MenuController {

    @Resource(name = "yp.MenuService")
    private final MenuService menuService;

    @GetMapping("/{type}")
    public ResponseEntity<? extends BasicResponse> getMenuList(
        @PathVariable("type") MenuType type) {
        try {
            MenuRequestDto dto = MenuRequestDto.builder()
                .menuType(type)
                .build();
            List<Menu> menuList = menuService.getAllMenuList(dto);

            return ResultHandler.setResult(menuList, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{type}/{id}")
    public ResponseEntity<? extends BasicResponse> getMenu(
        @PathVariable("type") MenuType type,
        @PathVariable("id") Long id
    ) {
        try {
            log.debug("type: " + type + ", id: " + id);
            MenuRequestDto dto = MenuRequestDto.builder()
                .id(id)
                .menuType(type)
                .build();
            Menu menu = menuService.getMenu(dto);

            return ResultHandler.setResult(MenuResponseDto.of(menu), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/{type}")
    public ResponseEntity<? extends BasicResponse> addMenu(
        @PathVariable("type") MenuType type,
        @Valid @RequestBody MenuRequestDto dto,
        Errors errors
    ) {
        try {
            ResponseEntity<ErrorResponse> errorResponse
                = ErrorHandler.getError(errors, new ErrorResponse());
            if (type.isEmpty() || errorResponse != null) {
                log.warn("[Wrong Request] >> " + dto.toString());
                return errorResponse;
            }

            menuService.addMenu(type, dto);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
