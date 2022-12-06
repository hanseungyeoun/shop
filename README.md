# README

# Basic Shopping Mall Admin & 상품 조회, 주문 Api 토이 프로젝트

> Shoping 도메인을 단순화하여 어드민 페이지와과 주문 상품 조회 Api을 구현하였습니다.
> 

## 들어가며

---

### 1. 프로젝트 소개

여러 해 동안 개발업무를 하면서 밑바닥 설계부터 구현까지 나만의 서비스를 가지고 싶어 시작하게 되었습니다.

### 2. 사용 기술

### 2-1 백엔드

**주요 프레임워크 / 라이브러리**

- Java 11
- SpringBoot 2.7.4
- JPA(Spring Data JPA)
- Querydsl
- Spring Security
- Spring Batch

Build Tool

- Gradle 7.5.1

DataBase

- H2

### 2-2 프론트엔드

- Html/css
- JavaScript
- Thymeleaf
- AdminLTE 3.2

### 참고 사항 h2 db 생성

h2 콘솔 접속 후 dbc:h2:~/shop 입력

## 3. 요구사항

### 3.1 Use case

1. 유저 - 서비스를 통해 상품을 선택하여 주문하는 고객
2. 내부 운영자 - 해당 서비스를 운영하고 관리하는 담당자

### 3.2 주요 도메인

1. 유저 : 유저나 내부 운영자를 관리한다.
2. 상품: 상품과 상품의 옵션 정보를 등록하고 카테고리와 브랜드을 관리함

### 3.3 각 도메인별 요구 사항

1. 상품
    - 상품을 등록, 조회 할 수 있다.
    - 상품을 1차 카테고리, 2차 카테고리, 브랜드로 분류한다.
    - 상품은 상품명, 가격 등의 기본 정보와 색상, 사이즈 옵션으로 구성된다
    - 등록된 상품은 유저의 주문을 받아 판매될 수 있다
    - 상품은 판매 준비중, 판매중, 판매 종료와 같은 상태를 가진다.
2. 주문
    - 시스템에 등록된 상품은 유저가 주문할 수 있다
    - 주문은 주문 등록, 결제, 배송 준비, 배송 중, 배송 완료의 단계를 가진다.
    - 주문 등록 과정에서는 결제수단을 선택하고 상품 및 상품 옵션을 선택한다.
    - 시스템에서 사용 가능한 결제수단은 1) 카드 2) 토스페이 3) 카카오페이 4) 네이버
    
    페이 등이 있다.
    
    - 결제 과정에서는 유저가 선택한 결제 수단으로 결제를 진행한다.
    - 결제가 완료되면 배송 준비 단계로 넘어간다 배송 중, 배송 완료의 단계도 순차적으로 진행된다.
    - 구현 간소화를 위해 결제 연동, 주문 취소, 배송정보 연동을 제외한다.
3. 회원
    - 내부 운영자는 어드민화면에서 폼으로 회원가입, 로그인한다.
    - 유저는 Api로 회원가입하고 Jwt 토큰을 발급 받는 방식으로 로그인한다.
    - 유저는 Api로 통해 상품 목록 조회, 주문 등록, 결제, 주문 조회를 한다.

### 3. 설계

- 상품 ERD
    
    ![Untitled 1.png](README%205d8c415a8bb6478885b2def9c0f4f786/Untitled_1.png)
    
- 상품 도메인 다이어그램
    
    ![Untitled](README%205d8c415a8bb6478885b2def9c0f4f786/Untitled.png)
    
