SET foreign_key_checks = 0; 
DROP TABLE IF EXISTS `user`;
DROP TABLE IF EXISTS `board`;
DROP TABLE IF EXISTS `comment`;
DROP TABLE IF EXISTS `comment_recommend`;
DROP TABLE IF EXISTS `board_recommend`;
DROP TABLE IF EXISTS `roles`;
DROP TABLE IF EXISTS `authority`;
DROP TABLE IF EXISTS `user_roles`;
DROP TABLE IF EXISTS `roles_authority`;
DROP TABLE IF EXISTS `audit_log`;
DROP TABLE IF EXISTS `comment_report`;
DROP TABLE IF EXISTS `board_report`;
DROP TABLE IF EXISTS `portfolio`;
DROP TABLE IF EXISTS `portfolio_detail`;
DROP TABLE IF EXISTS `bookmark`;
DROP TABLE IF EXISTS `transaction`;
DROP TABLE IF EXISTS `asset`;
SET foreign_key_checks = 1; 

CREATE TABLE `user` (
  `idx` BIGINT PRIMARY KEY AUTO_INCREMENT,
  `email` VARCHAR(255) UNIQUE NOT NULL,
  `password` VARCHAR(255) NOT NULL,
  `name` VARCHAR(100) NOT NULL,
  `nickname` VARCHAR(50) NOT NULL,
  `phone` VARCHAR(20),
  `profile_img` LONGTEXT,
  `img_type` VARCHAR(30),
  `gender` VARCHAR(10),
  `age` INT,
  `suspend_until` DATETIME,
  `suspend_reason` VARCHAR(255),
  `created_at` DATETIME DEFAULT (CURRENT_TIMESTAMP),
  `updated_at` DATETIME,
  `deleted_at` DATETIME,
  `deleted_reason` VARCHAR(255)
);

CREATE TABLE `roles` (
    `idx` INT PRIMARY KEY AUTO_INCREMENT,
    `name` VARCHAR(50) NOT NULL
);

CREATE TABLE `authority` (
    `idx` INT PRIMARY KEY AUTO_INCREMENT,
    `name` VARCHAR(50) NOT NULL
);

CREATE TABLE `user_roles` (
    `user_idx` BIGINT NOT NULL,
    `roles_idx` INT NOT NULL,
    PRIMARY KEY (`user_idx`, `roles_idx`)
);

CREATE TABLE `roles_authority` (
    `roles_idx` INT NOT NULL,
    `authority_idx` INT NOT NULL,
    PRIMARY KEY (`roles_idx`, `authority_idx`)
);

CREATE TABLE board (
    `idx` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `name` VARCHAR(100) NOT NULL,
    `content` VARCHAR(1000) NOT NULL,
    `user_idx` BIGINT NOT NULL,
    `view_count` INT DEFAULT 0,
    `recommend_count` INT DEFAULT 0,
    `is_hided` BOOL DEFAULT FALSE,
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME,
    `deleted_at` DATETIME
);

CREATE TABLE `board_recommend` (
  `board_idx` BIGINT NOT NULL,
  `user_idx` BIGINT NOT NULL,
  `created_at` DATETIME DEFAULT (CURRENT_TIMESTAMP),
  PRIMARY KEY (board_idx, user_idx)
);

-- board_report 테이블 생성
CREATE TABLE `board_report` (
  `board_idx` BIGINT NOT NULL,
  `user_idx` BIGINT NOT NULL,
  `reason` VARCHAR(255),
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (board_idx, user_idx)
);

CREATE TABLE `comment` (
  `idx` BIGINT PRIMARY KEY AUTO_INCREMENT,
  `user_idx` BIGINT,
  `board_idx` BIGINT,
  `content` TEXT NOT NULL,
  `recommend_count` INT DEFAULT 0,
  `created_at` DATETIME DEFAULT (CURRENT_TIMESTAMP),
  `updated_at` DATETIME,
  `deleted_at` DATETIME
);

