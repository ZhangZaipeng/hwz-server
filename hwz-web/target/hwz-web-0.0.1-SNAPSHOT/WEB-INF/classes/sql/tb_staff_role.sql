CREATE TABLE `tb_staff_role` (
	`role_id` int(11) NOT NULL COMMENT '角色id',
	`role_name` varchar(20) DEFAULT NULL COMMENT '角色名称',
	`deleted` smallint(1) DEFAULT '0' COMMENT '0 有效 1 无效',
	`created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
	`updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
	PRIMARY KEY (`role_id`)
) 
ENGINE=InnoDB 
DEFAULT CHARSET=utf8 
COMMENT='功能'
COLLATE='utf8_unicode_ci'
AUTO_INCREMENT=1
;

INSERT INTO tb_staff_role (role_id, role_name, deleted) 
VALUES('1', '油管家', '0');

INSERT INTO tb_staff_role (role_id, role_name, deleted) 
VALUES('2', '自驾游管理', '0');