- 주문 ERD
    
    ![Untitled 1.png](README%205d8c415a8bb6478885b2def9c0f4f786/Untitled_1%201.png)
    
    - 상품 도메인 다이어그램
        
        ![Untitled](README%205d8c415a8bb6478885b2def9c0f4f786/Untitled%201.png)
        
    - PaymentProcessor 중심으로 함 클라스 다이어그램
        
        ![Untitled](README%205d8c415a8bb6478885b2def9c0f4f786/Untitled%202.png)
        
    - 결제 처리는 유저가 선택한 결제 수단에 맞는 PaymentProcessor 가 처리하게끔 한다
        - 시스템에서 제공하는 결제 수단은 1) 카드 2) 토스페이 3) 카카오페이 4) 네이버페
        이 - 4가지이다
        - 구체적인 구현으로 들어간다면 각 결제 수단마다 결제를 처리하는 parameter 와
        flow 가 다 다르겠지만
        - 도메인 로직에서는 이를 추상화하여 "해당 주문의 결제를 처리한다" 라는 서비스의
        흐름을 한 눈에 파악할 수 있도록 해야 한다
        - 세세한 구현은 Service 가 아니라 PaymentProcessor 클래
        스에 위임하고, Service 도메인에서는 이를 활용하기 위한 Interface 를 선언
        하고 사용한다
        - -DIP 개념을 활용하여 도메인이 사용하는 Interface 의 실제 구현체를 주입 받
        아 (Injection) 사용할 수 있도록 한다
        - 이렇게 하면 하나의 Service 메서드를 읽기만해도 도메인의 흐름을 파악할 수
        있고, 세부 구현은 새로운 구현체를 Injection 하여 쉽게 변경할 수 있다
    - PaymentProcessor 는 주문의 결제를 처리하는 객체이고 아래와 같은 세부 구현
    을 가진다
        - PaymentValidator - 결제 과정에서의 validation check
        PaymentApiCaller - 결제 수단에 맞는 ApiCaller 호출
        - Spring 이 제공하는 DI 기능을 활용하여 각각의 인터페이스를 구현한 구현체
        를 List 로 받아 활용한다
        - PaymentValidator interface 의 경우 - 아래와 같은 메서드 시그니처를 선언
        하고, 다양한 validation 요구사항을 커버하는 각각의 validation 구현체를 생
        성하여 활용한다
        
        ```java
        public interface PaymentValidator {
        	void validate(Order order, OrderCommand.PaymentRequest paymentRequest);
        }
        ```
        
        - 주문 가격 체크 Validator
        - 결제 수단 체크 Validator
        - 주문 상태 체크 Validator 를 구현하였다.
    - PaymentApiCaller interface 의 경우 - 아래와 같은 메서드 시그니처를 선언
    하고, 다양한 결제수단 구현체를 생성하여 활용한다
        
        ```java
        public interface PaymentApiCaller {
        	boolean support(PayMethod payMethod);
        	void pay(OrderCommand.PaymentRequest request);
        }
        ```
        
        - support 메서드는 전달된 Paymethod 를 자신이 처리할 수 있는지 (=
        support 할 수 있는) 판별하는 return 값 boolean 의 메서드이다
        - pay 메서드는 전달된 paymentRequest 를 활용하여 실제적인 결제 과정
        을 처리하는 메서드이다
        - 예제 구현에서는
        카드
        토스페이
        카카오페이
        네이버페이를 구현하였다 (실제의 pay 메서드는 구현하지 않음)

### 3. 프로젝트 기능

프로젝트의 기능은 다음과 같습니다.

- 어드민 : 회원가입, 폼 로그인, 간단 통계 페이지, 상품조회, 상품 등록, 카테고리 등록, 브랜드 관리 등록 옵션 등록 관리
- Api :
    - 회원관리: 회원가입, Jwt 로그인
    - 상품: 카테고리별 상품조회, 브랜드별 상품
    - 주문: 주문 조회, 주문 등록, 주문 결제
- Batch: 일간 판매금액, 판매상품 수 통계

### 4. 주요 화면

- 어드민 > 회원가입
    
    
    ![Untitled 2.png](README%205d8c415a8bb6478885b2def9c0f4f786/Untitled_2.png)
    
- 어드민 > 로그인
    
    
    ![Untitled 3.png](README%205d8c415a8bb6478885b2def9c0f4f786/Untitled_3.png)
    
- 어드민 > 홈
    
    
    ![Untitled 4.png](README%205d8c415a8bb6478885b2def9c0f4f786/Untitled_4.png)
    
- 어드민 > 상품 관리 >상품 리스트
    
    
    ![Untitled 5.png](README%205d8c415a8bb6478885b2def9c0f4f786/Untitled_5.png)
    
