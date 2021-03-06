package com.lunchteam.lunchrestapi.handler;

import com.lunchteam.lunchrestapi.api.dto.BasicResponseDto;
import com.lunchteam.lunchrestapi.api.response.BasicResponse;
import com.lunchteam.lunchrestapi.api.response.CommonResponse;
import com.lunchteam.lunchrestapi.api.response.ErrorResponse;
import com.lunchteam.lunchrestapi.api.response.StatusEnum;
import java.util.HashMap;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Slf4j
public class ResultHandler {

    public static ResponseEntity<? extends BasicResponse> setResult(
        Object result, HttpStatus errorStatus
    ) {
        if (result instanceof StatusEnum) {
            // result is error message
            log.debug("[RETURN]: Status Enum");
            return result == StatusEnum.SUCCESS
                ? ResponseEntity.ok().build()
                : ResponseEntity.status(errorStatus)
                    .body(new ErrorResponse(((StatusEnum) result).get()));
        } else if (
            result instanceof List
                || result instanceof BasicResponseDto
                || result instanceof HashMap
        ) {
            // result is Class
            log.debug("[RETURN]: ok, Class: " + result.getClass().getSimpleName());
            return ResponseEntity.ok(new CommonResponse<>(result));
        } else if (result == null) {
            // result is null
            log.debug("[RETURN]: just Error Status");
            return ResponseEntity.status(errorStatus).build();
        } else {
            log.warn("No kind of result: " + result.getClass().getName());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
