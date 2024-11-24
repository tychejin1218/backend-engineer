package com.musinsa.assignment.common.exception;


import com.musinsa.assignment.common.type.ApiStatusType;
import java.io.Serial;
import lombok.Getter;

@Getter
public class ApiException extends RuntimeException {

  @Serial
  private static final long serialVersionUID = -3265652963196290284L;

  private final String status;
  private final String message;

  public ApiException(ApiStatusType apiStatusType) {
    super();
    this.status = apiStatusType.getCode();
    this.message = apiStatusType.getMessage();
  }
}