- 어드민 > 상품 관리> 상품 등록
    
    ![Untitled 6.png](README%205d8c415a8bb6478885b2def9c0f4f786/Untitled_6.png)
    
- 어드민 > 주문 관리 > 주문 조회

![Untitled 7.png](README%205d8c415a8bb6478885b2def9c0f4f786/Untitled_7.png)

- 어드민 > 쇼핑몰 관리 > 브랜드 관리
    
    
    ![Untitled 8.png](README%205d8c415a8bb6478885b2def9c0f4f786/Untitled_8.png)
    
- 어드민 > 쇼핑몰 관리 > 브랜드 등록
    
    
    ![Untitled 9.png](README%205d8c415a8bb6478885b2def9c0f4f786/Untitled_9.png)
    
- 어드민 > 쇼핑몰 관리 > 카네고리 관리
    
    
    ![Untitled 10.png](README%205d8c415a8bb6478885b2def9c0f4f786/Untitled_10.png)
    
- 어드민 > 쇼핑몰 관리 > 카테고리 등록
    
    ![Untitled 11.png](README%205d8c415a8bb6478885b2def9c0f4f786/Untitled_11.png)
    
- 어드민 > 쇼핑몰 관리 > 옵션 리스트
    
    ![Untitled 12.png](README%205d8c415a8bb6478885b2def9c0f4f786/Untitled_12.png)
    
- 어드민 > 쇼핑몰 관리 > 옵션 등록
    
    ![Untitled 13.png](README%205d8c415a8bb6478885b2def9c0f4f786/Untitled_13.png)
    

## 5. Api

### 회원가입 API

`POST` /api/v1/join

- curl
    
    ```bash
    curl -X POST \\  <http://localhost:8080/api/v1/join> \  -H 'content-type: application/json' \  -d '{    "username" : "userId",    "password" : "password"}'
    ```
    
- Response
    
    ```json
    {
      "result": "SUCCESS",
      "data": {
        "id": 3,
        "username": "userId"
      },
      "message": "성공",
      "errorCode": null
    }
    ```
    

### 로그인 API

`POST` /api/v1/login

- curl
    
    ```bash
    curl -X POST \\  <http://localhost:8080/api/v1/login> \  -H 'content-type: application/json' \  -d '{    "username" : "userId",    "password" : "password"}'
    ```
    
- Response
    
    ```json
    {
      "result": "SUCCESS",
      "data": {
        "id": 16,
        "itemToken": "itm_ULSSxaLaMDYXzzQH",
        "itemCode": "00010001",
        "itemName": "아디다스",
        "itemPrice": 10000,
        "brandName": "아이디스",
        "itemContent": "<p>아디다스 코드&nbsp;</p>",
        "mainImageName": "c9c83450-f471-4f17-a21c-3c8a4a17ceb4.jpg",
        "itemSalesStatus": "ON_PREPARE",
        "itemDisplayStatus": "ON_NOT_DISPLAY",
        "createdAt": "2022-11-29T01:09:36.043122",
        "updatedAt": "2022-11-29T01:09:36.043122",
        "categoryHierarchy": "아우터 < 코드",
        "itemImageList": [
          "b12072a9-c3c5-417b-af56-7bbf637fede4.jpg"
        ],
        "itemOptionGroupResponseList": [
          {
            "id": 17,
            "ordering": 0,
            "itemOptionGroupName": "color",
            "itemOptionResponseList": [
              {
                "id": 18,
                "ordering": 0,
                "itemOptionName": "베이지",
                "itemOptionPrice": 0
              }
            ]
          },
          {
            "id": 19,
            "ordering": 1,
            "itemOptionGroupName": "size",
            "itemOptionResponseList": [
              {
                "id": 20,
                "ordering": 0,
                "itemOptionName": "110",
                "itemOptionPrice": 0
              }
            ]
          }
        ]
      },
      "message": "성공",
      "errorCode": null
    }
    ```
    

## 상품 조회 API

`GET` /api/v1/items/{itemToken}

