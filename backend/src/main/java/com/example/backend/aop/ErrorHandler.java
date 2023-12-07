package com.example.backend.aop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.example.backend.exception.CustomArgumentNotValidException;
import com.example.backend.exception.LogicRuntimeException;

@ControllerAdvice
public class ErrorHandler extends ResponseEntityExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(ErrorHandler.class);

    // 統一處理 Exception並回傳INTERNAL_SERVER_ERROR
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorRes> handleConflict(Exception ex, WebRequest request) {
        logger.error("ErrorHandler catch:", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorRes("Server Error", null));
    }

    // 統一處理 CustomArgumentNotValidException 將錯誤欄位封裝回傳BAD_REQUEST
    @ExceptionHandler(CustomArgumentNotValidException.class)
    protected ResponseEntity<ErrorRes> handleCustomArgumentNotValid(CustomArgumentNotValidException ex,
            WebRequest request) {
        Map<String, List<String>> map = getFieldErrors(ex.getBindingResult());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorRes("Argument Not Valid", map));
    }

    // 統一處理 LogicRuntimeException 將訊息回傳至前端 UNPROCESSABLE_ENTITY (422)
    @ExceptionHandler(LogicRuntimeException.class)
    protected ResponseEntity<ErrorRes> handleLogicRuntimeException(LogicRuntimeException ex, WebRequest request) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new ErrorRes(ex.getMessage(), null));
    }

    // 統一處理 @Valid 錯誤，controller
    // 方法不要寫BindingResult，不然MethodArgumentNotValidException會被catch
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Map<String, List<String>> map = getFieldErrors(ex.getBindingResult());
        return handleExceptionInternal(ex, new ErrorRes("Argument Not Valid", map), headers, HttpStatus.BAD_REQUEST,
                request);
    }

    // 取得錯誤欄位及訊息
    private Map<String, List<String>> getFieldErrors(BindingResult result) {
        Map<String, List<String>> map = new HashMap<>();
        for (FieldError fieldError : result.getFieldErrors()) {
            map.computeIfAbsent(fieldError.getField(), k -> new ArrayList<>()).add(fieldError.getDefaultMessage());
        }
        return map;
    }

    private static record ErrorRes(String message, Map<String, List<String>> fieldErrors) {
    }

}
