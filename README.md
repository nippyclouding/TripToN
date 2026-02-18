# TripToN

고민을 가방에 담아 AI 상담사에게 맡기는 서비스

사용자가 자신의 고민을 가방 유형과 함께 제출하면, Google Gemini AI가 따뜻하고 공감적인 맞춤 조언을 생성해줍니다.
결과 페이지에서 다른 사용자들의 가방도 볼 수 있으며, 비밀번호를 통해 각 고민의 응답을 열어볼 수 있습니다.

---

## 시작하기

### 사전 준비

- Java 21 이상
- MySQL 8.0 이상 (실행 중이어야 함)
- Gemini API Key ([Google AI Studio](https://aistudio.google.com/app/apikey)에서 무료 발급)

### 1. 저장소 클론

```bash
git clone https://github.com/your-username/TripToN.git
cd TripToN
```

### 2. MySQL 데이터베이스 생성

```sql
CREATE DATABASE TripToNDB CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 3. 환경변수 설정

`application.properties`의 모든 설정은 환경변수로 덮어쓸 수 있습니다.
로컬 실행 시 가장 간단한 방법은 환경변수를 직접 지정하는 것입니다.

| 환경변수 | 기본값 | 설명 |
|----------|--------|------|
| `DB_USERNAME` | root | MySQL 사용자 |
| `DB_PASSWORD` | password | MySQL 비밀번호 |
| `GEMINI_API_KEY` | (없음) | Gemini API 키 |
| `GEMINI_ENABLED` | true | AI 기능 활성화 여부 |
| `SERVER_PORT` | 8080 | 서버 포트 |

### 4. 실행

```bash
# macOS / Linux
GEMINI_API_KEY=your_api_key DB_PASSWORD=your_db_password ./gradlew bootRun

# Windows PowerShell
$env:GEMINI_API_KEY="your_api_key"; $env:DB_PASSWORD="your_db_password"; ./gradlew bootRun
```

또는 빌드 후 실행:

```bash
./gradlew bootJar
GEMINI_API_KEY=your_api_key DB_PASSWORD=your_db_password java -jar build/libs/TripToN-0.0.1-SNAPSHOT.jar
```

### 5. 접속

```
http://localhost:8080
```

> Gemini API 키 없이도 실행 가능합니다. AI 응답 대신 고민 길이 기반의 기본 응답이 제공됩니다.

### Gemini API Key 발급 방법

1. [Google AI Studio](https://aistudio.google.com/app/apikey) 접속
2. Google 계정으로 로그인
3. **Get API Key** 클릭 후 키 생성
4. 발급받은 키를 실행 시 `GEMINI_API_KEY` 환경변수에 입력

```bash
# 예시
GEMINI_API_KEY=AIzaSyxxxxxxxxxxxxxxxxxxxxxxx DB_PASSWORD=mypassword ./gradlew bootRun
```

---

## API 문서 (Swagger UI)

이 프로젝트는 **Spring MVC 기반의 서버사이드 렌더링(Thymeleaf) 애플리케이션**이지만,
일부 엔드포인트는 REST API 형태로도 제공되며, Swagger UI를 통해 전체 엔드포인트를 한눈에 조회할 수 있습니다.

> MVC 엔드포인트(뷰 반환)는 Swagger UI에서 직접 실행하면 HTML 응답이 반환됩니다.
> REST API 엔드포인트(`/api/**`, `/verify-password`)는 Swagger UI에서 직접 테스트 가능합니다.

### 접속 URL

| 항목 | URL |
|------|-----|
| Swagger UI | `http://localhost:8080/swagger-ui/index.html` |
| OpenAPI JSON | `http://localhost:8080/v3/api-docs` |

### 사용 방법

1. 애플리케이션 실행 후 브라우저에서 `http://localhost:8080/swagger-ui/index.html` 접속
2. 엔드포인트 목록에서 원하는 항목 클릭하여 펼치기
3. **Try it out** 버튼 클릭
4. 필요한 파라미터 입력 후 **Execute** 클릭
5. 하단 **Response body**에서 결과 확인

### REST API 엔드포인트 (Swagger에서 직접 테스트 가능)

| Method | Endpoint | 설명 | 요청 예시 |
|--------|----------|------|----------|
| GET | `/api/luggage` | 가방 목록 페이지네이션 조회 | `?page=0` |
| GET | `/api/luggage/{lid}/response` | 특정 가방의 AI 응답 조회 | `lid`: 가방 ID |
| POST | `/verify-password` | 비밀번호 검증 | `{"lid": 1, "password": "1234"}` |

---

## 기술 스택

| 분류 | 기술 |
|------|------|
| Backend | Java 21, Spring Boot 3.5.4, Spring Data JPA |
| HTTP Client | RestClient (Spring 6.1+) |
| Frontend | Thymeleaf, HTML/CSS/JavaScript |
| Database | MySQL 8.0 |
| AI | Google Gemini 2.5 Flash Lite API |
| Security | BCrypt (비밀번호 해싱) |
| Test | JUnit 5, Mockito, MockMvc, H2 |
| Build | Gradle 8.14.3 |

### RestClient를 선택한 이유

Gemini API 호출에 WebClient 대신 **RestClient**를 사용합니다.

이 프로젝트는 Spring MVC 기반의 동기 웹 애플리케이션입니다. 컨트롤러가 `String`을 반환하고, 서비스 계층도 동기 방식으로 동작합니다.
이 구조에서 WebClient를 사용하면 결국 `.block()`으로 강제 동기화해야 하는데, 이는 비동기 클라이언트의 장점을 전혀 살리지 못하는 안티패턴입니다.

| 비교 | RestClient | WebClient + `.block()` |
|------|-----------|----------------------|
| 설계 의도 | 동기 HTTP 호출 | 비동기/리액티브 HTTP 호출 |
| Spring MVC 호환성 | 자연스러움 | `.block()` 강제 필요 |
| 스레드 안전성 | 안전 | `.block()`이 스레드 풀 고갈 유발 가능 |
| 코드 복잡도 | 단순 | Mono/Flux 불필요한 래핑 |
| 도입 시점 | Spring 6.1+ (최신) | Spring 5.0+ |

**WebClient가 적합한 경우:** 컨트롤러가 `Mono<String>`을 반환하는 WebFlux 기반 리액티브 애플리케이션일 때.
현재 프로젝트에서 비동기 전환이 필요하다면, 그때 WebFlux + WebClient로 마이그레이션하면 됩니다.

---

## 서비스 흐름

```
메인 페이지 ──> 소개 페이지 ──> 가방 선택 & 고민 입력 ──> AI 응답 생성 ──> 결과 확인 ──> 결과 갤러리
   (/)        (/introduce)       (/select)            (서버 처리)       (/info)       (/result)
```

### 1. 메인 페이지 (`/`)
서비스 진입점. 사이드바 메뉴와 배경 음악 토글이 있으며, 화살표를 클릭하면 소개 페이지로 이동합니다.

### 2. 소개 페이지 (`/introduce`)
배경 영상과 함께 서비스 컨셉을 스토리텔링 형식으로 설명합니다. 가방 유형별 소개 영상이 포함되어 있습니다.

### 3. 가방 선택 & 고민 입력 (`/select`)
캐러셀을 통해 3가지 가방 중 하나를 선택하고, 모달에서 고민(최대 120자), 이름, 비밀번호(최대 4자리)를 입력합니다.

| 선택지 | 타입 | 설명 |
|--------|------|------|
| LuggageA | LUGGAGE | 베개가방 |
| LuggageB | CART | 서류가방 |
| LuggageC | BAG | 세탁바구니가방 |

### 4. 서버 처리 (`POST /concern`)
입력값 검증 후 Gemini AI에게 프롬프트를 전달하여 한국어 맞춤 조언(300자 이내)을 생성합니다.
AI 호출 실패 시 고민 길이 기반의 기본 응답으로 자동 대체됩니다. 생성된 Luggage를 DB에 저장합니다.

### 5. 결과 확인 (`/info`)
사용자가 입력한 고민과 AI가 생성한 응답을 확인합니다.

### 6. 결과 갤러리 (`/result`)
모든 사용자의 가방 목록을 페이지네이션(5개/페이지)으로 조회합니다.
잠수함 아이콘을 클릭하면 가방들이 원형으로 배치되며, 특정 가방을 클릭하고 비밀번호를 입력하면 해당 고민의 AI 응답을 확인할 수 있습니다.

### 7. 상세 페이지 (`/{lid}`)
개별 고민의 가방 타입, 사용자 정보, 고민 내용, AI 응답을 상세하게 보여줍니다.

---

## 비즈니스 로직

### 핵심 흐름

```
[사용자 입력]                    [서버]                         [외부 API]
     |                            |                               |
     |  가방타입 + 고민 + 이름     |                                  |
     |  + 비밀번호                 |                                |
     |--------------------------->|                               |
     |                            |  1. 입력값 검증                  |
     |                            |  2. LuggageType 변환           |
     |                            |  3. Concern 객체 생성           |
     |                            |                               |
     |                            |  4. 프롬프트 구성 & API 호출      |
     |                            |------------------------------>|
     |                            |                               |  Gemini AI
     |                            |       AI 응답 (300자 이내)       |
     |                            |<------------------------------|
     |                            |                               |
     |                            |  5. Concern에 응답 할당          |
     |                            |  6. Luggage 생성 & DB 저장      |
     |                            |  7. 세션에 저장                  |
     |                            |                               |
     |   고민 + AI 응답 표시       |                                  |
     |<---------------------------|                               |
```

### AI 응답 생성 (ResponseService)

Strategy 패턴으로 구현되어 있으며, `gemini.enabled` 설정에 따라 구현체가 결정됩니다.

**GeminiService** (`gemini.enabled=true`)
- Gemini 2.5 Flash Lite API를 호출하여 맞춤 상담 응답 생성
- 실패 시 DefaultService로 자동 fallback
- 아래 프롬프트에 `{userName}`과 `{concern}`을 삽입하여 API에 전달합니다:

> 당신은 사람들의 고민 상담사입니다. {userName}님이 다음과 같은 고민을 가지고 있습니다: '{concern}'.
> 이 고민에 대해 따뜻하고 공감적이며 실용적인 조언을 2-3문장으로 제공해주세요.
> 답변은 한국어로 하고, 격려와 구체적인 해결방안을 포함해주세요.
> 응답 결과에 사용자의 고민 크기를 반영하여 반드시 한글로 300자 안으로 되게 해주세요.
> 만약 입력이 고민이 아니라 다른 대화라면 적절히 응답해주세요.

**DefaultService** (`gemini.enabled=false` 또는 API 실패 시)
- 고민 텍스트 길이에 따라 3단계 기본 응답 반환
  - 40자 미만: "가벼운 고민" 응답
  - 40~80자: "작지 않은 고민" 응답
  - 80자 이상: "큰 고민" 응답

### 비밀번호 검증 (결과 갤러리)

```
[결과 페이지에서 가방 클릭]
         |
         v
  비밀번호 입력 모달
         |
         v
  POST /verify-password (AJAX)
         |
    일치 여부 확인
      /        \
   성공          실패
    |             |
    v             v
  GET /api/     에러 메시지
  luggage/{lid}
  /response
    |
    v
  AI 응답 표시
```

---

## 데이터 모델

### Luggage (Entity)

| 필드 | 타입 | 설명 |
|------|------|------|
| LID | Long | PK, 자동 생성 |
| concern | Concern | 고민 정보 (Embedded) |
| luggageType | LuggageType | LUGGAGE / CART / BAG |
| dateTime | LocalDateTime | 생성 시각 |

### Concern (Embeddable, Luggage에 내장)

| 필드 | 타입 | 제약 | 설명 |
|------|------|------|------|
| userName | String | max 20, NotBlank | 사용자 이름 |
| concern | String | max 120, NotBlank | 고민 내용 |
| password | String | BCrypt 해싱, 60자 | 열람 비밀번호 (BCrypt로 해싱 저장) |
| response | String | TEXT | AI 생성 응답 |

---

## API 엔드포인트

### 페이지

| Method | Endpoint | 설명 |
|--------|----------|------|
| GET | `/` | 메인 페이지 |
| GET | `/introduce` | 서비스 소개 페이지 |
| GET | `/select` | 가방 선택 & 고민 입력 페이지 |
| POST | `/concern` | 고민 등록 및 AI 답변 생성 |
| GET | `/info` | 고민 확인 페이지 |
| GET | `/result` | 결과 갤러리 |
| GET | `/{lid}` | 개별 상세 페이지 |

### AJAX API

| Method | Endpoint | 설명 |
|--------|----------|------|
| POST | `/verify-password` | 비밀번호 검증 |
| GET | `/api/luggage/{lid}/response` | AI 응답 조회 |

---

## 프로젝트 구조

```
src/main/java/TripToN/TripToN/
├── TripToNApplication.java                  # Spring Boot 진입점
├── controller/
│   └── MainController.java                  # 페이지 및 API 엔드포인트
├── repository/
│   └── LuggageRepository.java               # JPA Repository
├── domain/
│   ├── Luggage.java                         # 가방 엔티티
│   ├── Concern.java                         # 고민 값 객체 (Embeddable, BCrypt 해싱)
│   └── LuggageType.java                     # 가방 타입 Enum
├── service/
│   ├── LuggageService.java                  # 핵심 비즈니스 로직
│   └── responseService/
│       ├── ResponseService.java             # 응답 생성 인터페이스 (Strategy)
│       ├── DefaultService.java              # 기본 응답 (길이 기반)
│       └── GeminiService.java               # Gemini API 호출 (RestClient)
└── config/
    ├── ServiceConfig.java                   # 조건부 빈 설정
    ├── GeminiConfig.java                    # RestClient 설정
    ├── SwaggerConfig.java                   # SpringDoc OpenAPI 설정
    └── dto/
        ├── GeminiRequest.java               # API 요청 DTO
        └── GeminiResponse.java              # API 응답 DTO

src/test/java/TripToN/TripToN/
├── TripToNApplicationTests.java             # 컨텍스트 로드 테스트
├── controller/
│   └── MainControllerTest.java              # 컨트롤러 테스트 (MockMvc)
├── repository/
│   └── LuggageRepositoryTest.java           # JPA 통합 테스트 (H2)
├── domain/
│   ├── ConcernTest.java                     # Concern 단위 테스트
│   └── LuggageTest.java                     # Luggage 단위 테스트
└── service/
    ├── LuggageServiceTest.java              # 서비스 단위 테스트 (Mockito)
    ├── UserServiceTest.java                 # 전체 흐름 통합 테스트
    └── responseService/
        ├── DefaultServiceTest.java          # DefaultService 단위 테스트
        └── GeminiServiceTest.java           # GeminiService 단위 테스트 (Mockito)

src/main/resources/
├── application.properties                   # 통합 설정 파일
├── templates/                               # Thymeleaf 템플릿
└── static/                                  # CSS, JS, 이미지, 영상
```

---

## 테스트

```bash
./gradlew test
```

H2 인메모리 데이터베이스를 사용하므로 MySQL 없이 테스트를 실행할 수 있습니다.

### 테스트 전략

이 프로젝트는 **테스트 피라미드** 원칙에 따라 모든 계층을 독립적으로 검증합니다.
각 계층이 책임지는 역할이 다르기 때문에, 테스트가 잡아내는 버그도 다릅니다.

```
           /\
          /통합\            ← UserServiceTest (전체 흐름 검증)
         /-----\
        / 슬라이스\         ← MainControllerTest, LuggageRepositoryTest
       /---------\           (Spring 컨텍스트 일부만 로드)
      / 단위 테스트  \       ← ConcernTest, LuggageTest, LuggageServiceTest,
     /-------------\          DefaultServiceTest, GeminiServiceTest
```

| 계층 | 테스트 방식 | 검증 대상 | Spring 로드 |
|---|---|---|---|
| **Domain** | 순수 JUnit | 객체 생성 규칙, BCrypt 해싱, 상태 검증 로직 | X |
| **Service** | Mockito Mock | 비즈니스 로직, 예외 처리, 의존성 호출 흐름 | X |
| **Repository** | @DataJpaTest + H2 | JPA 매핑, CRUD, 데이터 영속화 | JPA만 |
| **Controller** | @WebMvcTest + MockMvc | URL 라우팅, 입력 검증, 응답 형식, 세션 | MVC만 |

**왜 계층별로 나누는가?**
- Service 테스트에서 Repository를 Mock하면, JPA 매핑 오류는 잡을 수 없음
- Controller의 URL 매핑이나 입력값 검증은 Service 테스트로 검증 불가
- Domain 객체의 해싱 로직 버그는 Mock으로 가려져서 Service 테스트를 통과함
- **각 계층을 독립적으로 테스트해야 버그의 정확한 위치를 빠르게 특정 가능**

### 테스트 구성 (77개)

#### Domain 단위 테스트 (12개)

| 테스트 클래스 | 테스트 수 | 검증 내용 |
|---|---|---|
| `ConcernTest` | 7 | BCrypt 해싱 동작, 동일 비밀번호의 다른 해시 생성, 비밀번호 매칭(정상/오류/빈값), 필드 초기화 |
| `LuggageTest` | 5 | 생성 시 dateTime 자동 설정, isComplete 로직(concern/response null 체크), LuggageType enum 검증 |

- Spring 없이 `new`로 객체를 직접 생성하여 테스트 → 밀리초 단위로 실행
- 비밀번호 평문 저장, salt 고정, NullPointerException 등의 버그를 잡아냄

#### Service 단위 테스트 - Mockito (25개)

| 테스트 클래스 | 테스트 수 | 검증 내용 |
|---|---|---|
| `LuggageServiceTest` | 8 | findAll/findById 정상 및 예외, saveLuggage 검증(complete/incomplete), setResponse 위임 |
| `DefaultServiceTest` | 7 | 고민 길이별 응답 분류(40자/80자 기준), 경계값(39/40/79/80자) 정확한 분기 |
| `GeminiServiceTest` | 10 | API 정상 응답 및 trim, null/빈 candidates fallback, HTTP 에러(401/429/500) fallback, 네트워크 에러 fallback |

- `@Mock`으로 Repository와 외부 API를 격리하여 **비즈니스 로직만** 검증
- `then().should(never()).save(any())` 등으로 **호출되지 않아야 할 메서드**까지 검증
- GeminiService는 RestClient의 메서드 체인을 Mock으로 구성하여, 다양한 API 실패 시나리오(401, 429, 500, 네트워크 에러)에서 DefaultService로 정상 fallback하는지 검증

#### Controller 슬라이스 테스트 - MockMvc (20개)

| 테스트 클래스 | 테스트 수 | 검증 내용 |
|---|---|---|
| `MainControllerTest` | 20 | 전체 7개 엔드포인트의 HTTP 요청/응답 |

- `@WebMvcTest`로 Controller만 로드하여 빠르게 실행
- **페이지 라우팅**: GET 요청별 올바른 뷰 반환 검증
- **입력값 검증**: 빈 고민/이름/비밀번호, 잘못된 가방 타입 → 에러 메시지와 함께 이전 페이지 반환
- **세션 처리**: `MockHttpSession`으로 세션 유/무에 따른 분기(정상 표시 vs 리다이렉트) 검증
- **JSON API**: `jsonPath("$.success")`로 비밀번호 검증 API의 JSON 응답 필드 검증
- **예외 전파**: 존재하지 않는 ID 접근 시 리다이렉트 동작 검증

#### Repository 슬라이스 테스트 - @DataJpaTest (8개)

| 테스트 클래스 | 테스트 수 | 검증 내용 |
|---|---|---|
| `LuggageRepositoryTest` | 8 | save/findById/findAll/delete CRUD 전체 |

- `@DataJpaTest` + H2 인메모리 DB로 **실제 DB에 데이터를 넣고 꺼내며** 검증
- `@GeneratedValue` 자동 ID 생성, `@Embedded` Concern 영속화, BCrypt 해시의 DB 왕복 무결성 검증
- Mock으로는 잡을 수 없는 **JPA 매핑 오류, 컬럼 제약조건 위반** 등을 탐지

#### 통합 테스트 (2개)

| 테스트 클래스 | 테스트 수 | 검증 내용 |
|---|---|---|
| `UserServiceTest` | 2 | 사용자 입력 → Concern 생성 → 응답 생성 → Luggage 생성 전체 흐름 |

- 여러 도메인 객체가 함께 동작하는 **E2E 비즈니스 흐름** 검증
- response 없이 Luggage 생성 시 `isComplete() == false` 확인

### 테스트 작성 패턴

| 패턴 | 적용 방식 |
|---|---|
| **Given / When / Then** | 모든 테스트에서 준비-실행-검증 단계를 주석으로 구분 |
| **@Nested 그룹핑** | 메서드별로 테스트를 내부 클래스로 묶어 트리 구조의 가독성 확보 |
| **BDDMockito** | `given().willReturn()`, `then().should()`로 행위 기반 검증 |
| **@ParameterizedTest** | 동일 로직의 다중 입력값을 `@ValueSource`로 반복 테스트 |
| **경계값 테스트** | 39/40/79/80자 등 비즈니스 규칙 경계에서의 정확한 동작 보장 |
| **Reflection** | `@GeneratedValue` 필드에 테스트용 ID를 강제 설정 |
