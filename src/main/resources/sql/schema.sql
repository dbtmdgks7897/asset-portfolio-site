SET foreign_key_checks = 0; 

DROP TABLE IF EXISTS `user`;
DROP TABLE IF EXISTS `board`;
DROP TABLE IF EXISTS `comment`;
DROP TABLE IF EXISTS `comment_recommend`;
DROP TABLE IF EXISTS `board_recommend`;

SET foreign_key_checks = 1; 

CREATE TABLE `user` (
  `idx` INT PRIMARY KEY,
  `email` VARCHAR(255) UNIQUE NOT NULL,
  `password` VARCHAR(255) NOT NULL,
  `name` VARCHAR(100) NOT NULL,
  `nickname` VARCHAR(50) NOT NULL,
  `phone` VARCHAR(20),
  `profile_img` BLOB,
  `gender` VARCHAR(10),
  `age` INT,
  `suspend_until` DATETIME,
  `suspend_reason` VARCHAR(255),
  `created_at` DATETIME DEFAULT (CURRENT_TIMESTAMP),
  `updated_at` DATETIME,
  `deleted_at` DATETIME,
  `deleted_reason` VARCHAR(255)
);

CREATE TABLE board (
    `idx` INT PRIMARY KEY,
    `name` VARCHAR(100) NOT NULL,
    `content` VARCHAR(1000) NOT NULL,
    `user_idx` INT,
    `view_count` INT DEFAULT 0,
    `recommend_count` INT DEFAULT 0,
    `isHide` BOOL,
    `created_at` DATETIME DEFAULT (CURRENT_TIMESTAMP),
    `updated_at` DATETIME,
    `deleted_at` DATETIME
);

CREATE TABLE `board_recommend` (
  `idx` INT PRIMARY KEY AUTO_INCREMENT,
  `board_idx` INT NOT NULL,
  `user_idx` INT NOT NULL,
  `created_at` DATETIME DEFAULT (CURRENT_TIMESTAMP)
);

CREATE TABLE `comment` (
  `idx` INT PRIMARY KEY AUTO_INCREMENT,
  `user_idx` INT,
  `board_idx` INT,
  `content` TEXT,
  `recommend_count` INT DEFAULT 0,
  `created_at` DATETIME DEFAULT (CURRENT_TIMESTAMP),
  `updated_at` DATETIME,
  `deleted_at` DATETIME
);

CREATE TABLE `comment_recommend` (
  `idx` INT PRIMARY KEY AUTO_INCREMENT,
  `comment_idx` INT NOT NULL,
  `user_idx` INT NOT NULL,
  `created_at` DATETIME DEFAULT (CURRENT_TIMESTAMP)
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

