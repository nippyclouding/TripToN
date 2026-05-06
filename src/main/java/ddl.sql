drop database TripToNDB;
create DATABASE TripToNDB;
use TripToNDB;
show tables;
-- 부모 테이블 먼저 생성 (의존성이 없는 테이블들)
CREATE TABLE `MEMBERS` (
                           `member_id`	            BIGINT	       NOT NULL AUTO_INCREMENT,
                           `member_login_password`	VARCHAR(255)   NOT NULL,
                           `member_email`	        VARCHAR(100)   NOT NULL,
                           `created_at`	        DATETIME	   NOT NULL DEFAULT NOW(),
                           `updated_at`	        DATETIME	   NULL,
                           `deleted_at`         DATETIME       NULL,
                           CONSTRAINT `PK_MEMBERS` PRIMARY KEY (`member_id`)
);



-- 자식 테이블 생성 (MEMBERS 등을 참조하는 테이블)
CREATE TABLE `CONCERNS` (
                            `concern_id`	  BIGINT	            NOT NULL AUTO_INCREMENT,
                            `member_id`	      BIGINT	            NOT NULL,
                            `concern_title`	  VARCHAR(255)	        NOT NULL,
                            `concern_content` TEXT	                NOT NULL,
                            `created_at`	  DATETIME	            NOT NULL DEFAULT NOW(),
                            `updated_at`	  DATETIME	            NULL,
                            `deleted_at`      DATETIME              NULL,
                            `is_locked`	      BOOLEAN	            NOT NULL DEFAULT FALSE,
                            `luggage_type`	  ENUM('a', 'b', 'c')	NOT NULL DEFAULT 'a',
                            CONSTRAINT `PK_CONCERNS` PRIMARY KEY (`concern_id`),
                            CONSTRAINT `FK_MEMBERS_TO_CONCERNS` FOREIGN KEY (`member_id`) REFERENCES `MEMBERS` (`member_id`)
);

CREATE TABLE `RESPONSES` (
                             `response_id`	      BIGINT	NOT NULL AUTO_INCREMENT,
                             `concern_id`	      BIGINT	NOT NULL,
                             `response_content`	  TEXT	    NOT NULL,
                             `created_at`	      DATETIME	NOT NULL DEFAULT NOW(),
                             CONSTRAINT `PK_RESPONSES` PRIMARY KEY (`response_id`),
                             CONSTRAINT `FK_CONCERNS_TO_RESPONSES` FOREIGN KEY (`concern_id`) REFERENCES `CONCERNS` (`concern_id`),
                             CONSTRAINT `UQ_RESPONSE_CONCERN` UNIQUE (`concern_id`)
);

CREATE TABLE `COMMENTS` (
                            `comment_id`	  BIGINT	NOT NULL AUTO_INCREMENT,
                            `member_id`	      BIGINT	NOT NULL,
                            `concern_id`	  BIGINT	NOT NULL,
                            `comment_content` TEXT	    NOT NULL,
                            `created_at`	  DATETIME	NOT NULL DEFAULT NOW(),
                            `updated_at`	  DATETIME	NULL,
                            `deleted_at`      DATETIME  NULL,
                            CONSTRAINT `PK_COMMENTS` PRIMARY KEY (`comment_id`),
                            CONSTRAINT `FK_MEMBERS_TO_COMMENTS` FOREIGN KEY (`member_id`) REFERENCES `MEMBERS` (`member_id`),
                            CONSTRAINT `FK_CONCERNS_TO_COMMENTS` FOREIGN KEY (`concern_id`) REFERENCES `CONCERNS` (`concern_id`)
);



-- 좋아요 테이블 (중복 클릭 방지 UNIQUE KEY 추가)
CREATE TABLE `CONCERN_LIKES` (
                                 `concern_like_id` BIGINT	NOT NULL AUTO_INCREMENT,
                                 `member_id`	      BIGINT	NOT NULL,
                                 `concern_id`	  BIGINT	NOT NULL,
                                 `created_at`	  DATETIME	NOT NULL DEFAULT NOW(),
                                 CONSTRAINT `PK_CONCERN_LIKES` PRIMARY KEY (`concern_like_id`),
                                 CONSTRAINT `FK_MEMBERS_TO_CL` FOREIGN KEY (`member_id`) REFERENCES `MEMBERS` (`member_id`),
                                 CONSTRAINT `FK_CONCERNS_TO_CL` FOREIGN KEY (`concern_id`) REFERENCES `CONCERNS` (`concern_id`),
                                 CONSTRAINT `UQ_CONCERN_LIKE` UNIQUE (`member_id`, `concern_id`) -- 한 사람이 한 글에 한 번만!
);

CREATE TABLE `COMMENT_LIKES` (
                                 `comment_like_id` BIGINT	NOT NULL AUTO_INCREMENT,
                                 `member_id`	      BIGINT	NOT NULL,
                                 `comment_id`	  BIGINT	NOT NULL,
                                 `created_at`	  DATETIME	NOT NULL DEFAULT NOW(),
                                 CONSTRAINT `PK_COMMENT_LIKES` PRIMARY KEY (`comment_like_id`),
                                 CONSTRAINT `FK_MEMBERS_TO_CML` FOREIGN KEY (`member_id`) REFERENCES `MEMBERS` (`member_id`),
                                 CONSTRAINT `FK_COMMENTS_TO_CML` FOREIGN KEY (`comment_id`) REFERENCES `COMMENTS` (`comment_id`),
                                 CONSTRAINT `UQ_COMMENT_LIKE` UNIQUE (`member_id`, `comment_id`) -- 한 사람이 한 댓글에 한 번만!
);