package com.musinsa.assignment.common.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ApiStatusType {

  OK("200", "성공"),

  // Custom Exception 에러 코드
  CUSTOM_EXCEPTION("MSS_500", "오류가 발생했습니다. 확인 후 다시 시도해주세요."),
  BRAND_OR_PRODUCT_NOT_FOUND("MSS_401", "브랜드 및 상품이 존재하지 않습니다.");

  private final String code;
  private final String message;
}
