# TRIPTON

**고민을 가방에 담아 AI 상담사에게 맡기는 서비스**

사용자가 자신의 고민을 가방 유형과 함께 제출하면 Google Gemini AI가 맞춤 조언을 생성해줍니다.
결과 페이지에서 다른 사용자들의 가방(고민)도 볼 수 있으며, 고민 좋아요, 댓글 추가 & 수정 & 삭제 및 좋아요 버튼을 누를 수 있습니다.

---

<img width="643" height="574" alt="대표이미지" src="https://github.com/user-attachments/assets/f112c711-ca01-41c4-867a-b29661902be4" />

---

### 개발 과정 & 사용 기술

**개발 기간**
- 2개월 (2025.08.~2025.09.)
- 건국대 시각 영상 디자인학부 졸업 전시 웹 프로젝트

**개발 인원**
- 2명 (디자이너 1명, 개발자 1명)

**개발 환경**
- **Language & DB**: Java 21, MariaDB 11
- **Framework**: Spring Boot 3 with JPA (Spring Data JPA)
- **Auth**: Session & Cookie
- **Frontend**: Javascript, Thymeleaf (Server Side Rendering)
- **Infrastructure**: AWS EC2 & Docker Container
  - Spring Boot Container
  - MariaDB Container
  - NginX Container

---

### 1. ERD

<img width="2270" height="1442" alt="ERD" src="https://github.com/user-attachments/assets/3758fcdd-104e-4976-896a-45eb048a0ebe" />



---

### 2. SYSTEM ARCHITECTURE

<img width="1027" height="918" alt="SystemArchitecture" src="https://github.com/user-attachments/assets/5ae960d9-f287-4ad9-9db6-831555ff84fd" />


---

### 3. 주요 기능

#### **가방 종류 선택**
사용자는 로그인 후 고민을 담을 가방을 총 3가지 중 하나 선택할 수 있습니다. 

<img width="1870" height="949" alt="가방선택" src="https://github.com/user-attachments/assets/4edda114-fa59-47db-9572-96006528b653" />


#### **고민 입력**
사용자는 자신의 고민을 입력창에 입력할 수 있습니다.

<img width="735" height="848" alt="고민" src="https://github.com/user-attachments/assets/0170249d-7041-4130-80b5-568e3675a263" />

#### **AI 고민 응답**
Gemini AI에게 고민을 전송하여 응답을 사용자에게 전달합니다.

<img width="977" height="820" alt="응답" src="https://github.com/user-attachments/assets/a912b9c6-f81f-4cb8-b6af-400c80b69b78" />


#### **고민 게시글 전체 조회**
사용자는 전체 고민들을 조회할 수 있습니다.

<img width="765" height="613" alt="고민리스트" src="https://github.com/user-attachments/assets/64212bad-79ef-4e23-ac7b-fd14f882b804" />


#### **고민 게시글 상세 조회**
사용자는 각 고민들을 조회할 수 있습니다.
사용자는 댓글 조회, 작성, 수정, 삭제가 가능하며, 고민과 댓글에 대해 좋아요 버튼을 누를 수 있습니다.

<img width="1889" height="944" alt="상세조회와좋아요" src="https://github.com/user-attachments/assets/2f8598ba-3ed1-42ee-af32-8565499912c9" />


#### **관리자 페이지**


<img width="1895" height="985" alt="관리자" src="https://github.com/user-attachments/assets/34fc5568-d5fa-465b-9fd8-9c46d27a4736" />

