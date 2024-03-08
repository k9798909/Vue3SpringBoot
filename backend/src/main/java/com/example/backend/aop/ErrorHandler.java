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

    /**
     * 統一處理 Exception並回傳INTERNAL_SERVER_ERROR
     * @param ex
     * @param request
     * @return
     */
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorRes> handleConflict(Exception ex, WebRequest request) {
        logger.error("ErrorHandler catch:", ex);
        ErrorRes res = new ErrorRes(ErrorCode.SERVER_ERROR.code(), ErrorCode.SERVER_ERROR.message(), null);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
    }

    /**
     * 統一處理 CustomArgumentNotValidException 將錯誤欄位封裝回傳BAD_REQUEST
     * @param ex
     * @param request
     * @return
     */
    @ExceptionHandler(CustomArgumentNotValidException.class)
    protected ResponseEntity<ErrorRes> handleCustomArgumentNotValid(CustomArgumentNotValidException ex,
            WebRequest request) {
        Map<String, List<String>> map = getFieldErrors(ex.getBindingResult());
        ErrorRes res = new ErrorRes(ErrorCode.ARGUMENT_NOT_VALID.code(), ErrorCode.ARGUMENT_NOT_VALID.message(), map);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }

    /**
     * 統一處理 LogicRuntimeException 將訊息回傳至前端 UNPROCESSABLE_ENTITY (422)
     * @param ex
     * @param request
     * @return
     */
    @ExceptionHandler(LogicRuntimeException.class)
    protected ResponseEntity<ErrorRes> handleLogicRuntimeException(LogicRuntimeException ex, WebRequest request) {
        ErrorRes res = new ErrorRes(ErrorCode.UNPROCESSABLE_ENTITY.code(), ex.getMessage(), null);
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(res);
    }

    /**
     * 統一處理 @Valid 錯誤，controller
     * 方法不要寫BindingResult，不然MethodArgumentNotValidException會被catch
     * @param ex
     * @param headers
     * @param status
     * @param request
     * @return
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Map<String, List<String>> map = getFieldErrors(ex.getBindingResult());
        ErrorRes res = new ErrorRes(ErrorCode.ARGUMENT_NOT_VALID.code(), ErrorCode.ARGUMENT_NOT_VALID.message(), map);
        return handleExceptionInternal(ex, res, headers, HttpStatus.BAD_REQUEST, request);
    }

    /**
     * 將錯誤欄位封裝
     * @param result
     * @return
     */
    private Map<String, List<String>> getFieldErrors(BindingResult result) {
        Map<String, List<String>> map = new HashMap<>();
        for (FieldError fieldError : result.getFieldErrors()) {
            map.computeIfAbsent(fieldError.getField(), k -> new ArrayList<>()).add(fieldError.getDefaultMessage());
        }
        return map;
    }

    /**
     * 錯誤回傳格式
     */
    private static record ErrorRes(String code, String message, Map<String, List<String>> fieldErrors) {
    }

    public static enum ErrorCode {
        ARGUMENT_NOT_VALID("1", "輸入參數驗證失敗"), 
        SERVER_ERROR("2", "伺服器錯誤"), 
        UNPROCESSABLE_ENTITY("3", "邏輯錯誤")// 異常訊息由錯誤訊息組成
        ;

        private final String code;
        private final String message;

        private ErrorCode(String code, String message) {
            this.code = code;
            this.message = message;
        }

        public String code() {
            return code;
        }

        public String message() {
            return message;
        }

    }

}
