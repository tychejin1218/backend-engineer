package com.musinsa.assignment.common.type;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public enum CategoryType {

  TOP("상의", 1),
  OUTER("아우터", 2),
  PANTS("바지", 3),
  SNEAKERS("스니커즈", 4),
  BAG("가방", 5),
  HAT("모자", 6),
  SOCKS("양말", 7),
  ACCESSORIES("액세서리", 8);

  private final String name;
  private final int order;

  public static CategoryType ofEnumName(String enumName) {
    return Arrays.stream(values())
        .filter(categoryType -> categoryType.name().equalsIgnoreCase(enumName))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 카테고리입니다. - " + enumName));
  }

  public static CategoryType ofName(String name) {
    return Arrays.stream(values())
        .filter(categoryType -> categoryType.getName().equalsIgnoreCase(name))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 카테고리입니다. - " + name));
  }
}
