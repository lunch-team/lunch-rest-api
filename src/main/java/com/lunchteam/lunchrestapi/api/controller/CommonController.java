package com.lunchteam.lunchrestapi.api.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(name = "/api")
public class CommonController {

    @GetMapping("/connectionTest")
    public ResponseEntity<?> getMyMemberInfo() {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("data", "data001");

        List<Map<String, Object>> resultList = new ArrayList<>();
        resultList.add(resultMap);
        resultList.add(resultMap);

        return ResponseEntity.ok(resultList);
    }
}
