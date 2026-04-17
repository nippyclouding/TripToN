# TRIPTON

**고민을 가방에 담아 AI 상담사에게 맡기는 서비스**

사용자가 자신의 고민을 가방 유형과 함께 제출하면 Google Gemini AI가 맞춤 조언을 생성해줍니다.
결과 페이지에서 다른 사용자들의 가방(고민)도 볼 수 있으며, 고민 좋아요, 댓글 추가 & 수정 & 삭제 및 좋아요 버튼을 누를 수 있습니다.

---

<img width="693" height="605" alt="github1" src="https://github.com/user-attachments/assets/7082a6ce-83e2-4a00-8a9f-0441bdb78f28" />

---

### 개발 과정 & 사용 기술

**개발 기간**
- 2개월 (2025.08.~2025.09.)

**개발 인원**
- 2명 (디자이너 1명, 개발자 1명)

**개발 환경**
- **Language & DB**: Java 21, MariaDB
- **Framework**: Spring Boot 3 with JPA (Spring Data JPA)
- **Auth**: Session & Cookie
- **Frontend**: Javascript, Thymeleaf (Server Side Rendering)
- **Infrastructure**: AWS EC2 & Docker Container
  - Spring Boot Container
  - MariaDB Container

---

### 1. ERD

<img width="715" height="593" alt="github3" src="https://github.com/user-attachments/assets/153a9796-0d0f-4376-947d-35d304ff755d" />



---

### 2. SYSTEM ARCHITECTURE

<img width="518" height="463" alt="github2" src="https://github.com/user-attachments/assets/8b352a95-0548-465f-94c7-07b58ed490e6" />


---

### 3. 주요 기능

#### **가방 종류 선택**
사용자는 로그인 후 고민을 담을 가방을 총 3가지 중 하나 선택할 수 있습니다. 

<img width="952" height="579" alt="github4" src="https://github.com/user-attachments/assets/b7f992d0-f37f-4a57-97d1-ab18cf1dbfdc" />


#### **고민 입력**
사용자는 자신의 고민을 입력창에 입력할 수 있습니다.

<img width="585" height="549" alt="github5" src="https://github.com/user-attachments/assets/d21bf279-898e-431e-ab1e-5175b2999461" />

#### **AI 고민 응답**
Gemini AI에게 고민을 전송하여 응답을 사용자에게 전달합니다.

<img width="631" height="566" alt="github6" src="https://github.com/user-attachments/assets/f6ec5d39-03ce-4ce4-a414-eca1ce9aebbf" />


#### **고민 게시글 전체 조회**
사용자는 전체 고민들을 조회할 수 있습니다.

<img width="461" height="483" alt="github7" src="https://github.com/user-attachments/assets/5fc9c0c8-9715-44a3-9752-8eb040959e9b" />


#### **고민 게시글 상세 조회**
사용자는 각 고민들을 조회할 수 있습니다.
사용자는 댓글 조회, 작성, 수정, 삭제가 가능하며, 좋아요 버튼을 누를 수 있습니다.

<img width="1253" height="568" alt="github9" src="https://github.com/user-attachments/assets/5704fad4-4251-4b3f-ac5d-0840afb4fcb0" />

