CREATE TABLE `tb_staff_role_resource` (
	`role_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '角色id',
	`resource_id` int(11) NOT NULL DEFAULT '0' COMMENT '资源id',
	`created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
	`updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
	PRIMARY KEY (`role_id`,`resource_id`),
	KEY `idx_role_id` (`role_id`)
) 
ENGINE=InnoDB 
AUTO_INCREMENT=133 
DEFAULT CHARSET=utf8 
COMMENT='功能'
COLLATE='utf8_unicode_ci'
;


INSERT INTO tb_staff_role_resource (role_id, resource_id)
VALUES('1', '100');
INSERT INTO tb_staff_role_resource (role_id, resource_id)
VALUES('1', '101');

INSERT INTO tb_staff_role_resource (role_id, resource_id)
VALUES('2', '102');
INSERT INTO tb_staff_role_resource (role_id, resource_id)
VALUES('2', '103');

