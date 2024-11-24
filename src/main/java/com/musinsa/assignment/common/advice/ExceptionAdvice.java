package com.musinsa.assignment.common.advice;

import com.musinsa.assignment.common.exception.ApiException;
import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 공통 예외 처리 핸들러
 */
@Slf4j
@RestControllerAdvice
public class ExceptionAdvice {

  /**
   * 일반 예외 처리
   *
   * @param request HTTP 요청 객체
   * @param e       발생한 예외
   * @return ResponseEntity 에러 응답 객체
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<Map<String, Object>> handleException(
      HttpServletRequest request,
      Exception e) {
    log.error("handleException : {}", request.getRequestURI(), e);

    Map<String, Object> errorMap = new HashMap<>();
    errorMap.put("status", HttpStatus.INTERNAL_SERVER_ERROR);
    errorMap.put("message", e.getMessage());

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMap);
  }

  /**
   * 커스텀 예외 처리
   *
   * @param request HTTP 요청 객체
   * @param e       발생한 ApiException
   * @return ResponseEntity 에러 응답 객체
   */
  @ExceptionHandler(ApiException.class)
  public ResponseEntity<Map<String, Object>> handleApiException(
      HttpServletRequest request,
      ApiException e) {
    log.error("handleApiException : {}", request.getRequestURI(), e);

    Map<String, Object> errorMap = new HashMap<>();
    errorMap.put("status", e.getStatus());
    errorMap.put("message", e.getMessage());

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMap);
  }
}
