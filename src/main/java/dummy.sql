

-- 더미 데이터
INSERT INTO members (member_email, member_login_password, member_nickname, created_at, updated_at)
WITH RECURSIVE cte (n) AS (
    SELECT 1
    UNION ALL
    SELECT n + 1 FROM cte WHERE n < 1000
)
SELECT
    CONCAT('user', n, '@tripton.com'),
    '$2a$10$dummyHashPassword1234567890', -- 더미 암호화 비밀번호
    CONCAT('여행자_', n),
    NOW() - INTERVAL FLOOR(RAND() * 365) DAY, -- 최근 1년 사이 랜덤 가입일
    NOW()
FROM cte;


INSERT INTO concerns (concern_title, concern_content, luggage_type, member_id, created_at, updated_at)
WITH RECURSIVE cte (n) AS (
    SELECT 1
    UNION ALL
    SELECT n + 1 FROM cte WHERE n < 1000
)
SELECT
    CONCAT('여행 고민이 있습니다 - ', n),
    CONCAT('이번에 여행을 가는데 어떤 것을 준비해야 할지 고민입니다. (내용 ', n, ')'),
    ELT(FLOOR(1 + (RAND() * 3)), 'a', 'b', 'c'),
    FLOOR(1 + (RAND() * 1000)), -- 1~1000번 유저 랜덤 매핑
    NOW() - INTERVAL FLOOR(RAND() * 365) DAY,
    NOW()
FROM cte;


INSERT INTO responses (concern_id, response_content, created_at)
WITH RECURSIVE cte (n) AS (
    SELECT 1
    UNION ALL
    SELECT n + 1 FROM cte WHERE n < 1000
)
SELECT
    n, -- 1번 고민글부터 1000번 고민글까지 1:1 매핑
    CONCAT('안녕하세요! 제미나이입니다. 고객님의 고민에 대한 답변입니다. (답변 ', n, ')'),
    NOW() - INTERVAL FLOOR(RAND() * 300) DAY
FROM cte;

INSERT INTO comments (comment_content, concern_id, member_id, created_at, updated_at)
WITH RECURSIVE cte (n) AS (
    SELECT 1
    UNION ALL
    SELECT n + 1 FROM cte WHERE n < 1000
)
SELECT
    CONCAT('저도 비슷한 고민을 했었어요! 도움이 되시길 바랍니다. (댓글 ', n, ')'),
    FLOOR(1 + (RAND() * 1000)), -- 랜덤 고민글
    FLOOR(1 + (RAND() * 1000)), -- 랜덤 유저
    NOW() - INTERVAL FLOOR(RAND() * 365) DAY,
    NOW()
FROM cte;

INSERT INTO concern_likes (concern_id, member_id, created_at)
WITH RECURSIVE cte (n) AS (
    SELECT 1
    UNION ALL
    SELECT n + 1 FROM cte WHERE n < 1000
)
SELECT
    (n MOD 1000) + 1,  -- 고민글 ID (순환)
    n,                 -- 유저 ID (1~1000, 겹치지 않음 보장)
    NOW() - INTERVAL FLOOR(RAND() * 365) DAY
FROM cte;

INSERT INTO comment_likes (comment_id, member_id, created_at)
WITH RECURSIVE cte (n) AS (
    SELECT 1
    UNION ALL
    SELECT n + 1 FROM cte WHERE n < 1000
)
SELECT
    FLOOR(1 + ((n * 7) MOD 1000)), -- 좀 더 랜덤하게 퍼지도록 소수(7) 곱셈 활용
    n,
    NOW() - INTERVAL FLOOR(RAND() * 365) DAY
FROM cte;

INSERT INTO member_login_logs (login_try_id, login_member_nickname, login_status, login_failure_reason, created_at)
WITH RECURSIVE cte (n) AS (
    SELECT 1
    UNION ALL
    SELECT n + 1 FROM cte WHERE n < 1000
)
SELECT
    CONCAT('user', FLOOR(1 + (RAND() * 1000)), '@tripton.com'),
    CONCAT('여행자_', FLOOR(1 + (RAND() * 1000))),
    n MOD 2, -- 0(실패) 또는 1(성공)
    IF(n MOD 2 = 0, '비밀번호 불일치', NULL), -- 실패일 때만 사유 작성
    NOW() - INTERVAL FLOOR(RAND() * 365) DAY
FROM cte;

INSERT INTO gemini_api_request_logs (request_member_id, request_member_nickname, request_status, request_failure_reason, created_at)
WITH RECURSIVE cte (n) AS (
    SELECT 1
    UNION ALL
    SELECT n + 1 FROM cte WHERE n < 1000
)
SELECT
    FLOOR(1 + (RAND() * 1000)),
    CONCAT('여행자_', FLOOR(1 + (RAND() * 1000))),
    n MOD 2,
    IF(n MOD 2 = 0, 'Rate Limit (15 RPM) 초과', NULL),
    NOW() - INTERVAL FLOOR(RAND() * 365) DAY
FROM cte;