- curl
    
    ```bash
    curl -X GET "http://localhost:8080/api/v1/items/itm_ULSSxaLaMDYXzzQH" \-H "accept: application/json;charset=UTF-8"
    ```
    
- Response
    
    ```json
    {
      "result": "SUCCESS",
      "data": {
        "id": 16,
        "itemToken": "itm_ULSSxaLaMDYXzzQH",
        "itemCode": "00010001",
        "itemName": "아디다스",
        "itemPrice": 10000,
        "brandName": "아이디스",
        "itemContent": "<p>아디다스 코드&nbsp;</p>",
        "mainImageName": "c9c83450-f471-4f17-a21c-3c8a4a17ceb4.jpg",
        "itemSalesStatus": "ON_PREPARE",
        "itemDisplayStatus": "ON_NOT_DISPLAY",
        "createdAt": "2022-11-29T01:09:36.043122",
        "updatedAt": "2022-11-29T01:09:36.043122",
        "categoryHierarchy": "아우터 < 코드",
        "itemImageList": [
          "b12072a9-c3c5-417b-af56-7bbf637fede4.jpg"
        ],
        "itemOptionGroupResponseList": [
          {
            "id": 17,
            "ordering": 0,
            "itemOptionGroupName": "color",
            "itemOptionResponseList": [
              {
                "id": 18,
                "ordering": 0,
                "itemOptionName": "베이지",
                "itemOptionPrice": 0
              }
            ]
          },
          {
            "id": 19,
            "ordering": 1,
            "itemOptionGroupName": "size",
            "itemOptionResponseList": [
              {
                "id": 20,
                "ordering": 0,
                "itemOptionName": "110",
                "itemOptionPrice": 0
              }
            ]
          }
        ]
      },
      "message": "성공",
      "errorCode": null
    }
    ```
    

## 브랜드별 상품 조회 API

`GET` /api/v1/items/brands/{brandToken}

- curl
    
    ```bash
    curl -X GET "http://localhost:8080/api/v1/items/brands/brd_JOcnKCNThwIXQuNY" \-H "accept: application/json;charset=UTF-8"
    ```
    
- Response
    
    ```json
    {
      "result": "SUCCESS",
      "data": {
        "content": [
          {
            "id": 16,
            "itemToken": "itm_ULSSxaLaMDYXzzQH",
            "itemCode": "00010001",
            "itemName": "아디다스",
            "itemPrice": 10000,
            "brandName": "아이디스",
            "itemContent": "<p>아디다스 코드&nbsp;</p>",
            "mainImageName": "c9c83450-f471-4f17-a21c-3c8a4a17ceb4.jpg",
            "itemSalesStatus": "ON_PREPARE",
            "itemDisplayStatus": "ON_NOT_DISPLAY",
            "createdAt": "2022-11-29T01:09:36.043122",
            "updatedAt": "2022-11-29T01:09:36.043122",
            "categoryHierarchy": "아우터 < 코드",
            "itemImageList": [
              "b12072a9-c3c5-417b-af56-7bbf637fede4.jpg"
            ],
            "itemOptionGroupResponseList": [
              {
                "id": 17,
                "ordering": 0,
                "itemOptionGroupName": "color",
                "itemOptionResponseList": [
                  {
                    "id": 18,
                    "ordering": 0,
                    "itemOptionName": "베이지",
                    "itemOptionPrice": 0
                  }
                ]
              },
              {
                "id": 19,
                "ordering": 1,
                "itemOptionGroupName": "size",
                "itemOptionResponseList": [
                  {
                    "id": 20,
                    "ordering": 0,
                    "itemOptionName": "110",
                    "itemOptionPrice": 0
                  }
                ]
              }
            ]
          }
        ],
        "pageable": {
          "sort": {
            "empty": false,
            "sorted": true,
            "unsorted": false
          },
          "offset": 0,
          "pageNumber": 0,
          "pageSize": 10,
          "paged": true,
          "unpaged": false
        },
        "last": true,
        "totalPages": 1,
        "totalElements": 1,
        "size": 10,
        "number": 0,
        "sort": {
          "empty": false,
          "sorted": true,
          "unsorted": false
        },
        "first": true,
        "numberOfElements": 1,
        "empty": false
      },
      "message": "성공",
      "errorCode": null
    }
    ```
    

