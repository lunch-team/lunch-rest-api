package com.lunchteam.lunchrestapi.api.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
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

    @PostMapping("/postTest")
    public ResponseEntity<HashMap<String, Object>> login(
        @RequestBody HashMap<String, Object> params) {
        HashMap<String, Object> resultMap = new HashMap<>();
        log.debug("Call Post Test, Params: " + params.toString());
        resultMap.put("param1", params.get("param1"));
        resultMap.put("param2", params.get("param2"));
        return ResponseEntity.ok(resultMap);
    }
}
