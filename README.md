# TripToN 🧳

여행 짐싸기 고민을 AI가 해결해주는 웹 애플리케이션입니다.

사용자가 여행 가방 타입을 선택하고 고민을 입력하면, Gemini AI가 맞춤형 답변을 제공합니다.

## 📋 목차

- [주요 기능](#-주요-기능)
- [기술 스택](#-기술-스택)
- [시작하기](#-시작하기)
  - [필수 요구사항](#필수-요구사항)
  - [설치 및 실행](#설치-및-실행)
- [환경 설정](#-환경-설정)
- [프로젝트 구조](#-프로젝트-구조)
- [API 엔드포인트](#-api-엔드포인트)

## ✨ 주요 기능

- **가방 타입 선택**: 여행 캐리어, 카트, 백팩 중 선택
- **AI 고민 상담**: Gemini AI를 활용한 맞춤형 여행 짐싸기 조언
- **고민 저장**: 사용자의 고민과 AI 답변을 데이터베이스에 저장
- **공개 게시판**: 다른 사용자들의 고민과 답변 조회 (비밀번호로 상세 조회 보호)
- **반응형 UI**: 아름다운 인터랙티브 웹 인터페이스

## 🛠 기술 스택

### Backend
- **Java 21**
- **Spring Boot 3.5.4**
  - Spring Data JPA
  - Spring Web
  - Spring Validation
  - Thymeleaf
- **Lombok**
- **WebFlux** (AI API 통신)

### Database
- **MySQL 8.0+**
- **Hibernate/JPA**

### AI
- **Google Gemini API**

### Build Tool
- **Gradle 8.14.3**

## 🚀 시작하기

### 필수 요구사항

- **Java 21** 이상
- **MySQL 8.0** 이상
- **Gradle 8.14.3** 이상 (또는 Gradle Wrapper 사용)
- **Gemini API Key** ([Google AI Studio](https://aistudio.google.com/app/apikey)에서 발급)

### 설치 및 실행

#### 1. 저장소 클론

```bash
git clone https://github.com/your-username/TripToN.git
cd TripToN
```

#### 2. MySQL 데이터베이스 생성

```sql
CREATE DATABASE TripToNDB CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

#### 3. 환경 변수 설정

`.env.example` 파일을 복사하여 `.env` 파일을 생성하고 값을 입력합니다:

```bash
cp .env.example .env
```

`.env` 파일 수정:

```properties
# Database Configuration
DB_USERNAME=root
DB_PASSWORD=your_mysql_password
DB_NAME=TripToNDB
DB_PORT=3306

# Gemini AI Configuration
GEMINI_API_KEY=your_gemini_api_key_here
```

#### 4. 로컬 프로필 설정 (선택사항)

`src/main/resources/application-local.properties` 파일을 생성하고 설정:

```properties
# Gemini API Key (로컬 개발용)
gemini.api.key=your_gemini_api_key_here
```

#### 5. 애플리케이션 실행

**Gradle Wrapper 사용 (권장):**

```bash
# macOS/Linux
./gradlew bootRun

# Windows
gradlew.bat bootRun
```

**또는 빌드 후 실행:**

```bash
./gradlew build
java -jar build/libs/TripToN-0.0.1-SNAPSHOT.jar
```

#### 6. 브라우저에서 접속

```
http://localhost:8080
```

## ⚙️ 환경 설정

### application.properties 주요 설정

| 설정 항목 | 기본값 | 설명 |
|----------|--------|------|
| `server.port` | 8080 | 서버 포트 |
| `spring.profiles.active` | local | 활성 프로필 |
| `spring.datasource.url` | jdbc:mysql://localhost:3306/TripToNDB | 데이터베이스 URL |
| `spring.jpa.hibernate.ddl-auto` | update | JPA DDL 생성 전략 |
| `gemini.enabled` | true | Gemini AI 활성화 여부 |

### 프로필 설정

- **local**: 로컬 개발 환경 (기본)
- **docker**: Docker 컨테이너 환경
- **prod**: 프로덕션 환경

프로필 변경:
```bash
./gradlew bootRun --args='--spring.profiles.active=prod'
```

## 📁 프로젝트 구조

```
TripToN/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── TripToN/TripToN/
│   │   │       ├── domain/           # 도메인 엔티티
│   │   │       │   ├── Concern.java
│   │   │       │   ├── Luggage.java
│   │   │       │   └── LuggageType.java
│   │   │       ├── service/          # 비즈니스 로직
│   │   │       │   ├── LuggageService.java
│   │   │       │   └── responseService/
│   │   │       │       ├── ResponseService.java
│   │   │       │       ├── geminiService/
│   │   │       │       │   ├── GeminiService.java
│   │   │       │       │   ├── GeminiConfig.java
│   │   │       │       │   ├── GeminiRequest.java
│   │   │       │       │   └── GeminiResponse.java
│   │   │       │       └── DefaultService.java
│   │   │       ├── MainController.java
│   │   │       ├── LuggageRepository.java
│   │   │       └── TripToNApplication.java
│   │   └── resources/
│   │       ├── templates/            # Thymeleaf 템플릿
│   │       │   ├── 1_main.html
│   │       │   ├── 2_introduce.html
│   │       │   ├── 3_select.html
│   │       │   ├── 5_info.html
│   │       │   ├── 6_result.html
│   │       │   └── luggage_detail.html
│   │       ├── static/               # 정적 리소스
│   │       │   ├── css/
│   │       │   ├── js/
│   │       │   └── image/
│   │       ├── application.properties
│   │       ├── application-local.properties
│   │       ├── application-docker.properties
│   │       └── application-prod.properties
│   └── test/
├── .env.example                      # 환경변수 예제
├── .gitignore
├── build.gradle
├── Dockerfile                        # Docker 이미지 빌드
├── docker-compose.yml                # Docker Compose 설정
└── README.md
```

## 🔗 API 엔드포인트

### 웹 페이지

| Method | Endpoint | 설명 |
|--------|----------|------|
| GET | `/` | 메인 페이지 |
| GET | `/introduce` | 서비스 소개 페이지 |
| GET | `/select` | 가방 타입 선택 페이지 |
| GET | `/info` | 입력한 고민 확인 페이지 |
| GET | `/result` | 모든 고민 목록 조회 |
| GET | `/{lid}` | 특정 고민 상세 조회 |

### API

| Method | Endpoint | 설명 |
|--------|----------|------|
| POST | `/concern` | 고민 등록 및 AI 답변 생성 |
| POST | `/verify-password` | 비밀번호 검증 (AJAX) |
| GET | `/api/luggage/{lid}/response` | 특정 고민의 AI 답변 조회 |

### POST /concern 요청 예시

```
Content-Type: application/x-www-form-urlencoded

luggageType=LuggageA
concern=제주도 3박 4일 여행인데 어떤 옷을 챙겨야 할까요?
userName=홍길동
password=1234
```

## 🐳 Docker로 실행하기

### Docker Compose 사용

```bash
# 컨테이너 빌드 및 실행
docker-compose up -d

# 로그 확인
docker-compose logs -f

# 컨테이너 중지
docker-compose down
```

애플리케이션이 `http://localhost:8080`에서 실행됩니다.

### Docker만 사용

```bash
# 이미지 빌드
docker build -t tripton-app .

# 컨테이너 실행
docker run -p 8080:8080 \
  -e GEMINI_API_KEY=your_api_key \
  -e DB_PASSWORD=your_password \
  tripton-app
```

## 🔑 Gemini API Key 발급

1. [Google AI Studio](https://aistudio.google.com/app/apikey) 접속
2. Google 계정으로 로그인
3. "Get API Key" 클릭
4. 새 API 키 생성
5. 생성된 키를 `.env` 파일이나 `application-local.properties`에 설정

## 🛡️ 보안 주의사항

- `.env` 파일과 `application-local.properties`는 절대 Git에 커밋하지 마세요
- API Key는 환경변수로 관리하세요
- 프로덕션 환경에서는 강력한 데이터베이스 비밀번호를 사용하세요

## 📝 라이선스

이 프로젝트는 학습 목적으로 작성되었습니다.

## 👥 개발자

- **상원** - [GitHub](https://github.com/your-username)

## 🤝 기여하기

Pull Request는 언제나 환영합니다!

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📧 문의

프로젝트에 대한 질문이나 제안이 있으시면 Issue를 열어주세요.

---

⭐️ 이 프로젝트가 도움이 되었다면 Star를 눌러주세요!
