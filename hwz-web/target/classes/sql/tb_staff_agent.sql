CREATE TABLE `tb_staff_agent` (
	`staff_agent_id` BIGINT(11) NOT NULL AUTO_INCREMENT COMMENT '自增长主键',
	`staff_code` varchar(50) DEFAULT NULL COMMENT '员工工号',
	`telephone` varchar(255) DEFAULT NULL COMMENT '员工电话',
	`email` varchar(255) DEFAULT NULL COMMENT '员工邮箱',
	   
	`real_name` varchar(50) DEFAULT NULL COMMENT '员工真实姓名',
	`login_pwd` varchar(50) DEFAULT NULL COMMENT '登录密码',
	`role_id` int(11) NOT NULL COMMENT '员工隶属角色',
	`staff_status` smallint(4) DEFAULT NULL COMMENT '审核状态 0=未审核 / 1=审核通过 / 2=审核拒绝',
	   
	`last_login_time` timestamp NULL DEFAULT NULL COMMENT '最后登录时间',
	`login_count` int(11) DEFAULT NULL COMMENT '登录次数',
	`created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
	`updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
	   
	PRIMARY KEY (`staff_agent_id`),
	UNIQUE KEY `idx_staff_code` (`staff_code`),
	UNIQUE KEY `idx_email` (`email`),
	UNIQUE KEY `idx_telephone` (`telephone`)
) 
ENGINE=InnoDB 
AUTO_INCREMENT=1311 
DEFAULT CHARSET=utf8 
COLLATE='utf8_unicode_ci'
COMMENT='员工表'
;