## 카테고리별 상품 조회 API

`GET` /api/v1/items/categories/{categoryToken}

- curl
    
    ```bash
    curl -X GET "http://localhost:8080/api/v1/items/brands/brd_JOcnKCNThwIXQuNY" \-H "accept: application/json;charset=UTF-8"
    ```
    
- Response
    
    ```json
    {
      "result": "SUCCESS",
      "data": {
        "content": [
          {
            "id": 16,
            "itemToken": "itm_ULSSxaLaMDYXzzQH",
            "itemCode": "00010001",
            "itemName": "아디다스",
            "itemPrice": 10000,
            "brandName": "아이디스",
            "itemContent": "<p>아디다스 코드&nbsp;</p>",
            "mainImageName": "c9c83450-f471-4f17-a21c-3c8a4a17ceb4.jpg",
            "itemSalesStatus": "ON_PREPARE",
            "itemDisplayStatus": "ON_NOT_DISPLAY",
            "createdAt": "2022-11-29T01:09:36.043122",
            "updatedAt": "2022-11-29T01:09:36.043122",
            "categoryHierarchy": "아우터 < 코드",
            "itemImageList": [
              "b12072a9-c3c5-417b-af56-7bbf637fede4.jpg"
            ],
            "itemOptionGroupResponseList": [
              {
                "id": 17,
                "ordering": 0,
                "itemOptionGroupName": "color",
                "itemOptionResponseList": [
                  {
                    "id": 18,
                    "ordering": 0,
                    "itemOptionName": "베이지",
                    "itemOptionPrice": 0
                  }
                ]
              },
              {
                "id": 19,
                "ordering": 1,
                "itemOptionGroupName": "size",
                "itemOptionResponseList": [
                  {
                    "id": 20,
                    "ordering": 0,
                    "itemOptionName": "110",
                    "itemOptionPrice": 0
                  }
                ]
              }
            ]
          }
        ],
        "pageable": {
          "sort": {
            "empty": false,
            "sorted": true,
            "unsorted": false
          },
          "offset": 0,
          "pageNumber": 0,
          "pageSize": 10,
          "paged": true,
          "unpaged": false
        },
        "last": true,
        "totalPages": 1,
        "totalElements": 1,
        "size": 10,
        "number": 0,
        "sort": {
          "empty": false,
          "sorted": true,
          "unsorted": false
        },
        "first": true,
        "numberOfElements": 1,
        "empty": false
      },
      "message": "성공",
      "errorCode": null
    }
    ```
    

## 주문 조회 API

`GET` /api/v1/orders/{orderToken}

- curl
    
    ```bash
    curl -X GET "http://localhost:8080/api/v1/orders/ord_YFj145aBDAlgrgcK" \-H "accept: application/json;charset=UTF-8"
    ```
    
- Response
    
    ```json
    {
      "result": "SUCCESS",
      "data": {
        "orderId": 2,
        "orderToken": "ord_YFj145aBDAlgrgcK",
        "userId": 3,
        "userName": "userId",
        "payMethod": "NAVER_PAY",
        "totalAmount": 10000,
        "deliveryInfo": {
          "receiverPhone": "01000001234",
          "receiverZipcode": "한승연",
          "receiverName": "성남시",
          "receiverAddress1": "성남시",
          "receiverAddress2": "우리집",
          "etcMessage": "감사합니다"
        },
        "orderedAt": "2022-11-29T01:51:19.770393",
        "status": "ORDER_COMPLETE",
        "statusDescription": "주문완료",
        "createdAt": "2022-11-29T01:51:19.770393",
        "orderItemList": [
          {
            "orderItemId": 16,
            "orderCount": 1,
            "brandId": 1,
            "itemId": 16,
            "itemName": "아디다스",
            "totalAmount": 10000,
            "itemPrice": 10000,
            "deliveryStatus": "BEFORE_DELIVERY",
            "deliveryStatusDescription": "배송전",
            "optionName": "110 < 베이지",
            "orderItemOptionGroupList": [
              {
                "ordering": 1,
                "itemOptionGroupName": "size",
                "orderItemOptionList": [
                  {
                    "ordering": 1,
                    "itemOptionName": "110",
                    "itemOptionPrice": 0
                  }
                ]
              },
              {
                "ordering": 2,
                "itemOptionGroupName": "color",
                "orderItemOptionList": [
                  {
                    "ordering": 1,
                    "itemOptionName": "베이지",
                    "itemOptionPrice": 0
                  }
                ]
              }
            ]
          }
        ]
      },
      "message": "성공",
      "errorCode": null
    }
    ```
    

