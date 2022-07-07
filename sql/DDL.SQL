/*
Created: 2022-07-06
Modified: 2022-07-07
Model: gabojago
Database: MySQL 5.5
*/

-- Create tables section -------------------------------------------------

-- Table user

CREATE TABLE `user`
(
    `user_id` Bigint NOT NULL,
    `email` Varchar(30) NOT NULL,
    `name` Varchar(20) NOT NULL,
    `gender` Int(11) NOT NULL,
    `age` Tinyint NOT NULL,
    `location` Varchar(255) DEFAULT 'Seoul',
    `profile_photo` Varchar(255)
)
;

ALTER TABLE `user` ADD PRIMARY KEY (`user_id`)
;

ALTER TABLE `user` ADD UNIQUE `이메일` (`email`)
;

-- Table post

CREATE TABLE `post`
(
    `post_id` Bigint NOT NULL AUTO_INCREMENT,
    `user_id` Bigint NOT NULL,
    `date` Date NOT NULL,
    `title` Varchar(50) NOT NULL,
    `context` Varchar(255) NOT NULL,
    `view_cnt` Int NOT NULL DEFAULT 0,
    `great_cnt` Int NOT NULL DEFAULT 0,
    PRIMARY KEY (`post_id`)
)
;

CREATE INDEX `IX_Relationship2` ON `post` (`user_id`)
;

-- Table comment

CREATE TABLE `comment`
(
    `comment_id` Bigint NOT NULL AUTO_INCREMENT,
    `user_id` Bigint NOT NULL,
    `post_id` Bigint NOT NULL,
    `date` Date NOT NULL,
    `context` Varchar(255) NOT NULL,
    PRIMARY KEY (`comment_id`)
)
;

CREATE INDEX `IX_Relationship1` ON `comment` (`user_id`)
;

CREATE INDEX `IX_Relationship3` ON `comment` (`post_id`)
;

-- Table spot

CREATE TABLE `spot`
(
    `spot_id` Bigint NOT NULL AUTO_INCREMENT,
    `spot_name` Varchar(20) NOT NULL,
    `address` Varchar(100) NOT NULL,
    `region` Varchar(10) NOT NULL,
    `detail` Varchar(50),
    `tel` Varchar(20) NOT NULL,
    `view_cnt` Int NOT NULL DEFAULT 0,
    `spot_x` Int,
    `spot_y` Int,
    PRIMARY KEY (`spot_id`)
)
;

-- Table bookmark

CREATE TABLE `bookmark`
(
    `bookmark_id` Bigint NOT NULL AUTO_INCREMENT,
    `user_id` Bigint NOT NULL,
    `spot_id` Bigint NOT NULL,
    PRIMARY KEY (`bookmark_id`)
)
;

CREATE INDEX `IX_Relationship9` ON `bookmark` (`user_id`)
;

CREATE INDEX `IX_Relationship10` ON `bookmark` (`spot_id`)
;

-- Table warning

CREATE TABLE `warning`
(
    `warning_id` Bigint NOT NULL AUTO_INCREMENT,
    `post_id` Bigint NOT NULL,
    `user_id` Bigint NOT NULL,
    `date` Datetime NOT NULL,
    `reason` Varchar(100) NOT NULL,
    PRIMARY KEY (`warning_id`)
)
;

CREATE INDEX `IX_Relationship4` ON `warning` (`post_id`)
;

CREATE INDEX `IX_Relationship5` ON `warning` (`user_id`)
;

-- Table tag

CREATE TABLE `tag`
(
    `tag_id` Int NOT NULL AUTO_INCREMENT,
    `value` Varchar(20) NOT NULL,
    PRIMARY KEY (`tag_id`)
)
;

-- Table spot_age_statistic

CREATE TABLE `spot_age_statistic`
(
    `spot_id` Bigint NOT NULL,
    `age_10` Int NOT NULL DEFAULT 0,
    `age_20` Int NOT NULL DEFAULT 0,
    `age_30` Int NOT NULL DEFAULT 0,
    `age_40` Int NOT NULL DEFAULT 0,
    `age_50` Int NOT NULL DEFAULT 0
)
;

ALTER TABLE `spot_age_statistic` ADD PRIMARY KEY (`spot_id`)
;

-- Table great

CREATE TABLE `great`
(
    `great_id` Bigint NOT NULL AUTO_INCREMENT,
    `user_id` Bigint NOT NULL,
    `post_id` Bigint NOT NULL,
    PRIMARY KEY (`great_id`)
)
;

CREATE INDEX `IX_Relationship6` ON `great` (`user_id`)
;

CREATE INDEX `IX_Relationship7` ON `great` (`post_id`)
;

-- Table file

CREATE TABLE `file`
(
    `file_id` Bigint NOT NULL AUTO_INCREMENT,
    `post_id` Bigint NOT NULL,
    `file_name` Varchar(255) NOT NULL,
    PRIMARY KEY (`file_id`)
)
;