CREATE TABLE `comment_recommend` (
  `comment_idx` BIGINT NOT NULL,
  `user_idx` BIGINT NOT NULL,
  `created_at` DATETIME DEFAULT (CURRENT_TIMESTAMP),
  PRIMARY KEY (comment_idx, user_idx)
);

-- comment_report 테이블 생성
CREATE TABLE `comment_report` (
  `comment_idx` BIGINT NOT NULL,
  `user_idx` BIGINT NOT NULL,
  `reason` VARCHAR(255),
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (comment_idx, user_idx)
);

-- 포트폴리오(Portfolio) 테이블
CREATE TABLE `portfolio` (
    `idx` INT PRIMARY KEY AUTO_INCREMENT,
    `user_idx` BIGINT NOT NULL,
    `name` VARCHAR(100) NOT NULL,
    `description` VARCHAR(500),
    `created_at` DATETIME DEFAULT (CURRENT_TIMESTAMP),
    `updated_at` DATETIME,
    `deleted_at` DATETIME
);

-- 포트폴리오 디테일(PortfolioDetail) 테이블
CREATE TABLE `portfolio_detail` (
    `idx` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `portfolio_idx` INT,
    `asset_idx` VARCHAR(20),
    `amount` DECIMAL(18,2),
    `average_purchase_price` DECIMAL(18,2),
    `total_purchase_price` DECIMAL(18,2),
    `dividend_month` VARCHAR(20),
    `dividend_amount` DECIMAL(18,2),
    `created_at` DATETIME DEFAULT (CURRENT_TIMESTAMP),
    `updated_at` DATETIME,
    `deleted_at` DATETIME
);

-- 자산(Asset) 테이블
CREATE TABLE `asset` (
    `idx` VARCHAR(20) PRIMARY KEY,
    `name` VARCHAR(100) NOT NULL,
    `type` VARCHAR(50) NOT NULL
);

-- 거래(Transaction) 테이블
CREATE TABLE `transaction` (
    `idx` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `user_idx` BIGINT,
    `asset_idx` VARCHAR(20),
    `type` VARCHAR(10),
    `amount` DECIMAL(18,2),
    `price_avg` DECIMAL(18,2),
    `profit` DECIMAL(18,2),
    `transaction_date` DATETIME
);

-- 북마크(Bookmark) 테이블
CREATE TABLE `bookmark` (
    `idx` INT PRIMARY KEY AUTO_INCREMENT,
    `user_idx` BIGINT,
    `asset_idx` VARCHAR(20)
);


