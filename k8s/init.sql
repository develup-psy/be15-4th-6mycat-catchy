CREATE USER IF NOT EXISTS 'swcamp'@'%' IDENTIFIED BY 'swcamp';
GRANT ALL PRIVILEGES ON 6mycat.* TO 'swcamp'@'%';
FLUSH PRIVILEGES;

-- 기존 테이블 삭제
DROP TABLE IF EXISTS game_score;
DROP TABLE IF EXISTS game_image;
DROP TABLE IF EXISTS notification;
DROP TABLE IF EXISTS `like`;
DROP TABLE IF EXISTS comment;
DROP TABLE IF EXISTS feed_image;
DROP TABLE IF EXISTS feed;
DROP TABLE IF EXISTS block;
DROP TABLE IF EXISTS follow;
DROP TABLE IF EXISTS cat;
DROP TABLE IF EXISTS jjure;
DROP TABLE IF EXISTS member;

-- 멤버 테이블(member)
CREATE TABLE member (
                        id BIGINT NOT NULL AUTO_INCREMENT,
                        email VARCHAR(255) NOT NULL,
                        name VARCHAR(30) NULL,
                        social VARCHAR(10) NOT NULL,
                        contact_number VARCHAR(11) NOT NULL,
                        profile_image VARCHAR(255) NULL,
                        nickname VARCHAR(30) NOT NULL UNIQUE,
                        status_message VARCHAR(100) NULL,
                        created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        deleted_at TIMESTAMP NULL,
                        PRIMARY KEY (id)
);

-- 쭈르 테이블(jjure)
CREATE TABLE jjure (
                       id BIGINT NOT NULL AUTO_INCREMENT,
                       member_id bigint NOT NULL,
                       caption VARCHAR(100) NOT NULL,
                       file_key VARCHAR(255) NOT NULL,
                       thumbnail_url VARCHAR(500) NULL,
                       music_url VARCHAR(255) NULL,
                       created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       deleted_at TIMESTAMP NULL,
                       PRIMARY KEY (id),
                       constraint fk_jjure_member
                           foreign key (member_id) references member (id)
                               on delete cascade
);

-- 고양이 테이블(cat)
CREATE TABLE cat (
                     id BIGINT NOT NULL AUTO_INCREMENT,
                     member_id BIGINT NOT NULL,
                     name VARCHAR(30) NOT NULL,
                     gender CHAR(1) NOT NULL DEFAULT 'N',
                     breed VARCHAR(100),
                     birth_date DATE,
                     age INT,
                     created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                     updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                     deleted_at TIMESTAMP,
                     PRIMARY KEY (id),
                     FOREIGN KEY (member_id) REFERENCES member(id)
);

CREATE TABLE follow (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        follower_id BIGINT NOT NULL,
                        following_id BIGINT NOT NULL,
                        requested_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                        accepted_at TIMESTAMP NULL,
                        CONSTRAINT uq_follow UNIQUE (follower_id, following_id),
                        CONSTRAINT fk_follower FOREIGN KEY (follower_id) REFERENCES member(id),
                        CONSTRAINT fk_following FOREIGN KEY (following_id) REFERENCES member(id)
);

-- 차단 테이블(block)
CREATE TABLE block (
                       id BIGINT NOT NULL AUTO_INCREMENT,
                       blocker_id BIGINT NOT NULL,
                       blocked_id BIGINT NOT NULL,
                       blocked_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       PRIMARY KEY (id),
                       FOREIGN KEY (blocker_id) REFERENCES member(id),
                       FOREIGN KEY (blocked_id) REFERENCES member(id)
);

-- 피드 테이블(feed)
CREATE TABLE feed (
                      id BIGINT NOT NULL AUTO_INCREMENT,
                      member_id BIGINT NOT NULL,
                      content VARCHAR(500),
                      music_url VARCHAR(255),
                      created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                      updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                      deleted_at TIMESTAMP,
                      PRIMARY KEY (id),
                      FOREIGN KEY (member_id) REFERENCES member(id)
);

-- 피드 이미지 테이블(feed_image)
CREATE TABLE feed_image (
                            id BIGINT NOT NULL AUTO_INCREMENT,
                            feed_id BIGINT NOT NULL,
                            image_url VARCHAR(255) NOT NULL,
                            sequence INT NOT NULL DEFAULT 0,
                            PRIMARY KEY (id),
                            FOREIGN KEY (feed_id) REFERENCES feed(id)
);

-- 댓글 테이블(comment)
CREATE TABLE comment (
                         id BIGINT NOT NULL AUTO_INCREMENT,
                         member_id BIGINT NOT NULL,
                         target_id BIGINT NOT NULL,
                         target_type ENUM('JJURE', 'FEED') NOT NULL,
                         parent_comment_id BIGINT,
                         content VARCHAR(255),
                         created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                         PRIMARY KEY (id),
                         FOREIGN KEY (member_id) REFERENCES member(id),
                         FOREIGN KEY (parent_comment_id) REFERENCES comment(id)
);

-- 좋아요 테이블(like)
CREATE TABLE `like` (
                        id BIGINT NOT NULL AUTO_INCREMENT,
                        member_id BIGINT NOT NULL,
                        target_id BIGINT NOT NULL,
                        target_type ENUM('JJURE', 'FEED') NOT NULL,
                        liked_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        PRIMARY KEY (id),
                        FOREIGN KEY (member_id) REFERENCES member(id)
);

-- 알림 테이블(notification)
CREATE TABLE notification (
                              id BIGINT NOT NULL AUTO_INCREMENT,
                              member_id BIGINT NOT NULL, -- 알림을 받는 사람
                              sender_member_id BIGINT NOT NULL, -- 알림을 보내게 만든 주체
                              related_id BIGINT,
                              type VARCHAR(100) NOT NULL,
                              content VARCHAR(255),
                              created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                              PRIMARY KEY (id),
                              FOREIGN KEY (member_id) REFERENCES member(id),
                              FOREIGN KEY (sender_member_id) REFERENCES member(id)
);

CREATE TABLE game_score (
                            id BIGINT NOT NULL AUTO_INCREMENT,
                            member_id BIGINT NOT NULL,
                            score INT,
                            created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                            PRIMARY KEY (id),
                            CONSTRAINT uq_game_score_member UNIQUE (member_id),
                            CONSTRAINT fk_game_score_member FOREIGN KEY (member_id) REFERENCES member(id)
);