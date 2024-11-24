# [MUSINSA] Java(Kotlin) Backend Engineer 과제 

## 목차
1. [구현 범위에 대한 설명](#-구현-범위에-대한-설명)
2. [코드 빌드, 테스트, 실행 방법](#-코드-빌드-테스트-실행-방법)
3. [기타 추가 정보](#-기타-추가-정보)

## 구현 범위에 대한 설명

과제를 구현한 End-point 및 Method 정보입니다.

- **구현 1) 카테고리 별 최저가격 브랜드와 상품 가격, 총액을 조회하는 API**
  - End-point: `GET - /v1/product/cheapest-per-category`
  - Method: `getCheapestProductForEachCategory`

- **구현 2) 단일 브랜드로 모든 카테고리 상품을 구매할 때 최저가격에 판매하는 브랜드와 카테고리의 상품가격, 총액을 조회하는 API**
  - End-point: `GET - /v1/product/cheapest-total-brand`
  - Method: `getCheapestBrandForTotalPrice`

- **구현 3) 카테고리 이름으로 최저, 최고 가격 브랜드와 상품 가격을 조회하는 API**
  - End-point: `GET /v1/product/product/{categoryName}`
  - Method: `getProductByCategory`

- **구현 4) 브랜드 및 상품을 추가 / 업데이트 / 삭제하는 API**
  - End-points:
    - `POST - /v1/product` (Method: `insertProduct`)
    - `PUT - /v1/product` (Method: `updateProduct`)
    - `DELETE - /v1/product` (Method: `deleteProduct`)
  
## 코드 빌드, 테스트, 실행 방법

개발 환경, 테스트 방법, 그리고 애플리케이션의 실행 방법에 대한 정보입니다.

- **개발 환경**
  - Java: `OpenJDK 17`
  - Gradle: `8.11.1`

- **테스트 유닛**
  - ProductController 단위 테스트: `ProductControllerTest`
  - ProductService 단위 테스트: `ProductServiceTest`
  - ProductRepository 단위 테스트: `ProductRepositoryTest`

- **실행 방법**
  - 프로젝트 빌드: `./gradlew build`
  - .jar 파일의 디렉토리로 이동: `cd build/libs`
  - .jar 파일 실행하여 애플리케이션 시작: `java -jar backend-engineer-0.0.1-SNAPSHOT.jar`
  - API 테스팅: 웹 브라우저나 API 테스트 도구를 이용하여 아래 API에 접속하세요.
    - [http://localhost:9091/api/v1/product/cheapest-per-category](http://localhost:9091/api/v1/product/cheapest-per-category)
 
## 기타 추가 정보

### Library

프로젝트에서 사용된 주요 라이브러리 및 도구들의 목록입니다.

- **Spring Boot**
  - `org.springframework.boot:spring-boot-starter-web`
  - `org.springframework.boot:spring-boot-starter-aop`
  - `org.springframework.boot:spring-boot-starter-test`
- **Embedded Tomcat**
  - `org.springframework.boot:spring-boot-starter-tomcat`
- **Database**
  - `com.h2database:h2`
  - `org.springframework.boot:spring-boot-starter-data-jpa`
- **Lombok**
  - `org.projectlombok:lombok`
- **ModelMapper**
  - `org.modelmapper:modelmapper:3.2.1`

### Static Analysis Tools

- **Checkstyle** (코드 스타일 유지를 위한 정적 코드 분석 도구)
  - 사용 버전: `10.20.0`
  - 설정 파일:
    - `config/checkstyle/google-checkstyle.xml`
    - `config/checkstyle/checkstyle-suppressions.xml`

- **PMD** (코드 품질 유지 및 코드 스타일 규칙 적용을 위한 정적 코드 분석 도구)
  - 사용 버전: `7.7.0`
  - 설정 파일: `config/pmd/custom-ruleset.xml`