package com.lunchteam.lunchrestapi.api.controller;

import com.lunchteam.lunchrestapi.api.dto.MenuModifyRequestDto;
import com.lunchteam.lunchrestapi.api.dto.MenuRequestDto;
import com.lunchteam.lunchrestapi.api.dto.MenuTypeRequestDto;
import com.lunchteam.lunchrestapi.api.entity.MenuEntity;
import com.lunchteam.lunchrestapi.api.entity.MenuTypeEntity;
import com.lunchteam.lunchrestapi.api.response.BasicResponse;
import com.lunchteam.lunchrestapi.api.response.CommonResponse;
import com.lunchteam.lunchrestapi.api.response.ErrorResponse;
import com.lunchteam.lunchrestapi.api.service.MenuService;
import com.lunchteam.lunchrestapi.handler.ErrorHandler;
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
            String error = menuService.addMenu(menuRequestDto);
            return error == null
                ? ResponseEntity.noContent().build()
                : ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(error));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/getRandomMenu")
    public ResponseEntity<? extends BasicResponse> getRandomMenu(
        @RequestBody MenuRequestDto menuRequestDto
    ) {
        try {
            List<MenuEntity> result = menuService.getRandomMenu(menuRequestDto.getRandomNumber());
            return result != null
                ? ResponseEntity.ok(new CommonResponse<>(result))
                : ResponseEntity.internalServerError().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }

    }

    @GetMapping("/getAllMenu")
    public ResponseEntity<? extends BasicResponse> getAllMenu() {
        try {
            List<MenuEntity> result = menuService.getAllMenu();
            return result != null
                ? ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse<>(result))
                : ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

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

            String error = menuService.modifyMenu(menuModifyRequestDto);
            return error == null
                ? ResponseEntity.noContent().build()
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(error));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/deleteMenu")
    public ResponseEntity<? extends BasicResponse> deleteMenu(
        @RequestBody MenuModifyRequestDto menuModifyRequestDto) {

        try {
            if (menuModifyRequestDto.getId() == null) {
                log.warn("No Id");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }

            String error = menuService.deleteMenu(menuModifyRequestDto.getId());
            return error == null
                ? ResponseEntity.noContent().build()
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(error));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/visitMenu")
    public ResponseEntity<? extends BasicResponse> visitMenu(
        @RequestBody MenuModifyRequestDto menuModifyRequestDto) {
        try {
            if (menuModifyRequestDto.getId() == null) {
                log.warn("No Id");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            MenuEntity menu = menuService.visitMenu(menuModifyRequestDto);
            return menu != null
                ? ResponseEntity.ok(new CommonResponse<>(menu))
                : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("getVisitMenuList")
    public ResponseEntity<? extends BasicResponse> getVisitMenuList(
        @RequestBody MenuModifyRequestDto menuModifyRequestDto
    ) {
        try {
            // TODO
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("addMenuType")
    public ResponseEntity<? extends BasicResponse> addMenuType(
        @RequestBody MenuTypeRequestDto menuTypeRequestDto
    ) {
        try {
            String error = menuService.addMenuType(menuTypeRequestDto);
            return error == null
                ? ResponseEntity.noContent().build()
                : ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(error));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("getMenuType")
    public ResponseEntity<? extends BasicResponse> getMenuType() {
        try {
            List<MenuTypeEntity> result = menuService.getMenuType();
            return result != null
                ? ResponseEntity.ok(new CommonResponse<>(result))
                : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

}
