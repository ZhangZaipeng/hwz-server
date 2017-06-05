DROP TABLE IF EXISTS `tb_user_agent`;
CREATE TABLE `tb_user_agent` (
    `user_agent_id` BIGINT(11) NOT NULL AUTO_INCREMENT COMMENT '用户登录编号',
   `user_id` BIGINT(11) DEFAULT NULL COMMENT '用户编号',
   `login_name` varchar(20) DEFAULT NULL COMMENT '登录名,电话',
   `login_pwd` varchar(100) DEFAULT NULL COMMENT '登录密码',
   `telephone` varchar(20) DEFAULT NULL COMMENT '电话',
   `email` varchar(128) DEFAULT NULL COMMENT '邮箱',
   
   `last_login_time` timestamp NULL DEFAULT NULL COMMENT '最后登录时间',
   `login_count` int(11) DEFAULT NULL COMMENT '登录次数',
   `deleted` smallint(1) DEFAULT '0' COMMENT '0 有效 1 无效',
   
   `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
   `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
   `platform` varchar(50) DEFAULT NULL COMMENT '登录平台',
   PRIMARY KEY (`user_agent_id`),
   UNIQUE KEY `idx_telephone` (`telephone`),
   UNIQUE KEY `idx_email` (`email`),
   UNIQUE KEY `idx_login_name` (`login_name`)
 ) 
 ENGINE=InnoDB 
 AUTO_INCREMENT=79196 
 DEFAULT CHARSET=utf8 
 COLLATE='utf8_unicode_ci'
 COMMENT='用户登录'
 ;
 