CREATE INDEX `IX_Relationship11` ON `file` (`post_id`)
;

-- Table spot_tag

CREATE TABLE `spot_tag`
(
    `spot_tag_id` Bigint NOT NULL AUTO_INCREMENT,
    `spot_id` Bigint NOT NULL,
    `tag_id` Int NOT NULL,
    PRIMARY KEY (`spot_tag_id`)
)
;

CREATE INDEX `IX_Relationship12` ON `spot_tag` (`spot_id`)
;

CREATE INDEX `IX_Relationship17` ON `spot_tag` (`tag_id`)
;

-- Table post_tag

CREATE TABLE `post_tag`
(
    `post_tag_id` Bigint NOT NULL AUTO_INCREMENT,
    `post_id` Bigint NOT NULL,
    `tag_id` Int NOT NULL,
    PRIMARY KEY (`post_tag_id`)
)
;

CREATE INDEX `IX_Relationship13` ON `post_tag` (`post_id`)
;

CREATE INDEX `IX_Relationship16` ON `post_tag` (`tag_id`)
;

-- Table user_tag_statistic

CREATE TABLE `user_tag_statistic`
(
    `user_tag_id` Bigint NOT NULL AUTO_INCREMENT,
    `user_id` Bigint NOT NULL,
    `tag_id` Int NOT NULL,
    `tag_cnt` Int NOT NULL DEFAULT 0,
    PRIMARY KEY (`user_tag_id`)
)
;

CREATE INDEX `IX_Relationship14` ON `user_tag_statistic` (`tag_id`)
;

CREATE INDEX `IX_Relationship15` ON `user_tag_statistic` (`user_id`)
;

-- Create foreign keys (relationships) section -------------------------------------------------

ALTER TABLE `comment` ADD CONSTRAINT `WriteComment` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE NO ACTION ON UPDATE RESTRICT
;

ALTER TABLE `post` ADD CONSTRAINT `WritePost` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE NO ACTION ON UPDATE RESTRICT
;

ALTER TABLE `comment` ADD CONSTRAINT `MakeComment` FOREIGN KEY (`post_id`) REFERENCES `post` (`post_id`) ON DELETE CASCADE ON UPDATE RESTRICT
;

ALTER TABLE `warning` ADD CONSTRAINT `WarnedPost` FOREIGN KEY (`post_id`) REFERENCES `post` (`post_id`) ON DELETE CASCADE ON UPDATE RESTRICT
;

ALTER TABLE `warning` ADD CONSTRAINT `WarnPost` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE NO ACTION ON UPDATE RESTRICT
;

ALTER TABLE `great` ADD CONSTRAINT `PressGreat` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE ON UPDATE RESTRICT
;

ALTER TABLE `great` ADD CONSTRAINT `GreatPost` FOREIGN KEY (`post_id`) REFERENCES `post` (`post_id`) ON DELETE CASCADE ON UPDATE RESTRICT
;

ALTER TABLE `spot_age_statistic` ADD CONSTRAINT `AgeStatistic` FOREIGN KEY (`spot_id`) REFERENCES `spot` (`spot_id`) ON DELETE CASCADE ON UPDATE RESTRICT
;

ALTER TABLE `bookmark` ADD CONSTRAINT `AddBookmark` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE ON UPDATE RESTRICT
;

ALTER TABLE `bookmark` ADD CONSTRAINT `BookmarkedSpot` FOREIGN KEY (`spot_id`) REFERENCES `spot` (`spot_id`) ON DELETE CASCADE ON UPDATE RESTRICT
;

ALTER TABLE `file` ADD CONSTRAINT `FileReference` FOREIGN KEY (`post_id`) REFERENCES `post` (`post_id`) ON DELETE CASCADE ON UPDATE RESTRICT
;

ALTER TABLE `spot_tag` ADD CONSTRAINT `TaggedSpot` FOREIGN KEY (`spot_id`) REFERENCES `spot` (`spot_id`) ON DELETE CASCADE ON UPDATE RESTRICT
;

ALTER TABLE `post_tag` ADD CONSTRAINT `TaggedPost` FOREIGN KEY (`post_id`) REFERENCES `post` (`post_id`) ON DELETE CASCADE ON UPDATE RESTRICT
;

ALTER TABLE `user_tag_statistic` ADD CONSTRAINT `UserStatisticTagReference` FOREIGN KEY (`tag_id`) REFERENCES `tag` (`tag_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
;

ALTER TABLE `user_tag_statistic` ADD CONSTRAINT `TagStatistic` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
;

ALTER TABLE `post_tag` ADD CONSTRAINT `TagReferenceByPost` FOREIGN KEY (`tag_id`) REFERENCES `tag` (`tag_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
;

ALTER TABLE `spot_tag` ADD CONSTRAINT `TagReferenceBySpot` FOREIGN KEY (`tag_id`) REFERENCES `tag` (`tag_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
;

