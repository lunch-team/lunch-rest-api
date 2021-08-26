package com.lunchteam.lunchrestapi.api.controller;

import com.lunchteam.lunchrestapi.api.dto.MenuModifyRequestDto;
import com.lunchteam.lunchrestapi.api.dto.MenuRequestDto;
import com.lunchteam.lunchrestapi.api.entity.MenuEntity;
import com.lunchteam.lunchrestapi.api.response.BasicResponse;
import com.lunchteam.lunchrestapi.api.response.CommonResponse;
import com.lunchteam.lunchrestapi.api.response.ErrorResponse;
import com.lunchteam.lunchrestapi.api.response.StatusEnum;
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
        String error;
        ResponseEntity<ErrorResponse> errorResponse
            = ErrorHandler.getError(errors, new ErrorResponse());
        if (errorResponse != null) {
            log.warn("Wrong Request.");
            return errorResponse;
        }

        try {
            error = menuService.addMenu(menuRequestDto);
        } catch (Exception e) {
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(StatusEnum.INTERNAL_SERVER_ERROR));
        }
        return error == null
            ? ResponseEntity.noContent().build()
            : ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(error));
    }

    @PostMapping("/getRandomMenu")
    public ResponseEntity<? extends BasicResponse> getRandomMenu(
        @RequestBody MenuRequestDto menuRequestDto
    ) {
        List<MenuEntity> result;
        try {
            result = menuService.getRandomMenu(menuRequestDto.getRandomNumber());
        } catch (Exception e) {
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(StatusEnum.INTERNAL_SERVER_ERROR));
        }

        return result != null
            ? ResponseEntity.ok(new CommonResponse<>(result))
            : ResponseEntity.internalServerError().build();
    }

    @GetMapping("/getAllMenu")
    public ResponseEntity<? extends BasicResponse> getAllMenu() {
        List<MenuEntity> result;
        try {
            result = menuService.getAllMenu();
        } catch (Exception e) {
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(StatusEnum.INTERNAL_SERVER_ERROR));
        }
        return result != null
            ? ResponseEntity.status(HttpStatus.OK)
            .body(new CommonResponse<>(result))
            : ResponseEntity.notFound().build();
    }

    @PostMapping("/modifyMenu")
    public ResponseEntity<? extends BasicResponse> modifyMenu(
        @Valid @RequestBody MenuModifyRequestDto menuModifyRequestDto,
        Errors errors
    ) {
        String error;
        ResponseEntity<ErrorResponse> errorResponse
            = ErrorHandler.getError(errors, new ErrorResponse());
        if (errorResponse != null) {
            log.warn("Wrong Request.");
            return errorResponse;
        }
        try {
            error = menuService.modifyMenu(menuModifyRequestDto);
        } catch (Exception e) {
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(StatusEnum.INTERNAL_SERVER_ERROR));
        }
        return error == null
            ? ResponseEntity.noContent().build()
            : ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(error));
    }
}
