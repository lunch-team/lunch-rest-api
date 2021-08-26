package com.lunchteam.lunchrestapi.handler;

import com.lunchteam.lunchrestapi.api.response.ErrorResponse;
import java.util.stream.Collectors;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;

public class ErrorHandler {

    public static ResponseEntity<ErrorResponse> getError(
        Errors errors, ErrorResponse result
    ) {
        if (errors.hasErrors()) {
            result.setErrorMessage(errors.getAllErrors().stream().map(
                    DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(",")));
            return ResponseEntity.badRequest().body(result);
        }
        return null;
    }
}
