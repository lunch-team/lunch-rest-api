package com.lunchteam.lunchrestapi.api.controller.yp;

import com.lunchteam.lunchrestapi.api.dto.yp.menu.Menu;
import com.lunchteam.lunchrestapi.api.dto.yp.menu.Menu.MenuType;
import com.lunchteam.lunchrestapi.api.dto.yp.menu.MenuRequestDto;
import com.lunchteam.lunchrestapi.api.dto.yp.menu.MenuResponseDto;
import com.lunchteam.lunchrestapi.api.response.BasicResponse;
import com.lunchteam.lunchrestapi.api.service.yp.OrderService;
import com.lunchteam.lunchrestapi.handler.ResultHandler;
import java.util.List;
import javax.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController("yp.OrderController")
@RequestMapping("/yp/order")
@RequiredArgsConstructor
public class OrderController {

    @Resource(name = "yp.OrderService")
    private final OrderService orderService;

    @GetMapping("/")
    public ResponseEntity<? extends BasicResponse> getOrder() {
        try {
            MenuRequestDto dto = MenuRequestDto.builder()
                .build();
            List<Menu> orderList = orderService.getAllOrderList(dto);

            return ResultHandler.setResult(orderList, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<? extends BasicResponse> getOrderDetail(@PathVariable("id") Long id) {
        try {
            log.debug("id: " + id);
            MenuRequestDto dto = MenuRequestDto.builder()
                .id(id)
                .build();
            Menu menu = orderService.getMenu(dto);

            return ResultHandler.setResult(MenuResponseDto.of(menu), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