CREATE TABLE `audit_log` (
    `idx` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `table_name` VARCHAR(100) NOT NULL,
    `user_idx` VARCHAR(100) NOT NULL,
    `row_id` INT,
    `operation` VARCHAR(10) NOT NULL,
    `column_name` VARCHAR(100),
    `old_value` VARCHAR(255),
    `new_value` VARCHAR(255),
    `reason` VARCHAR(255),
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX `idx_board_user_idx` ON `board` (`user_idx`);

CREATE INDEX `idx_board_recommend_board_idx` ON `board_recommend` (`board_idx`);

CREATE INDEX `idx_board_recommend_user_idx` ON `board_recommend` (`user_idx`);

CREATE INDEX `idx_comment_user_idx` ON `comment` (`user_idx`);

CREATE INDEX `idx_comment_board_idx` ON `comment` (`board_idx`);

CREATE INDEX `idx_comment_recommend_comment_idx` ON `comment_recommend` (`comment_idx`);

CREATE INDEX `idx_comment_recommend_user_idx` ON `comment_recommend` (`user_idx`);

ALTER TABLE `board` ADD CONSTRAINT `fk_board_user` FOREIGN KEY (`user_idx`) REFERENCES `user` (`idx`) ON DELETE CASCADE;
ALTER TABLE `board_recommend` ADD CONSTRAINT `fk_board_recommend_board` FOREIGN KEY (`board_idx`) REFERENCES `board` (`idx`) ON DELETE CASCADE;
ALTER TABLE `board_recommend` ADD CONSTRAINT `fk_board_recommend_user` FOREIGN KEY (`user_idx`) REFERENCES `user` (`idx`) ON DELETE CASCADE;
ALTER TABLE `comment` ADD CONSTRAINT `fk_comment_user` FOREIGN KEY (`user_idx`) REFERENCES `user` (`idx`) ON DELETE CASCADE;
ALTER TABLE `comment` ADD CONSTRAINT `fk_comment_board` FOREIGN KEY (`board_idx`) REFERENCES `board` (`idx`) ON DELETE CASCADE;
ALTER TABLE `comment_recommend` ADD CONSTRAINT `fk_comment_recommend_comment` FOREIGN KEY (`comment_idx`) REFERENCES `comment` (`idx`) ON DELETE CASCADE;
ALTER TABLE `comment_recommend` ADD CONSTRAINT `fk_comment_recommend_user` FOREIGN KEY (`user_idx`) REFERENCES `user` (`idx`) ON DELETE CASCADE;
ALTER TABLE `user_roles` ADD CONSTRAINT `fk_user_roles_user` FOREIGN KEY (`user_idx`) REFERENCES `user` (`idx`) ON DELETE CASCADE;
ALTER TABLE `user_roles` ADD CONSTRAINT `fk_user_roles_roles` FOREIGN KEY (`roles_idx`) REFERENCES `roles` (`idx`) ON DELETE CASCADE;
ALTER TABLE `roles_authority` ADD CONSTRAINT `fk_roles_authority_roles` FOREIGN KEY (`roles_idx`) REFERENCES `roles` (`idx`) ON DELETE CASCADE;
ALTER TABLE `roles_authority` ADD CONSTRAINT `fk_roles_authority_authority` FOREIGN KEY (`authority_idx`) REFERENCES `authority` (`idx`) ON DELETE CASCADE;
ALTER TABLE `board_report` ADD CONSTRAINT `fk_board_report_board` FOREIGN KEY (`board_idx`) REFERENCES `board` (`idx`) ON DELETE CASCADE;
ALTER TABLE `board_report` ADD CONSTRAINT `fk_board_report_user` FOREIGN KEY (`user_idx`) REFERENCES `user` (`idx`) ON DELETE CASCADE;
ALTER TABLE `comment_report` ADD CONSTRAINT `fk_comment_report_comment` FOREIGN KEY (`comment_idx`) REFERENCES `comment` (`idx`) ON DELETE CASCADE;
ALTER TABLE `comment_report` ADD CONSTRAINT `fk_comment_report_user` FOREIGN KEY (`user_idx`) REFERENCES `user` (`idx`) ON DELETE CASCADE;
ALTER TABLE `portfolio` ADD FOREIGN KEY (`user_idx`) REFERENCES `user` (`idx`) ON DELETE CASCADE;
ALTER TABLE `portfolio_detail` ADD FOREIGN KEY (`portfolio_idx`) REFERENCES `portfolio` (`idx`) ON DELETE CASCADE;
ALTER TABLE `portfolio_detail` ADD FOREIGN KEY (`asset_idx`) REFERENCES `asset` (`idx`);
ALTER TABLE `transaction` ADD FOREIGN KEY (`user_idx`) REFERENCES `user` (`idx`) ON DELETE CASCADE;
ALTER TABLE `transaction` ADD FOREIGN KEY (`asset_idx`) REFERENCES `asset` (`idx`);
ALTER TABLE `bookmark` ADD FOREIGN KEY (`user_idx`) REFERENCES `user` (`idx`) ON DELETE CASCADE;
ALTER TABLE `bookmark` ADD FOREIGN KEY (`asset_idx`) REFERENCES `asset` (`idx`);