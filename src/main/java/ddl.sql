drop database TripToNDB;
create DATABASE TripToNDB;
use TripToNDB;
show tables;


-- 부모 테이블 먼저 생성 (의존성이 없는 테이블들)
-- 1. 회원 테이블
CREATE TABLE members (
                         member_id BIGINT NOT NULL AUTO_INCREMENT,
                         member_email VARCHAR(100) NOT NULL,
                         member_login_password VARCHAR(255) NOT NULL,
                         member_nickname VARCHAR(255) NOT NULL,
                         created_at DATETIME(6),
                         updated_at DATETIME(6),
                         deleted_at DATETIME(6),
                         PRIMARY KEY (member_id)
);

-- 2. 고민(게시글) 테이블
CREATE TABLE concerns (
                          concern_id BIGINT NOT NULL AUTO_INCREMENT,
                          concern_title VARCHAR(255) NOT NULL,
                          concern_content TEXT,
                          luggage_type ENUM ('a','b','c') NOT NULL,
                          member_id BIGINT NOT NULL,
                          created_at DATETIME(6),
                          updated_at DATETIME(6),
                          deleted_at DATETIME(6),
                          PRIMARY KEY (concern_id)
);

-- 3. AI 응답 테이블
CREATE TABLE responses (
                           response_id BIGINT NOT NULL AUTO_INCREMENT,
                           concern_id BIGINT NOT NULL,
                           response_content TEXT NOT NULL,
                           created_at DATETIME(6),
                           PRIMARY KEY (response_id)
);

-- 4. 댓글 테이블
CREATE TABLE comments (
                          comment_id BIGINT NOT NULL AUTO_INCREMENT,
                          comment_content TEXT NOT NULL,
                          concern_id BIGINT NOT NULL,
                          member_id BIGINT NOT NULL,
                          created_at DATETIME(6),
                          updated_at DATETIME(6),
                          deleted_at DATETIME(6),
                          PRIMARY KEY (comment_id)
);

-- 5. 고민 게시글 좋아요 테이블
CREATE TABLE concern_likes (
                               concern_like_id BIGINT NOT NULL AUTO_INCREMENT,
                               concern_id BIGINT NOT NULL,
                               member_id BIGINT NOT NULL,
                               created_at DATETIME(6) NOT NULL,
                               PRIMARY KEY (concern_like_id)
);

-- 6. 댓글 좋아요 테이블
CREATE TABLE comment_likes (
                               comment_like_id BIGINT NOT NULL AUTO_INCREMENT,
                               comment_id BIGINT NOT NULL,
                               member_id BIGINT NOT NULL,
                               created_at DATETIME(6),
                               PRIMARY KEY (comment_like_id)
);

-- 7. 회원 로그인 로그 테이블
CREATE TABLE member_login_logs (
                                   member_login_log_id BIGINT NOT NULL AUTO_INCREMENT,
                                   login_try_id VARCHAR(255),
                                   login_member_nickname VARCHAR(255),
	                                   login_status BIT,
	                                   login_failure_reason VARCHAR(255),
	                                   login_try_ip VARCHAR(45),
	                                   created_at DATETIME(6),
	                                   PRIMARY KEY (member_login_log_id)
);

-- 8. 제미나이 API 요청 로그 테이블
CREATE TABLE gemini_api_request_logs (
                                         ai_response_log_id BIGINT NOT NULL AUTO_INCREMENT,
                                         request_member_id BIGINT,
                                         request_member_nickname VARCHAR(255),
                                         request_status BIT,
                                         request_failure_reason VARCHAR(255),
                                         created_at DATETIME(6),
                                         PRIMARY KEY (ai_response_log_id)
);


-- 유니크 제약조건
-- 회원 이메일, 닉네임 중복 방지
ALTER TABLE members ADD CONSTRAINT UK_MEMBER_EMAIL UNIQUE (member_email);
ALTER TABLE members ADD CONSTRAINT UK_MEMBER_NICKNAME UNIQUE (member_nickname);

-- 하나의 고민글에는 하나의 AI 응답만 존재 (1:1 관계)
ALTER TABLE responses ADD CONSTRAINT UK_RESPONSE_CONCERN UNIQUE (concern_id);

-- 좋아요 중복 방지 (한 유저가 같은 글/댓글에 한 번만 좋아요 가능)
ALTER TABLE concern_likes ADD CONSTRAINT UQ_CONCERN_LIKE UNIQUE (member_id, concern_id);
ALTER TABLE comment_likes ADD CONSTRAINT UQ_COMMENT_LIKE UNIQUE (member_id, comment_id);


-- 외래키 제약조건
-- 고민글(concerns) -> 작성자(members)
ALTER TABLE concerns ADD CONSTRAINT fk_concerns_member_id FOREIGN KEY (member_id) REFERENCES members (member_id);

-- 댓글(comments) -> 고민글(concerns), 작성자(members)
ALTER TABLE comments ADD CONSTRAINT fk_comments_concern_id FOREIGN KEY (concern_id) REFERENCES concerns (concern_id);
ALTER TABLE comments ADD CONSTRAINT fk_comments_member_id FOREIGN KEY (member_id) REFERENCES members (member_id);

-- AI 응답(responses) -> 고민글(concerns)
ALTER TABLE responses ADD CONSTRAINT fk_responses_concern_id FOREIGN KEY (concern_id) REFERENCES concerns (concern_id);

-- 고민글 좋아요(concern_likes) -> 고민글(concerns), 회원(members)
ALTER TABLE concern_likes ADD CONSTRAINT fk_concern_likes_concern_id FOREIGN KEY (concern_id) REFERENCES concerns (concern_id);
ALTER TABLE concern_likes ADD CONSTRAINT fk_concern_likes_member_id FOREIGN KEY (member_id) REFERENCES members (member_id);

-- 댓글 좋아요(comment_likes) -> 댓글(comments), 회원(members)
ALTER TABLE comment_likes ADD CONSTRAINT fk_comment_likes_comment_id FOREIGN KEY (comment_id) REFERENCES comments (comment_id);
ALTER TABLE comment_likes ADD CONSTRAINT fk_comment_likes_member_id FOREIGN KEY (member_id) REFERENCES members (member_id);


-- 인덱스 설정
-- 마이페이지 조회 시 4번의 쿼리 전달 -> 성능 고려
CREATE INDEX idx_concerns_member_deleted_created
    ON CONCERNS (member_id, deleted_at, created_at DESC);

CREATE INDEX idx_comments_member_paging
    ON COMMENTS (member_id, deleted_at, created_at DESC);

CREATE INDEX idx_concerns_deleted_created
    ON CONCERNS (deleted_at, created_at DESC);

CREATE INDEX idx_concerns_deleted_created
    ON CONCERNS (deleted_at, created_at DESC);