## 주문 등록 API

`POST` /api/v1/orders/init

- curl
    
    ```bash
    curl -X POST "http://localhost:8080/api/v1/orders/init" -H "accept: application/json;charset=UTF-8" \-H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJ1c2VybmFtZSI6InVzZXJJZCIsImlhdCI6MTY2OTY1MzI1MywiZXhwIjoxNjcyMjQ1MjUzfQ.2A19inM3F3u_Pp57EtLG23EUuirbWfy34ANKEZgFL3o" -H "Content-Type: application/json;charset=UTF-8" \-d '{
      "userId": "3",
      "payMethod": "NAVER_PAY",
      "receiverName": "한승연",
      "receiverPhone": "01000001234",
      "receiverZipcode": "12345",
      "receiverAddress1": "성남시",
      "receiverAddress2": "우리집",
      "etcMessage": "감사합니다",
      "orderItemList": [
        {
          "orderCount": 1,
          "itemToken": "itm_ULSSxaLaMDYXzzQH",
          "itemName": "아디다스",
          "itemPrice": "10000",
          "brandId": 1,
          "orderItemOptionGroupList": [
            {
              "ordering": 1,
              "itemOptionGroupName": "size",
              "orderItemOptionList": [
                {
                  "ordering": 1,
                  "itemOptionName": "110",
                  "itemOptionPrice": 0
                }
              ]
            },
            {
              "ordering": 2,
              "itemOptionGroupName": "color",
              "orderItemOptionList": [
                {
                  "ordering": 1,
                  "itemOptionName": "베이지",
                  "itemOptionPrice": 0
                }
              ]
            }
          ]
        }
      ]
    }'
    ```
    
- Respone
    
    ```json
    {
      "result": "SUCCESS",
      "data": {
        "orderToken": "ord_YFj145aBDAlgrgcK"
      },
      "message": "성공",
      "errorCode": null
    }
    ```
    

## 결제 요청 Api

`POST` /api/v1/orders/payment-order

- curl
    
    ```bash
    curl -X POST "http://localhost:8080/api/v1/orders/init" -H "accept: application/json;charset=UTF-8" \-H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJ1c2VybmFtZSI6InVzZXJJZCIsImlhdCI6MTY2OTY1MzI1MywiZXhwIjoxNjcyMjQ1MjUzfQ.2A19inM3F3u_Pp57EtLG23EUuirbWfy34ANKEZgFL3o" -H "Content-Type: application/json;charset=UTF-8" \-d '{
      "orderToken": "ord_1agmbewQIWiI9kub",
      "payMethod": "NAVER_PAY",
      "amount": "10000",
      "orderDescription": "주문테스트"
    }'
    ```
    
- Respone
    
    ```json
    {  "result": "SUCCESS",  "data": "OK",  "message": "성공",  "errorCode": null}
    ```
    

---

## 마치며

지금까지 스프링 공부한것을 복습해가면서 어떻하면 좋은 구조가 될까 코드가 되는까 좋은 고민많이하면서 많든 프로젝트입니다.

비록 생각 보다 프로젝트 규모가 커서 예상 시간이 벗어나서 프론트까지 만들지도못하고 많은 기능과 테스트 코드를 다 작성 못하지 못하고 초기 구상과 많이 떨어지게 1차 마무리 지였습니다.

그래도 시행 착오을 토대도 나중에 시간이 되면 완성을 시키고, 다음 프로젝트 할 때 많이 참고 하겠습니다.