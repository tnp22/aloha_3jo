CREATE TABLE `movie` (
	`id`	varchar(100)	NOT NULL PRIMARY KEY,
	`title`	varchar(255)	NOT NULL,
	`content`	text	NOT NULL,
	`type`	varchar(400)	NOT NULL,
	`release_date`	timestamp	NOT NULL,
	`reg_date`	timestamp	NOT NULL	DEFAULT CURRENT_TIMESTAMP,
	`upd_date`	timestamp	NOT NULL	DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE `users` (
	`id`	varchar(100)	NOT NULL PRIMARY KEY,
	`pw`	varchar(255)	NOT NULL,
	`name`	varchar(100)	NOT NULL,
	`email`	varchar(100)	NOT NULL,
	`enabled`	boolean	NOT NULL	DEFAULT false,
	`reg_date`	timestamp	NOT NULL	DEFAULT CURRENT_TIMESTAMP,
	`upd_date`	timestamp	NOT NULL	DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE `auth_list` (
	`id`	varchar(100)	NOT NULL PRIMARY KEY,
	`type_name`	varchar(100)	NOT NULL UNIQUE
);


CREATE TABLE `user_auth` (
	`id`    int AUTO_INCREMENT NOT NULL PRIMARY KEY,
	`user_id`	varchar(100)	NOT NULL,
	`auth`	varchar(100)	NOT NULL,
	FOREIGN KEY (`auth`) REFERENCES `auth_list`(`type_name`) ON UPDATE CASCADE
);

CREATE TABLE `cinema` (
	`id`	varchar(100)	NOT NULL PRIMARY KEY,
	`auth`	varchar(100)	NOT NULL,
	`area`	varchar(100)	NOT NULL,
	`area_sub`	varchar(100)	NOT NULL,
	FOREIGN KEY (`auth`) REFERENCES `auth_list`(`type_name`) ON UPDATE CASCADE
);

CREATE TABLE `theater` (
	`id`	varchar(100)	NOT NULL PRIMARY KEY,
	`cinema_id`	varchar(100)	NOT NULL,
	`map`	text	NOT NULL,
    FOREIGN KEY (`cinema_id`) REFERENCES `cinema`(id) ON DELETE RESTRICT
);



CREATE TABLE `notice` (
	`id`	varchar(100)	NOT NULL PRIMARY KEY, 
	`title`	varchar(255)	NOT NULL,
	`content`	text	NOT NULL,
	`reg_date`	timestamp	NOT NULL	DEFAULT CURRENT_TIMESTAMP,
	`upd_date`	timestamp	NOT NULL	DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE `files` (
	`id`	varchar(100)	NOT NULL PRIMARY KEY,
	`fk_table`	varchar(100)	NOT NULL,
	`fk_id`	varchar(100)	NOT NULL,
	`division`	varchar(200)	NOT NULL,
	`url`	varchar(1000)	NOT NULL
);

CREATE TABLE `review` (
	`id`	varchar(100)	NOT NULL PRIMARY KEY,
	`movie_id`	varchar(100)	NOT NULL,
	`user_id`	varchar(100)	NOT NULL,
	`content`	text	NOT NULL
);

CREATE TABLE `reserve` (
	`id`	varchar(100)	NOT NULL PRIMARY KEY,
	`user_id`	varchar(100)	NOT NULL,
	`theater_list_id`	varchar(100)	NOT NULL,
	`pos_x`	int	NOT NULL,
	`pos_y`	int	NOT NULL
);





CREATE TABLE `theater_list` (
	`id`	varchar(100)	NOT NULL PRIMARY KEY,
	`movie_id`	varchar(100)	NOT NULL,
	`theater_id`	varchar(100)	NOT NULL,
	`cinema_id`	varchar(100)	NOT NULL,
	`time`	timestamp	NOT NULL
);

CREATE TABLE `rating` (
	`id`	varchar(100)	NOT NULL PRIMARY KEY,
	`review_id`	varchar(100)	NOT NULL,
	`user_id`	varchar(100)	NOT NULL,
	`rating_vaule`	int	NOT NULL
);



CREATE TABLE `inspection` (
	`id`	varchar(100)	NOT NULL PRIMARY KEY,
	`cinema_id`	varchar(100)	NOT NULL,
	`start_time`	timestamp	NOT NULL,
	`end_time`	timestamp	NOT NULL
);

CREATE TABLE `cast` (
	`id`	varchar(100)	NOT NULL PRIMARY KEY,
	`movie_id`	varchar(100)	NOT NULL,
	`rule`	varchar(100)	NOT NULL,
	`name`	varchar(100)	NOT NULL
);

