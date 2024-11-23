package com.musinsa.assignment.type;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public enum CategoryType {

  top("상의", 1),
  outer("아우터", 2),
  pants("바지", 3),
  sneakers("스니커즈", 4),
  bag("가방", 5),
  hat("모자", 6),
  socks("양말", 7),
  accessories("액세서리", 8);

  private final String name;
  private final int order;

  public static CategoryType ofEnumName(String enumName) {
    return Arrays.stream(CategoryType.values())
        .filter(categoryType -> categoryType.name().equals(enumName))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 카테고리입니다. - " + enumName));
  }
}
