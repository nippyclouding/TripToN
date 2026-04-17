# TRIPTON

**고민을 가방에 담아 AI 상담사에게 맡기는 서비스**

사용자가 자신의 고민을 가방 유형과 함께 제출하면 Google Gemini AI가 맞춤 조언을 생성해줍니다.
결과 페이지에서 다른 사용자들의 가방(고민)도 볼 수 있으며, 고민 좋아요, 댓글 추가 & 수정 & 삭제 및 좋아요 버튼을 누를 수 있습니다.

---

### 개발 과정 & 사용 기술

**개발 기간**
- 2개월 (2025.08.~2025.09.)

**개발 인원**
- 2명 (디자이너 1명, 개발자 1명)

**개발 환경**
- **Language & DB**: Java 21, MySQL 9.0
- **Framework**: Spring Boot 3 with JPA (Spring Data JPA)
- **Auth**: Session & Cookie
- **Frontend**: Javascript, Thymeleaf (Server Side Rendering)
- **Infrastructure**: AWS EC2 & Docker Container
  - Spring Boot Container
  - MariaDB Container

---

### 1. ERD

*(내용 없음)*

---

### 2. SYSTEM ARCHITECTURE

*(내용 없음)*

---

### 3. 주요 기능

#### **가방 종류 선택**
사용자는 로그인 후 고민을 담을 가방을 총 3가지 중 하나 선택할 수 있습니다. 

#### **고민 입력**
사용자는 자신의 고민을 입력창에 입력할 수 있습니다.

#### **AI 고민 응답**
Gemini AI에게 고민을 전송하여 응답을 사용자에게 전달합니다.

#### **고민 게시글 전체 조회**
사용자는 전체 고민들을 조회할 수 있습니다.

#### **고민 게시글 상세 조회**
사용자는 각 고민들을 조회할 수 있습니다.
사용자는 댓글 조회, 작성, 수정, 삭제가 가능하며, 좋아요 버튼을 누를 수 있습니다.
