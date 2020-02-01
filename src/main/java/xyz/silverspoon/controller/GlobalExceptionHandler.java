package xyz.silverspoon.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import xyz.silverspoon.component.ImCommonResult;

import javax.servlet.http.HttpServletRequest;

@RestController
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public ImCommonResult<String> defaultErrorHandler(HttpServletRequest request, Exception e) {
        return ImCommonResult.error(500, e.getMessage());
    }
}
