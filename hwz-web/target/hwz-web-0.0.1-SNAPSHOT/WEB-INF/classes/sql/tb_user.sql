DROP TABLE IF EXISTS `tb_user`;
create table `tb_user`(
	`user_id` BIGINT(11) NOT NULL AUTO_INCREMENT COMMENT '自增长主键',
	`nick_name` varchar(20) DEFAULT NULL COMMENT '昵称',
	`gender` varchar(2) DEFAULT NULL COMMENT '男/女',
	`mobile` varchar(20) DEFAULT NULL COMMENT '手机号',
	`icon_img_url` varchar(500) DEFAULT NULL COMMENT '头像',
	`birthdate` datetime DEFAULT NULL COMMENT '出生日期',
	
	`created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
	`updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
	
	PRIMARY KEY (`user_id`),
	UNIQUE KEY `mobile` (`mobile`)
)
ENGINE=InnoDB
COLLATE='utf8_unicode_ci'
AUTO_INCREMENT=624597124
ROW_FORMAT=DEFAULT
DEFAULT CHARSET=utf8
COMMENT='用户表'
;

INSERT INTO tb_user (`nick_name`, `gender`, `mobile`, `birthdate`, `created_at`, `updated_at`) 
VALUES ('哈尼伯特', '男', '13308629859', NULL);
INSERT INTO tb_user (`nick_name`, `gender`, `mobile`, `birthdate`, `created_at`, `updated_at`) 
VALUES ('静静', '女', '18824313197', NULL, NOW(), NOW());