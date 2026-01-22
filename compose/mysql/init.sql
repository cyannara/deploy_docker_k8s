-- DB와 테이블 모두 UTF-8 설정
CREATE DATABASE IF NOT EXISTS edudb
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_general_ci;

USE edudb;

-- 테이블 생성
CREATE TABLE board (
    bno BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    content TEXT NOT NULL,
    writer VARCHAR(50) NOT NULL,
    regdate DATETIME DEFAULT CURRENT_TIMESTAMP
) DEFAULT CHARSET=utf8mb4
  COLLATE utf8mb4_general_ci;

-- 샘플 데이터 삽입
INSERT INTO board (title, content, writer, regdate) VALUES
('첫 번째 글', '첫 번째 게시물 내용입니다.', '홍길동', NOW()),
('두 번째 글', '두 번째 게시물 내용입니다.', '김철수', NOW()),
('세 번째 글', '세 번째 게시물 내용입니다.', '이영희', NOW()),
('네 번째 글', '네 번째 게시물 내용입니다.', '박민수', NOW()),
('다섯 번째 글', '다섯 번째 게시물 내용입니다.', '최지훈', NOW()),
('여섯 번째 글', '여섯 번째 게시물 내용입니다.', '한유정', NOW()),
('일곱 번째 글', '일곱 번째 게시물 내용입니다.', '정민호', NOW()),
('여덟 번째 글', '여덟 번째 게시물 내용입니다.', '오세훈', NOW()),
('아홉 번째 글', '아홉 번째 게시물 내용입니다.', '서지수', NOW()),
('열 번째 글', '열 번째 게시물 내용입니다.', '백승현', NOW()),
('열한 번째 글', '열한 번째 게시물 내용입니다.', '유재석', NOW()),
('열두 번째 글', '열두 번째 게시물 내용입니다.', '강호동', NOW());