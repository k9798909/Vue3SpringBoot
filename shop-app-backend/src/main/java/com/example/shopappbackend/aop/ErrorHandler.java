package com.example.shopappbackend.aop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.shopappbackend.dto.ErrorDto;
import com.example.shopappbackend.enums.ErrorCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.example.shopappbackend.exception.CustomArgumentNotValidException;
import com.example.shopappbackend.exception.LogicRuntimeException;

@ControllerAdvice
public class ErrorHandler extends ResponseEntityExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(ErrorHandler.class);

    /**
     * 統一處理 Exception並回傳INTERNAL_SERVER_ERROR
     *
     * @param ex      ex
     * @return ResponseEntity
     */
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorDto> handleConflict(Exception ex) {
        logger.error("ErrorHandler catch:", ex);
        ErrorDto res = new ErrorDto(ErrorCode.SERVER_ERROR.code(), ErrorCode.SERVER_ERROR.message(), null);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
    }

    /**
     * 統一處理 CustomArgumentNotValidException 將錯誤欄位封裝回傳BAD_REQUEST
     *
     * @param ex      CustomArgumentNotValidException
     * @return ResponseEntity
     */
    @ExceptionHandler(CustomArgumentNotValidException.class)
    protected ResponseEntity<ErrorDto> handleCustomArgumentNotValid(CustomArgumentNotValidException ex) {
        Map<String, List<String>> map = getFieldErrors(ex.getBindingResult());
        ErrorDto res = new ErrorDto(ErrorCode.ARGUMENT_NOT_VALID.code(), ErrorCode.ARGUMENT_NOT_VALID.message(), map);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }

    /**
     * 統一處理 LogicRuntimeException 將訊息回傳至前端 UNPROCESSABLE_ENTITY (422)
     *
     * @param ex      LogicRuntimeException
     * @return ResponseEntity
     */
    @ExceptionHandler(LogicRuntimeException.class)
    protected ResponseEntity<ErrorDto> handleLogicRuntimeException(LogicRuntimeException ex) {
        ErrorDto res = new ErrorDto(ErrorCode.UNPROCESSABLE_ENTITY.code(), ex.getMessage(), null);
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(res);
    }

    /**
     * 統一處理 BadCredentialsException (401)
     *
     * @param ex      BadCredentialsException
     * @return ResponseEntity
     */
    @ExceptionHandler(BadCredentialsException.class)
    protected ResponseEntity<Void> handleBadCredentialsException(BadCredentialsException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    /**
     * 統一處理 UsernameNotFoundException (401)
     *
     * @param ex      UsernameNotFoundException
     * @return ResponseEntity
     */
    @ExceptionHandler(UsernameNotFoundException.class)
    protected ResponseEntity<Void> handleBadCredentialsException(UsernameNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    /**
     * 統一處理 @Valid 錯誤，controller
     * 方法不要寫BindingResult，不然MethodArgumentNotValidException會被catch
     *
     * @param ex      MethodArgumentNotValidException
     * @param headers HttpHeaders
     * @param status  HttpStatusCode
     * @param request Request
     * @return ResponseEntity
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Map<String, List<String>> map = getFieldErrors(ex.getBindingResult());
        ErrorDto res = new ErrorDto(ErrorCode.ARGUMENT_NOT_VALID.code(), ErrorCode.ARGUMENT_NOT_VALID.message(), map);
        return handleExceptionInternal(ex, res, headers, HttpStatus.BAD_REQUEST, request);
    }

    /**
     * 將錯誤欄位封裝
     *
     * @param result BindingResult
     * @return Map<String, List < String>>
     */
    private Map<String, List<String>> getFieldErrors(BindingResult result) {
        Map<String, List<String>> map = new HashMap<>();
        for (FieldError fieldError : result.getFieldErrors()) {
            map.computeIfAbsent(fieldError.getField(), k -> new ArrayList<>()).add(fieldError.getDefaultMessage());
        }
        return map;
    }


}
