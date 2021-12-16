package com.lunchteam.lunchrestapi.api.controller;

import com.lunchteam.lunchrestapi.api.response.BasicResponse;
import com.lunchteam.lunchrestapi.api.service.MenuService;
import com.lunchteam.lunchrestapi.handler.ResultHandler;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/stat")
@RequiredArgsConstructor
public class StatisticsController {

    private final MenuService menuService;


    /**
     * 메뉴 타입 별 방문수 통계
     *
     * @return List
     */
    @GetMapping("/getVisitCountByMenuType")
    public ResponseEntity<? extends BasicResponse> getVisitCountGroupByMenuName() {
        try {
            List<Object> result = menuService.getVisitCountGroupByMenuName();
            return ResultHandler.setResult(result, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 요일 별 메뉴 타입 선호도 통계
     *
     * @return List
     */
    @GetMapping("/getVisitCountByDayOfWeek")
    public ResponseEntity<? extends BasicResponse> getVisitCountGroupByDayOfWeek() {
        try {
            Map<String, Object> result = menuService.getVisitCountGroupByDayOfWeek();
            return ResultHandler.setResult(result, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 요일 별 메뉴 선호도 통계
     *
     * @return List
     */
    @GetMapping("/getMenuListByDayOfWeek")
    public ResponseEntity<? extends BasicResponse> getMenuListByDayOfWeek() {
        try {
            Map<String, Object> result = menuService.getMenuListByDayOfWeek();
            return ResultHandler.setResult(result, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
