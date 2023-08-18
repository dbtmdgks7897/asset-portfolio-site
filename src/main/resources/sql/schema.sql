CREATE TABLE `user` (
  `idx` bigint(20) NOT NULL AUTO_INCREMENT,
  `email` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `nickname` varchar(255) NOT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `profile_img` mediumtext DEFAULT NULL,
  `img_type` varchar(255) DEFAULT 'png',
  `gender` varchar(255) DEFAULT NULL,
  `age` int(11) DEFAULT NULL,
  `suspend_until` datetime(6) DEFAULT NULL,
  `suspend_reason` varchar(255) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime(6) DEFAULT NULL,
  `deleted_at` datetime(6) DEFAULT NULL,
  `deleted_reason` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`idx`),
  UNIQUE KEY `UK_oso07pudw19e66bs4yp8hwpux` (`email`)
);

CREATE TABLE `roles` (
  `idx` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`idx`)
);

CREATE TABLE `user_roles` (
  `user_idx` bigint(20) NOT NULL,
  `roles_idx` int(11) NOT NULL,
  KEY `FKa34w5lball82mrl5kcbkimlty` (`roles_idx`),
  KEY `FKouit3ld601idg73wq2uiwhob1` (`user_idx`),
  CONSTRAINT `FKa34w5lball82mrl5kcbkimlty` FOREIGN KEY (`roles_idx`) REFERENCES `roles` (`idx`),
  CONSTRAINT `FKouit3ld601idg73wq2uiwhob1` FOREIGN KEY (`user_idx`) REFERENCES `user` (`idx`)
);

CREATE TABLE `authority` (
  `idx` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`idx`)
);

CREATE TABLE `roles_authority` (
  `roles_idx` int(11) NOT NULL,
  `authority_idx` int(11) NOT NULL,
  KEY `FK3pdwso14ne1eydojbwenu3vth` (`authority_idx`),
  KEY `FK2a3gx8057kcjls5qlpg9nem59` (`roles_idx`),
  CONSTRAINT `FK2a3gx8057kcjls5qlpg9nem59` FOREIGN KEY (`roles_idx`) REFERENCES `roles` (`idx`),
  CONSTRAINT `FK3pdwso14ne1eydojbwenu3vth` FOREIGN KEY (`authority_idx`) REFERENCES `authority` (`idx`)
);

CREATE TABLE `board` (
  `idx` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `content` varchar(255) NOT NULL,
  `user_idx` bigint(20) NOT NULL,
  `view_count` int(11) DEFAULT 0,
  `recommend_count` int(11) DEFAULT 0,
  `is_hided` bit(1) DEFAULT FALSE,
  `created_at` datetime(6) DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime(6) DEFAULT NULL,
  `deleted_at` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`idx`),
  KEY `FKn5hq5nlk75iglopl215y5jt83` (`user_idx`),
  CONSTRAINT `FKn5hq5nlk75iglopl215y5jt83` FOREIGN KEY (`user_idx`) REFERENCES `user` (`idx`)
);

CREATE TABLE `board_recommend` (
  `board_idx` bigint(20) NOT NULL,
  `user_idx` bigint(20) NOT NULL,
  `created_at` datetime(6) DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`board_idx`,`user_idx`),
  UNIQUE KEY `UK_pnpsf6yp0upi1n76wwy5rni4m` (`board_idx`),
  UNIQUE KEY `UK_qdmpw0ku3o96tlcrx36x70oju` (`user_idx`)
);

CREATE TABLE `board_report` (
  `board_idx` bigint(20) NOT NULL,
  `user_idx` bigint(20) NOT NULL,
  `reason` varchar(255) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`board_idx`,`user_idx`),
  UNIQUE KEY `UK_iatkx85gmyris8uaf11fj6imj` (`board_idx`),
  UNIQUE KEY `UK_lu6ahlpfrdq2i8f355wltfmcj` (`user_idx`)
);

CREATE TABLE `comment` (
  `idx` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_idx` bigint(20) NOT NULL,
  `board_idx` bigint(20) NOT NULL,
  `content` varchar(255) NOT NULL,
  `recommend_count` int(11) DEFAULT 0,
  `created_at` datetime(6) DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime(6) DEFAULT NULL,
  `deleted_at` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`idx`),
  KEY `FK18qu651b4scnktwvkstxhmoqb` (`board_idx`),
  KEY `FKbbasm0l7q1tn8t973k41wih81` (`user_idx`),
  CONSTRAINT `FK18qu651b4scnktwvkstxhmoqb` FOREIGN KEY (`board_idx`) REFERENCES `board` (`idx`),
  CONSTRAINT `FKbbasm0l7q1tn8t973k41wih81` FOREIGN KEY (`user_idx`) REFERENCES `user` (`idx`)
);

CREATE TABLE `comment_recommend` (
  `comment_idx` bigint(20) NOT NULL,
  `user_idx` bigint(20) NOT NULL,
  `created_at` datetime(6) DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`comment_idx`,`user_idx`),
  UNIQUE KEY `UK_t15v8uorx3bd22nti8jyh5dtk` (`comment_idx`),
  UNIQUE KEY `UK_htalek5fs3yaao596k8onmg2w` (`user_idx`)
);

CREATE TABLE `comment_report` (
  `comment_idx` bigint(20) NOT NULL,
  `user_idx` bigint(20) NOT NULL,
  `reason` varchar(255) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`comment_idx`,`user_idx`),
  UNIQUE KEY `UK_q89mjy6scoeosvxypj6eq7yk5` (`comment_idx`),
  UNIQUE KEY `UK_pbdrmoxp7ft201eho3wv3nvc1` (`user_idx`)
);

CREATE TABLE `portfolio` (
  `idx` int(11) NOT NULL AUTO_INCREMENT,
  `user_idx` bigint(20) NOT NULL,
  `name` varchar(255) NOT NULL,
  `description` varchar(255) NOT NULL,
  `created_at` datetime(6) DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime(6) DEFAULT NULL,
  `deleted_at` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`idx`),
  KEY `FKoc460dqn3joolauy9mji0y3ri` (`user_idx`),
  CONSTRAINT `FKoc460dqn3joolauy9mji0y3ri` FOREIGN KEY (`user_idx`) REFERENCES `user` (`idx`)
);

CREATE TABLE `asset_type` (
  `idx` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`idx`)
);

CREATE TABLE `asset` (
  `idx` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `type_idx` int(11) NOT NULL,
  `price` decimal(38,2) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`idx`),
  KEY `FK4hv6qmuvab3mtrgj8kh4a2tp4` (`type_idx`),
  CONSTRAINT `FK4hv6qmuvab3mtrgj8kh4a2tp4` FOREIGN KEY (`type_idx`) REFERENCES `asset_type` (`idx`)
);

CREATE TABLE `portfolio_detail` (
  `idx` int(11) NOT NULL AUTO_INCREMENT,
  `portfolio_idx` int(11) NOT NULL,
  `asset_idx` varchar(255) NOT NULL,
  `amount` decimal(38,2) DEFAULT NULL,
  `average_purchase_price` decimal(38,2) DEFAULT NULL,
  `total_purchase_price` decimal(38,2) DEFAULT NULL,
  `dividend_month` varchar(255) DEFAULT NULL,
  `dividend_amount` decimal(38,2) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime(6) DEFAULT NULL,
  `deleted_at` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`idx`),
  KEY `FK163wcng8f59mp63miqd301uxr` (`asset_idx`),
  KEY `FKb0bbxnkngybhej819o5cutbry` (`portfolio_idx`),
  CONSTRAINT `FK163wcng8f59mp63miqd301uxr` FOREIGN KEY (`asset_idx`) REFERENCES `asset` (`idx`),
  CONSTRAINT `FKb0bbxnkngybhej819o5cutbry` FOREIGN KEY (`portfolio_idx`) REFERENCES `portfolio` (`idx`)
);

CREATE TABLE `bookmark` (
  `idx` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_idx` bigint(20) NOT NULL,
  `asset_idx` varchar(255) NOT NULL,
  PRIMARY KEY (`idx`),
  KEY `FKqs46o7wlpjitfs0mv7f3ibhe3` (`asset_idx`),
  KEY `FK75f0lkcn5n9k221v58krwqqth` (`user_idx`),
  CONSTRAINT `FK75f0lkcn5n9k221v58krwqqth` FOREIGN KEY (`user_idx`) REFERENCES `user` (`idx`),
  CONSTRAINT `FKqs46o7wlpjitfs0mv7f3ibhe3` FOREIGN KEY (`asset_idx`) REFERENCES `asset` (`idx`)
);

CREATE TABLE `transaction` (
  `idx` int(11) NOT NULL AUTO_INCREMENT,
  `user_idx` bigint(20) NOT NULL,
  `portfolio_idx` int(11) NOT NULL,
  `asset_idx` varchar(255) NOT NULL,
  `type` varchar(255) DEFAULT NULL,
  `price_avg` decimal(38,2) DEFAULT NULL,
  `amount` decimal(38,2) DEFAULT NULL,
  `profit` decimal(38,2) DEFAULT NULL,
  `transaction_date` datetime(6) DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`idx`),
  KEY `FKkl7usxxre8v2t6xi9idbynkif` (`asset_idx`),
  KEY `FKqmbqckxr2k9cuomeby1sff18j` (`portfolio_idx`),
  KEY `FKjyy45dbi4rto9mh49k2nir5mr` (`user_idx`),
  CONSTRAINT `FKjyy45dbi4rto9mh49k2nir5mr` FOREIGN KEY (`user_idx`) REFERENCES `user` (`idx`),
  CONSTRAINT `FKkl7usxxre8v2t6xi9idbynkif` FOREIGN KEY (`asset_idx`) REFERENCES `asset` (`idx`),
  CONSTRAINT `FKqmbqckxr2k9cuomeby1sff18j` FOREIGN KEY (`portfolio_idx`) REFERENCES `portfolio` (`idx`)
);

CREATE TABLE `audit_log` (
  `idx` bigint(20) NOT NULL AUTO_INCREMENT,
  `table_name` varchar(255) NOT NULL,
  `user_idx` bigint(20) NOT NULL,
  `row_id` bigint(20) DEFAULT NULL,
  `operation` varchar(255) NOT NULL,
  `column_name` varchar(255) DEFAULT NULL,
  `old_value` varchar(255) DEFAULT NULL,
  `new_value` varchar(255) DEFAULT NULL,
  `reason` varchar(255) DEFAULT NULL,
  `created_at` varchar(255) DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`idx`)
);